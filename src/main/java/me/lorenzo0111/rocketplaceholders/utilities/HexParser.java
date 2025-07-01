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

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexParser {
    private static final Pattern PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    private HexParser() {
    }

    public static @NotNull String text(@NotNull String text) {
        return parseHexString(text, PATTERN);
    }

    private static boolean serverSupportsHex() {
        try {
            ChatColor.of(Color.BLACK);
            return true;
        } catch (NoSuchMethodError ignore) {
            return false;
        }
    }

    private static String parseHexString(@NotNull String text, @NotNull Pattern hexPattern) {
        Matcher hexColorMatcher = hexPattern.matcher(text);

        if (serverSupportsHex()) {
            while (hexColorMatcher.find()) {
                String hex = hexColorMatcher.group(1);
                ChatColor color = ChatColor.of(hex);

                String before = text.substring(0, hexColorMatcher.start());
                String after = text.substring(hexColorMatcher.end());
                text = before + color + after;
                hexColorMatcher = hexPattern.matcher(text);
            }
        }

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
    }
}
