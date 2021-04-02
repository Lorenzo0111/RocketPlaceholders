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

package me.lorenzo0111.rocketplaceholders.creator;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class PlaceholderAPICreator extends PlaceholderExpansion {

    /*

    Plugin by Lorenzo0111

     */

    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;

    public PlaceholderAPICreator(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lorenzo0111";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {

        if (player == null) {
            return null;
        }

        if (!player.isOnline()) {
            return null;
        }

        final Player onlinePlayer = player.getPlayer();

        if (onlinePlayer == null) {
            return null;
        }

        final Placeholder placeholder = placeholdersManager.searchPlaceholder(identifier);

        if (placeholder == null) {
            return null;
        }

        if (!placeholder.hasConditionNodes()) {
            return PlaceholderAPI.setPlaceholders(player,placeholder.getText());
        }

        List<ConditionNode> conditionNodes = Objects.requireNonNull(placeholder.getConditionNodes());
        for (ConditionNode node : conditionNodes) {
            if (((Requirement) node.getCondition()).apply(onlinePlayer)) {
                plugin.debug("Applied: " + node.getRequirement());
                return PlaceholderAPI.setPlaceholders(player,node.getText());
            }
        }

        return PlaceholderAPI.setPlaceholders(player,placeholder.getText());

    }
}