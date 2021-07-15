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

package me.lorenzo0111.rocketplaceholders.conversation;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Conversation extends StringPrompt {
    private final Player author;
    private RocketPlaceholders plugin;
    private final String reason;

    public Conversation(String reason, Player author) {
        this.author = author;
        this.reason = reason;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {
        return ChatColor.translateAlternateColorCodes('&', "&7" + reason + " &7or '&ecancel&7' to cancel.");
    }

    public abstract void handle(@Nullable String input);

    @Nullable
    @Override
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        this.handle(input);

        return Prompt.END_OF_CONVERSATION;
    }

    public RocketPlaceholders getPlugin() {
        return plugin;
    }

    public void setPlugin(RocketPlaceholders plugin) {
        this.plugin = plugin;
    }

    public Player getAuthor() {
        return author;
    }
}
