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

import com.glyart.mystral.database.AsyncDatabase;
import com.glyart.mystral.database.Credentials;
import com.glyart.mystral.database.Mystral;
import com.glyart.mystral.sql.BatchSetter;
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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DatabaseManager {
    private final RocketPlaceholders plugin;
    private final ConfigurationSection mysqlSection;
    private AsyncDatabase database;
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

        Credentials credentials = Credentials.builder()
                .host(mysqlSection.getString("ip", "127.0.0.1"))
                .port(mysqlSection.getInt("port", 3306))
                .user(mysqlSection.getString("username", "root"))
                .password(mysqlSection.getString("password", ""))
                .schema(mysqlSection.getString("database"))
                .pool("RocketPlaceholders Pool")
                .build();

        Executor executor = (cmd) -> Bukkit.getScheduler().runTaskAsynchronously(plugin, cmd);
        this.database = Mystral.newAsyncDatabase(credentials, executor);
    }

    public void createTables() {
        new BukkitRunnable() {
            @Override
            public void run() {
                database.update("CREATE TABLE IF NOT EXISTS `rp_placeholders` (" +
                        "`identifier` varchar(255) UNIQUE NOT NULL," +
                        "`text` varchar(255) NOT NULL," +
                        "`settings` TEXT," +
                        "PRIMARY KEY (`identifier`)" +
                        ");",false);
                database.update("CREATE TABLE IF NOT EXISTS `rp_nodes` (" +
                        "`identifier` varchar(255) NOT NULL," +
                        "`type` varchar(255) NOT NULL," +
                        "`value` varchar(255)," +
                        "`item_material` varchar(255)," +
                        "`item_name` varchar(255)," +
                        "`item_lore` varchar(255)," +
                        "`text` varchar(255)" +
                        ");", false);

                upgrade();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void upgrade() {
        database.update("CREATE TABLE IF NOT EXISTS `rp_info` (" +
                "`key` varchar(255) NOT NULL," +
                "`value` TEXT NOT NULL," +
                "PRIMARY KEY (`key`)" +
                ");", false);

        CompletableFuture<Integer> future = database.query("SELECT * FROM `rp_info`;", (set) -> set.getInt("version"));
        future.whenComplete((version,error) -> {
            if (version == null || version == 0) {
                database.update("INSERT INTO `rp_info`(`key`,`value`) VALUES('version','2.0')", false);
                database.update("ALTER TABLE `rp_placeholders`" +
                        "ADD `settings` TEXT;",false);
            }
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<Multimap<String, ConditionNode>> getNodes() {
        CompletableFuture<Multimap<String,ConditionNode>> completableFuture = new CompletableFuture<>();

        CompletableFuture<List<RawNode>> map =  database.queryForListOrElseGet("SELECT * FROM rp_nodes;", (set, i) -> {
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
            return new RawNode(set.getString("identifier"), new ConditionNode(requirement, set.getString("text")));
        }, ArrayList::new);

        map.whenComplete((list, error) -> {
            if (error != null) {
                plugin.getLogger().log(Level.WARNING, "An error has occurred while querying from database.." ,error);
                completableFuture.complete(null);
                return;
            }

            Multimap<String,ConditionNode> multimap = ArrayListMultimap.create();
            for (RawNode rawNode : list) {
                multimap.put(rawNode.getOwner(), rawNode.getNode());
            }
            completableFuture.complete(multimap);
        });

        return completableFuture;
    }


    public void sync() {
        new BukkitRunnable() {

            @Override
            public void run() {
                plugin.getLogger().info("Saving data to the database..");

                final Storage internalPlaceholders = getStorageManager().getInternalPlaceholders();

                List<Placeholder> placeholders = new ArrayList<>(internalPlaceholders.getMap().values());

                database.batchUpdate("insert into rp_placeholders (`identifier`, `text`, `settings`) VALUES (?,?,?);", new BatchSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement statement, int i) throws SQLException {
                        Placeholder placeholder = placeholders.get(i);
                        statement.setString(1, placeholder.getIdentifier());
                        statement.setString(2, placeholder.getText());
                        statement.setString(3, gson.toJson(placeholder.getSettings()));
                    }

                    @Override
                    public int getBatchSize() {
                        return placeholders.size();
                    }
                });

                List<Placeholder> collect = internalPlaceholders.getMap()
                        .values()
                        .stream()
                        .filter(Placeholder::hasConditionNodes)
                        .collect(Collectors.toList());

                List<RawNode> nodes = new ArrayList<>();
                for (Placeholder placeholder : collect) {
                    List<ConditionNode> conditionNodes = placeholder.getConditionNodes();
                    if (conditionNodes == null) continue;
                    for (ConditionNode node : conditionNodes) {
                        if (node.getRequirement().getType().equals(RequirementType.API)) continue;
                        nodes.add(new RawNode(placeholder.getIdentifier(), node));
                    }
                }

                database.batchUpdate("insert into rp_nodes (`identifier`, `type`, `value`, `item_material`, `item_name`,`item_lore`, `text`) VALUES (?,?,?,?,?,?,?);", new BatchSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement statement, int i) throws SQLException {
                        RawNode node = nodes.get(i);
                        statement.setString(1, node.getOwner());
                        statement.setString(2, node.getNode().getRequirement().getType().toString());
                        statement.setString(3, null);
                        statement.setString(4, null);
                        statement.setString(5, null);
                        statement.setString(6, null);
                        statement.setString(7, node.getNode().getText());

                        switch (node.getNode().getRequirement().getType()) {
                            case GROUP:
                            case JAVASCRIPT:
                            case MONEY:
                            case PERMISSION:
                                statement.setObject(3, node.getNode().getRequirement().getDatabaseInfo().get("value"));
                                break;
                            case ITEM:
                                statement.setObject(4, node.getNode().getRequirement().getDatabaseInfo().get("item_material"));
                                statement.setObject(5, node.getNode().getRequirement().getDatabaseInfo().get("item_name"));

                                if (node.getNode().getRequirement().getDatabaseInfo().containsKey("item_lore") && node.getNode().getRequirement().getDatabaseInfo().get("item_lore") != null) {
                                    statement.setObject(6,
                                            gson.toJson(node.getNode().getRequirement().getDatabaseInfo().get("item_lore")));
                                }
                                break;
                            case TEXT:
                                statement.setString(3, node.getNode().getRequirement().getDatabaseInfo().get("one") + "%%" + node.getNode().getRequirement().getDatabaseInfo().get("two"));
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return nodes.size();
                    }
                });
            }

        }.runTaskAsynchronously(plugin);
    }

    public CompletableFuture<Map<String, Placeholder>> getFromDatabase() {
        CompletableFuture<Map<String, Placeholder>> completableFuture = new CompletableFuture<>();

        getNodes().thenAccept(nodes -> {
            Map<String, Placeholder> hashMap = new HashMap<>();

            CompletableFuture<List<Placeholder>> future = database.queryForList("SELECT * FROM rp_placeholders;", (set,i) -> {
                PlaceholderSettings settings = null;
                if (set.getString("settings") != null)
                    settings = gson.fromJson(set.getString("settings"), PlaceholderSettings.class);

                return new Placeholder(set.getString("identifier"), plugin, set.getString("text"), new ArrayList<>(nodes.get(set.getString("identifier"))), settings);
            });

            future.whenComplete((placeholders,error) -> {
                if (error != null) {
                    plugin.getLogger().log(Level.WARNING, "An error has occurred while loading data from database.", error);
                    completableFuture.complete(null);
                    return;
                }

                for (Placeholder placeholder : placeholders) {
                    hashMap.put(placeholder.getIdentifier(), placeholder);
                }
                completableFuture.complete(hashMap);
            });
        });

        return completableFuture;
    }

    public boolean isMain() {
        return mysqlSection.getBoolean("main");
    }

    public CompletableFuture<Void> removeAll() {
        CompletableFuture<Integer> placeholders = database.update("DELETE FROM rp_placeholders;", false);
        CompletableFuture<Integer> nodes = database.update("DELETE FROM rp_nodes;", false);

        return CompletableFuture.allOf(placeholders,nodes);
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
