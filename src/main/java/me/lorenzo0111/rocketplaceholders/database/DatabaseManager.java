package me.lorenzo0111.rocketplaceholders.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final RocketPlaceholders plugin;
    private ConfigurationSection mysqlSection;
    private Connection connection;

    public DatabaseManager(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.mysqlSection = this.plugin.getConfig().getConfigurationSection("mysql");

        if (mysqlSection == null) {
            this.plugin.getLogger().severe("MySQL configuration section not found. Please add it.");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + mysqlSection.getString("ip") + ":" + mysqlSection.getInt("port") + "/" + mysqlSection.getString("database"), mysqlSection.getString("username"), mysqlSection.getString("password"));
        } catch (ClassNotFoundException | SQLException e) {
            this.plugin.getLogger().severe("MySQL driver is not installed, please install it to use the mysql function");
        }
    }

    public void createTables() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final Statement statement = connection.createStatement();

                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_placeholders` (" +
                            "`identifier` varchar(255) UNIQUE NOT NULL," +
                            "`text` varchar(255) NOT NULL," +
                            "PRIMARY KEY (`identifier`)" +
                            ");");


                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_nodes` (" +
                            "`identifier` varchar(255) NOT NULL," +
                            "`permission` varchar(255) NOT NULL," +
                            "`text` varchar(255) NOT NULL" +
                            ");");

                } catch (SQLException exception) {
                    plugin.getLogger().severe("Error while creating tables: " + exception);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void sync() {
        new BukkitRunnable() {

            @Override
            public void run() { 
                final Storage internalPlaceholders = plugin.getStorageManager().getInternalPlaceholders();
                final HashMap<String, Placeholder> hashMap = internalPlaceholders.getHashMap();

                try {
                    final PreparedStatement statement = connection.prepareStatement("insert into rp_placeholders (`identifier`, `text`) VALUES (?,?);");
                    final PreparedStatement nodeStatement = connection.prepareStatement("insert into rp_nodes (`identifier`, `permission`, `text`) VALUES (?,?,?);");

                    hashMap.forEach((s, placeholder) -> {
                        try {
                            statement.setString(1, s);
                            statement.setString(2, placeholder.getText());
                            statement.executeUpdate();

                            if (placeholder.hasPermissionNodes()) {

                                for (PermissionNode node : placeholder.getPermissionNodes()) {
                                    nodeStatement.setString(1, s);
                                    nodeStatement.setString(2, node.getPermission());
                                    nodeStatement.setString(3, node.getText());
                                    nodeStatement.executeUpdate();
                                }

                            }
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    });
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }


                
            }

        }.runTaskAsynchronously(plugin);
    }

    public CompletableFuture<Multimap<String, PermissionNode>> getNodes() {
        CompletableFuture<Multimap<String,PermissionNode>> completableFuture = new CompletableFuture<>();

        new BukkitRunnable() {

            @Override
            public void run() {

                try {
                    final Statement statement = connection.createStatement();

                    ResultSet set = statement.executeQuery("SELECT * FROM rp_nodes;");

                    Multimap<String,PermissionNode> nodes = ArrayListMultimap.create();

                    while (set.next()) {
                        nodes.put(set.getString("identifier"), new PermissionNode(set.getString("permission"), set.getString("text")));
                    }

                    completableFuture.complete(nodes);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

            }

        }.runTaskAsynchronously(plugin);

        return completableFuture;
    }

    public CompletableFuture<HashMap<String, Placeholder>> getFromDatabase() {
        CompletableFuture<HashMap<String, Placeholder>> completableFuture = new CompletableFuture<>();

        getNodes().thenAccept(nodes -> new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM rp_placeholders;");
                    ResultSet resultSet = statement.executeQuery();

                    HashMap<String, Placeholder> hashMap = new HashMap<>();

                    while (resultSet.next()) {
                        hashMap.put(resultSet.getString("identifier"), new Placeholder(resultSet.getString("identifier"), resultSet.getString("text"), new ArrayList<>(nodes.get(resultSet.getString("identifier")))));
                    }

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

                    statement.executeUpdate("DELETE FROM rp_nodes;");
                    statement.executeUpdate("DELETE FROM rp_placeholders;");

                    completableFuture.complete(true);
                } catch (SQLException exception) {
                    exception.printStackTrace();

                    completableFuture.complete(false);
                }
            }

        }.runTaskAsynchronously(plugin);

        return completableFuture;
    }

    public void reload(InternalPlaceholders internalPlaceholders) {
        this.getFromDatabase().thenAccept(placeholders -> {
            internalPlaceholders.reloadPlaceholders();
            plugin.getStorageManager().getInternalPlaceholders().getHashMap().putAll(placeholders);
            plugin.getLogger().info("Loaded " + placeholders.size() + " placeholders from the database!");
        });
    }

    public void setMysqlSection(ConfigurationSection mysqlSection) {
        this.mysqlSection = mysqlSection;
    }
}
