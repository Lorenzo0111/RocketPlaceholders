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

package me.lorenzo0111.rocketplaceholders.providers;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class Provider {
    protected final RocketPlaceholders plugin;
    protected final StorageManager manager;

    public Provider(RocketPlaceholders plugin, PlaceholdersManager manager) {
        this.plugin = plugin;
        this.manager = manager.getStorageManager();
    }

    public Provider(RocketPlaceholders plugin, StorageManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Nullable
    public String provide(@Nullable OfflinePlayer player, String identifier) {
        final Placeholder placeholder = manager.get(identifier);

        if (placeholder == null) {
            return null;
        }

        if (player == null || !player.isOnline()) {
            return placeholder.getText();
        }

        Player onlinePlayer = player.getPlayer();

        String cachedText = RocketPlaceholders.getApi().getCacheStorage().getText(placeholder,player.getUniqueId());
        if (cachedText != null) {
            plugin.debug("Returning cached text for " + placeholder.getIdentifier());
            return cachedText;
        }

        String userText = RocketPlaceholders.getApi().getUserStorage().getText(placeholder,player.getUniqueId());
        if (userText != null) {
            return this.parse(placeholder,player,userText);
        }

        if (!placeholder.hasConditionNodes()) {
            return this.parse(placeholder,player,placeholder.getText());
        }

        List<ConditionNode> conditionNodes = Objects.requireNonNull(placeholder.getConditionNodes());
        for (ConditionNode node : conditionNodes) {
            if (node.getRequirement().apply(onlinePlayer)) {
                plugin.debug("Applied: " + node.getRequirement());
                try {
                    String result = this.parse(placeholder,player,node.getText());
                    RocketPlaceholders.getApi().getCacheStorage().setText(placeholder,player.getUniqueId(), result);
                    return result;
                } catch (InvalidConditionException e) {
                    plugin.getLogger().severe(String.format("An error has occurred while parsing placeholder %s: %s", placeholder.getIdentifier(), e.getMessage()));
                    return null;
                }
            }
        }

        try {
            return this.parse(placeholder,player,placeholder.getText());
        } catch (InvalidConditionException e) {
            plugin.getLogger().severe(String.format("An error has occurred while parsing placeholder %s: %s", placeholder.getIdentifier(), e.getMessage()));
            return null;
        }

    }

    public abstract String parse(Placeholder placeholder, OfflinePlayer player, String text) throws InvalidConditionException;
}
