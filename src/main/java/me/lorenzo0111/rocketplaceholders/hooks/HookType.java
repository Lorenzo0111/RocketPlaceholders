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

package me.lorenzo0111.rocketplaceholders.hooks;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum HookType {
    PLACEHOLDERAPI("PlaceholderAPI", "me.clip.placeholderapi.PlaceholderAPI"),
    MVDW("MVdWPlaceholderAPI", ""),
    NULL("null", null);

    private final String plugin;
    private final String provider;
    private Method method;

    HookType(String plugin, @Nullable String provider) {
        this.plugin = plugin;
        this.provider = provider;

        this.loadMethod();
    }

    private void loadMethod() {
        if (provider == null) return;

        try {
            Class<?> aClass = Class.forName(provider);
            Method method;

            switch (this) {
                case PLACEHOLDERAPI:
                    method = aClass.getMethod("setPlaceholders", OfflinePlayer.class, String.class);
                    break;
                case MVDW:
                    method = aClass.getMethod("replacePlaceholders", OfflinePlayer.class, String.class);
                    break;
                default:
                    method = null;
                    break;
            }

            if (method == null) return;

            this.method = method;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public @NotNull String parse(OfflinePlayer player, String string) {
        if (provider == null) return string;

        try {
            Object invoke = method.invoke(null, player, string);

            if (!(invoke instanceof String)) {
                return string;
            }

            return (String) invoke;
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            return string;
        }
    }

    public String getPlugin() {
        return plugin;
    }
}
