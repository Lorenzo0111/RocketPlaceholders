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

package me.lorenzo0111.rocketplaceholders.creator.conditions.engine;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.conditions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.RequirementType;
import me.lorenzo0111.rocketplaceholders.creator.conditions.types.HasItemCondition;
import me.lorenzo0111.rocketplaceholders.creator.conditions.types.HasMoneyCondition;
import me.lorenzo0111.rocketplaceholders.creator.conditions.types.HasPermissionCondition;
import me.lorenzo0111.rocketplaceholders.creator.conditions.types.JSCondition;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Requirements {
    private final RocketPlaceholders plugin;

    public Requirements(RocketPlaceholders plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public Requirement parseRequirement(ConfigurationSection section) {
        RequirementType type;

        if (section.get("type") == null) {
            return null;
        }

        type = RequirementType.valueOf(section.getString("type"));

        switch (type) {
            case ITEM:
                Material material = Material.valueOf(section.getString("material"));
                String name = section.getString("name");
                List<String> lore = section.getStringList("lore");

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                boolean translateColors = false;

                if (section.contains("colors")) {
                    translateColors = section.getBoolean("colors");
                }

                if (meta != null) {
                    meta.setDisplayName(translateColors ? translateColors(name) : name);
                    meta.setLore(translateColors ? translateColors(lore) : lore);
                    item.setItemMeta(meta);
                }

                return new HasItemCondition(item,plugin);
            case JAVASCRIPT:
                String expression = section.getString("value");
                if (expression == null) {
                    throw new InvalidConditionException("Expression cannot be null");
                }

                return new JSCondition(plugin,expression);
            case MONEY:
                long amount = section.getLong("value");

                return new HasMoneyCondition(plugin,amount);
            case PERMISSION:
                String permission = section.getString("value");
                if (permission == null) {
                    throw new InvalidConditionException("Expression cannot be null");
                }

                return new HasPermissionCondition(plugin,permission);
        }

        return null;
    }

    @Nullable
    private String translateColors(String text) {
        if (text == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Nullable
    private List<String> translateColors(List<String> text) {
        if (text == null) {
            return null;
        }

        List<String> translated = new ArrayList<>();

        for (String string : text) {
            translated.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        return translated;
    }

}
