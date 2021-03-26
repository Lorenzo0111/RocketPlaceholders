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

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final RocketPlaceholders plugin;
    private final ConfigurationSection mysqlSection;
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
        } catch (ClassNotFoundException e) {
            this.plugin.getLogger().info("MySQL driver is not installed, please install it to use the mysql function");
            return;
        }

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + mysqlSection.getString("ip") + ":" + mysqlSection.getInt("port") + "/" + mysqlSection.getString("database"), mysqlSection.getString("username"), mysqlSection.getString("password"));
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

                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS `rp_placeholders` (" +
                            "`identifier` varchar(255) UNIQUE NOT NULL," +
                            "`text` varchar(255) NOT NULL," +
                            "PRIMARY KEY (`identifier`)" +
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
                final Storage internalPlaceholders = getStorageManager().getInternalPlaceholders();

                final Map<String, Placeholder> hashMap = internalPlaceholders.getMap();

                try {
                    final PreparedStatement statement = connection.prepareStatement("insert into rp_placeholders (`identifier`, `text`) VALUES (?,?);");

                    hashMap.forEach((s, placeholder) -> {
                        try {
                            statement.setString(1, s);
                            statement.setString(2, placeholder.getText());
                            statement.executeUpdate();
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

    public CompletableFuture<Map<String, Placeholder>> getFromDatabase() {
        CompletableFuture<Map<String, Placeholder>> completableFuture = new CompletableFuture<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM rp_placeholders;");
                    ResultSet resultSet = statement.executeQuery();

                    Map<String, Placeholder> hashMap = new HashMap<>();

                    while (resultSet.next()) {
                        hashMap.put(resultSet.getString("identifier"), new Placeholder(resultSet.getString("identifier"), plugin, resultSet.getString("text")));
                    }

                    completableFuture.complete(hashMap);
                }catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);

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
            this.getStorageManager().getInternalPlaceholders().getMap().putAll(placeholders);
            plugin.getLogger().info("Loaded " + placeholders.size() + " placeholders from the database!");
        });
    }

    public StorageManager getStorageManager() {
        return Objects.requireNonNull(plugin.getStorageManager(), "StorageManager cannot be null.");
    }
}
