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

package me.lorenzo0111.rocketplaceholders.creator.conditions.types;

import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.RequirementType;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirements;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class HasItemCondition extends Requirement {
    private final ItemStack item;

    @Override
    public String toString() {
        return "{" +
                "item=" + item + "," +
                "type=" + this.getType() +
                '}';
    }

    public HasItemCondition(ItemStack item) {
        this.item = item;
        this.getDatabaseInfo().put("item_name", Objects.requireNonNull(item.getItemMeta()).getDisplayName());
        this.getDatabaseInfo().put("item_lore",Objects.requireNonNull(item.getItemMeta()).getLore());
        this.getDatabaseInfo().put("item_material",item.getType().toString());
    }

    @Override
    public boolean apply(Player player) {
        return player.getInventory().containsAtLeast(item,item.getAmount()) || hasInArmor(player,item);
    }

    public static HasItemCondition create(Material material, String itemName, List<String> itemLore) throws InvalidConditionException {
        if (material == null) {
            throw new InvalidConditionException("Material cannot be null");
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(Requirements.translateColors(itemName));
            meta.setLore(Requirements.translateColors(itemLore));
            item.setItemMeta(meta);
        }

        return new HasItemCondition(item);
    }

    @Override
    public RequirementType getType() {
        return RequirementType.ITEM;
    }

    private boolean hasInArmor(@NotNull Player player, ItemStack item) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.isSimilar(item)) {
                return true;
            }
        }

        return false;
    }
}
