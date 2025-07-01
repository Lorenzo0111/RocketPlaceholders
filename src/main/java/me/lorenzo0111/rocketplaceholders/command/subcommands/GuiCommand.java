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

package me.lorenzo0111.rocketplaceholders.command.subcommands;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.lorenzo0111.rocketplaceholders.command.RocketPlaceholdersCommand;
import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import me.lorenzo0111.rocketplaceholders.conversation.ConversationUtil;
import me.lorenzo0111.rocketplaceholders.conversation.conversations.TextConversation;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.utilities.GuiUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class GuiCommand extends SubCommand {

    public GuiCommand(RocketPlaceholdersCommand command) {
        super(command);
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        final String prefix = this.getCommand().getPlugin().getConfig().getString("prefix");
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&r &cThis command can be performed only from a player."));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&r &7Loading the GUI, this may take some seconds.."));

        final Player player = (Player) sender;
        final PaginatedGui gui = GuiUtils.createGui("Placeholders List");
        GuiUtils.setPageItems(gui);

        List<Placeholder> placeholders = this.command
                .getPlugin()
                .getStorageManager()
                .getAll()
                .values()
                .stream()
                .filter(Placeholder::hasKey)
                .collect(Collectors.toList());

        for (Placeholder placeholder : placeholders) {
            gui.addItem(ItemBuilder.from(Material.BOOK)
                    .setName(String.format("§8§l» §7%s", placeholder.getIdentifier()))
                    .setLore("§7Click to edit this placeholder")
                    .asGuiItem(e -> {
                        PaginatedGui settingsGui = GuiUtils.createGui(placeholder.getIdentifier() + " &8&l» &7Settings");

                        settingsGui.setItem(2, ItemBuilder.from(Material.TORCH)
                                .setName("§8§l» §7Information")
                                .setLore("§8Identifier: §7" + placeholder.getIdentifier(),
                                        "§8Text: §7" + placeholder.getText(),
                                        placeholder.hasConditionNodes() ?
                                                "§8Conditions: §7" + Objects.requireNonNull(placeholder.getConditionNodes()).size() :
                                                "Component.empty()",
                                        "§7§oClick to edit the text.")
                                .asGuiItem(event -> {
                                    event.setCancelled(true);
                                    gui.close(event.getWhoClicked());
                                    ConversationUtil.createConversation(this.getCommand().getPlugin(), new TextConversation((Player) event.getWhoClicked(), placeholder));
                                }));

                        Material material = placeholder.hasConditionNodes() ? Material.CHEST : Material.BARRIER;

                        Objects.requireNonNull(material);

                        settingsGui.setItem(6, ItemBuilder.from(material)
                                .setName("§8§l» §7Conditions")
                                .setLore(placeholder.hasConditionNodes() ? "§7Click to view" : "§7There isn't any condition.")
                                .asGuiItem(event -> {
                                    event.setCancelled(true);

                                    if (placeholder.hasConditionNodes()) {
                                        GuiUtils.createConditionsGui(placeholder).open(player);
                                    }
                                }));

                        settingsGui.setItem(22, ItemBuilder.from(Material.ARROW)
                                .setName("§8§l» §7Back")
                                .asGuiItem(event -> {
                                    event.setCancelled(true);
                                    event.getWhoClicked().closeInventory();
                                    gui.open(event.getWhoClicked());
                                }));

                        settingsGui.open(player);

                    }));
        }

        gui.open(player);

    }
}
