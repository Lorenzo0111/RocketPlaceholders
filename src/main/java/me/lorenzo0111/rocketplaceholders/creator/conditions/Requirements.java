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

package me.lorenzo0111.rocketplaceholders.creator.conditions;

import me.lorenzo0111.rocketplaceholders.creator.conditions.types.*;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.utilities.HexParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Requirements handler
 */
public class Requirements {

    private Requirements() {}

    /**
     * @param type Type of the requirement
     * @param value <b>Optional</b> Value of the requirement, used for JAVASCRIPT,MONEY AND PERMISSION
     * @param material <b>Optional</b> Material of the item, used for ITEM
     * @param itemName <b>Optional</b> Name of the item, used for ITEM
     * @param itemLore <b>Optional</b> Lore of the item, used for ITEM
     * @return A requirement
     */
    @Nullable
    public static Requirement createRequirement(RequirementType type, @Nullable String value, @Nullable Material material, @Nullable String itemName,@Nullable List<String> itemLore) throws InvalidConditionException {
        switch (type) {
            case ITEM:
                return HasItemCondition.create(material,itemName,itemLore);
            case JAVASCRIPT:
                return JSCondition.create(value);
            case MONEY:
                return HasMoneyCondition.create(value);
            case PERMISSION:
                return HasPermissionCondition.create(value);
            case GROUP:
                return HasGroupCondition.create(value);
            case TEXT:
                return TextCondition.create(value);
            default:
                return null;
        }
    }

    /**
     * Generate a condition from a ConfigurationSection
     * @param section ConfigurationSection that contains a condition
     * @return A requirement
     */
    @Nullable
    public static Requirement parseRequirement(ConfigurationSection section) throws InvalidConditionException {
        RequirementType type;

        if (section.get("type") == null) {
            return null;
        }

        type = RequirementType.valueOf(section.getString("type"));

        return createRequirement(
                type,
                section.getString("value"),
                Material.getMaterial(section.getString("material","")),
                section.getString("name"),
                section.getStringList("lore")
        );
    }

    /**
     * @param text Text to colorize
     * @return Colorized text
     */
    @Nullable
    public static String translateColors(String text) {
        if (text == null) {
            return null;
        }

        return HexParser.text(text);
    }

    /**
     * @param text List of texts to translate
     * @return List of colorized texts
     */
    @Nullable
    public static List<String> translateColors(List<String> text) {
        if (text == null) {
            return null;
        }

        List<String> translated = new ArrayList<>();

        for (String string : text) {
            translated.add(HexParser.text(string));
        }

        return translated;
    }

}
