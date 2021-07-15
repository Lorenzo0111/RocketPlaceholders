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

package me.lorenzo0111.rocketplaceholders.utilities;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Objects;

public final class GuiUtils {

    public static PaginatedGui createGui(String title) {
        final String prefix = "&8&l» &7";
        final PaginatedGui gui = new PaginatedGui(3, 17, ChatColor.translateAlternateColorCodes('&',prefix + title), EnumSet.noneOf(InteractionModifier.class));
        gui.setDefaultClickAction(a -> a.setCancelled(true));
        return gui;
    }

    public static void setPageItems(PaginatedGui gui) {
        ItemStack item = Objects.requireNonNull(XMaterial.ARROW.parseItem(), "Arrow does not exists.");

        GuiItem left = ItemBuilder.from(item)
                .name(Component.text("§8§l» §7Previous"))
                .asGuiItem(e -> {
                    e.setCancelled(true);
                    gui.previous();
                });
        GuiItem right = ItemBuilder.from(item)
                .name(Component.text("§8§l» §7Next"))
                .asGuiItem(e -> {
                    e.setCancelled(true);
                    gui.next();
                });
        gui.setItem(21,left);
        gui.setItem(23,right);

        gui.getFiller().fillBottom(ItemBuilder.from(Material.GLASS_PANE).name(Component.text("§r")).asGuiItem());
    }

    public static PaginatedGui createConditionsGui(Placeholder placeholder) {
        final PaginatedGui gui = createGui(placeholder.getIdentifier() + " &8&l» &7Conditions");

        setPageItems(gui);

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for (ConditionNode node : Objects.requireNonNull(placeholder.getConditionNodes())) {

            gui.addItem(ItemBuilder.from(Objects.requireNonNull(XMaterial.ITEM_FRAME.parseItem()))
                    .name(Component.text("§8§l» §7" + node.getRequirement().getType().name()))
                    .lore(Component.text("§8Type: §7" + node.getRequirement().getType().name()),
                            Component.text("§8Text: §7" + node.getText()))
                    .asGuiItem());

        }

        return gui;
    }

}