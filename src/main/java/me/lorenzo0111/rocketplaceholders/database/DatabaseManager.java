/*
 *  This file is part of RocketPlaceholders, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.lorenzo0111.rocketplaceholders.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.RequirementType;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirements;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.PlaceholderSettings;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final RocketPlaceholders plugin;
    private final ConfigurationSection mysqlSection;
    private Connection connection;
    private final Gson gson = new Gson();

    public DatabaseManager(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.mysqlSection = this.plugin.getConfig().getConfigurationSection("mysql");

        if (mysqlSection == null) {
            this.plugin.getLogger().severe("MySQL configuration section not found. Please add it.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            this.plugin.getLogger().info("MySQL driver is not installed, please install it to use the mysql function");
            return;
        }

        final String jdbc = mysqlSection.getString("jdbc");

        try {
            if (jdbc == null) {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + mysqlSection.getString("ip") + ":" + mysqlSection.getInt("port") + "/" + mysqlSection.getString("database"), mysqlSection.getString("username"), mysqlSection.getString("password"));
            } else {
                this.connection = DriverManager.getConnection(jdbc);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public void createTables() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final Statement statement = connection.createStatement();

                    upgrade(statement);

                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_placeholders` (" +
                            "`identifier` varchar(255) UNIQUE NOT NULL," +
                            "`text` varchar(255) NOT NULL," +
                            "`settings` TEXT," +
                            "PRIMARY KEY (`identifier`)" +
                            ");");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_nodes` (" +
                            "`identifier` varchar(255) NOT NULL," +
                            "`type` varchar(255) NOT NULL," +
                            "`value` varchar(255)," +
                            "`item_material` varchar(255)," +
                            "`item_name` varchar(255)," +
                            "`item_lore` varchar(255)," +
                            "`text` varchar(255)" +
                            ");");

                    statement.close();
                } catch (SQLException exception) {
                    plugin.getLogger().severe("Error while creating tables: " + exception);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void upgrade(Statement statement) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(null, null, "rp_info", new String[] {"TABLE"});

        if (!resultSet.next()) {
            // Upgrade

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_info` (" +
                    "`key` varchar(255) NOT NULL," +
                    "`value` TEXT NOT NULL," +
                    "PRIMARY KEY (`key`)" +
                    ");");

            statement.executeUpdate("INSERT INTO `rp_info`(`key`,`value`) VALUES('version','2.0')");

            statement.executeUpdate("ALTER TABLE `rp_placeholders`" +
                    "ADD `settings` TEXT;");
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<Multimap<String, ConditionNode>> getNodes() {
        CompletableFuture<Multimap<String,ConditionNode>> completableFuture = new CompletableFuture<>();

        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    final Statement statement = connection.createStatement();

                    ResultSet set = statement.executeQuery("SELECT * FROM rp_nodes;");

                    Multimap<String,ConditionNode> nodes = ArrayListMultimap.create();

                    while (set.next()) {
                        List<String> itemLore = new ArrayList<>();
                        Material material = null;
                        if (set.getString("item_lore") != null) {
                            Type listType = new TypeToken<List<String>>() {}.getType();
                            itemLore = gson.fromJson(set.getString("item_lore"),listType);
                        }

                        if (set.getString("item_material") != null) {
                            material = Material.getMaterial(set.getString("item_material"));
                        }


                        final Requirement requirement = Requirements.createRequirement(RequirementType.valueOf(set.getString("type")),set.getString("value"), material,set.getString("item_name"),itemLore);
                        nodes.put(set.getString("identifier"), new ConditionNode(requirement, set.getString("text")));
                    }

                    statement.close();
                    set.close();
                    completableFuture.complete(nodes);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

            }

        }.runTaskAsynchronously(plugin);

        return completableFuture;
    }


    public void sync() {
        new BukkitRunnable() {

            @Override
            public void run() {
                final Storage internalPlaceholders = getStorageManager().getInternalPlaceholders();

                final Map<String, Placeholder> hashMap = internalPlaceholders.getMap();

                try {
                    final PreparedStatement statement = connection.prepareStatement("insert into rp_placeholders (`identifier`, `text`, `settings`) VALUES (?,?,?);");
                    final PreparedStatement nodeStatement = connection.prepareStatement("insert into rp_nodes (`identifier`, `type`, `value`, `item_material`, `item_name`,`item_lore`, `text`) VALUES (?,?,?,?,?,?,?);");

                    hashMap.forEach((s, placeholder) -> {
                        try {
                            statement.setString(1, s);
                            statement.setString(2, placeholder.getText());
                            statement.setString(3, gson.toJson(placeholder.getSettings()));
                            statement.executeUpdate();

                            if (placeholder.hasConditionNodes() && placeholder.getConditionNodes() != null) {

                                for (ConditionNode node : placeholder.getConditionNodes()) {
                                    if (!node.getRequirement().getType().equals(RequirementType.API)) {
                                        nodeStatement.setString(1, s);
                                        nodeStatement.setString(2, node.getRequirement().getType().toString());
                                        nodeStatement.setString(3,null); // Value
                                        nodeStatement.setString(4,null); // Item Material
                                        nodeStatement.setString(5,null); // Item Name
                                        nodeStatement.setString(6,null); // Item Lore
                                        nodeStatement.setString(7, node.getText());

                                        switch (node.getRequirement().getType()) {
                                            case GROUP:
                                            case JAVASCRIPT:
                                            case MONEY:
                                            case PERMISSION:
                                                nodeStatement.setObject(3,node.getRequirement().getDatabaseInfo().get("value"));
                                                break;
                                            case ITEM:
                                                nodeStatement.setObject(4,node.getRequirement().getDatabaseInfo().get("item_material"));
                                                nodeStatement.setObject(5,node.getRequirement().getDatabaseInfo().get("item_name"));

                                                if (node.getRequirement().getDatabaseInfo().containsKey("item_lore") && node.getRequirement().getDatabaseInfo().get("item_lore") != null) {
                                                    nodeStatement.setObject(6,
                                                            gson.toJson(node.getRequirement().getDatabaseInfo().get("item_lore")));
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                        nodeStatement.executeUpdate();
                                    }
                                }

                            }
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    });
                    statement.close();
                    nodeStatement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }



            }

        }.runTaskAsynchronously(plugin);
    }

    public CompletableFuture<Map<String, Placeholder>> getFromDatabase() {
        CompletableFuture<Map<String, Placeholder>> completableFuture = new CompletableFuture<>();

        getNodes().thenAccept(nodes -> new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM rp_placeholders;");
                    ResultSet resultSet = statement.executeQuery();

                    Map<String, Placeholder> hashMap = new HashMap<>();

                    while (resultSet.next()) {
                        PlaceholderSettings settings = null;
                        if (resultSet.getString("settings") != null)
                            settings = gson.fromJson(resultSet.getString("settings"), PlaceholderSettings.class);

                        hashMap.put(resultSet.getString("identifier"), new Placeholder(resultSet.getString("identifier"), plugin, resultSet.getString("text"), new ArrayList<>(nodes.get(resultSet.getString("identifier"))), settings));
                    }

                    resultSet.close();
                    statement.close();
                    completableFuture.complete(hashMap);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

        }.runTaskAsynchronously(plugin));

        return completableFuture;
    }

    public boolean isMain() {
        return mysqlSection.getBoolean("main");
    }

    public CompletableFuture<Boolean> removeAll() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("DELETE FROM rp_placeholders;");
                    statement.executeUpdate("DELETE FROM rp_nodes;");

                    completableFuture.complete(true);
                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();

                    completableFuture.complete(false);
                }
            }

        }.runTaskAsynchronously(plugin);

        return completableFuture;
    }

    public void reload(ConfigManager configManager) {
        this.getFromDatabase().thenAccept(placeholders -> {
            try {
                configManager.reloadPlaceholders();
            } catch (IOException | InvalidConditionException e) {
                e.printStackTrace();
            }
            this.getStorageManager().getInternalPlaceholders().getMap().putAll(placeholders);
            plugin.getLogger().info("Loaded " + placeholders.size() + " placeholders from the database!");
        });
    }

    public StorageManager getStorageManager() {
        return Objects.requireNonNull(plugin.getStorageManager(), "StorageManager cannot be null.");
    }
}
