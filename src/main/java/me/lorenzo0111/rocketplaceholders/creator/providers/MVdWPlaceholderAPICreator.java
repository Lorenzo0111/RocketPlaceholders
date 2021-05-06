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

package me.lorenzo0111.rocketplaceholders.creator.providers;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class MVdWPlaceholderAPICreator {
    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;

    public MVdWPlaceholderAPICreator(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager.hook(this);
        this.reload();
    }

    public void reload() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin,() -> placeholdersManager.getStorageManager().getAll().forEach((s, placeholder) -> PlaceholderAPI.registerPlaceholder(plugin, String.format("rp_%s",s), (event) -> {
            if (!event.isOnline()) return null;

            if (!placeholder.hasConditionNodes()) {
                return this.parse(placeholder, event.getPlayer(), placeholder.getText());
            }

            List<ConditionNode> conditionNodes = Objects.requireNonNull(placeholder.getConditionNodes());
            for (ConditionNode node : conditionNodes) {
                if (((Requirement) node.getCondition()).apply(event.getPlayer())) {
                    plugin.debug("Applied: " + node.getRequirement());
                    return this.parse(placeholder, event.getPlayer(), node.getText());
                }
            }

            return this.parse(placeholder, event.getPlayer(), placeholder.getText());
        })));
    }

    private String parse(Placeholder placeholder, Player player, String text) {
        return placeholder.parseJS(PlaceholderAPI.replacePlaceholders(player,text));
    }

}
