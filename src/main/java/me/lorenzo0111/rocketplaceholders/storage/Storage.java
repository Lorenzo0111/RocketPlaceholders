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

package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private final Map<String, Placeholder> placeholders = new HashMap<>();
    private final JavaPlugin plugin;

    public Storage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void add(@NotNull String identifier, @NotNull Placeholder placeholder) {
        this.placeholders.put(identifier, placeholder);
    }

    public void build(@NotNull String identifier, @NotNull String text) {
        this.placeholders.put(identifier, new Placeholder(identifier, plugin, text));
    }

    public void build(@NotNull String identifier, @NotNull String text, List<PermissionNode> permissionNodes) {
        this.placeholders.put(identifier, new Placeholder(identifier, plugin, text, permissionNodes));
    }

    public void clear() {
        this.placeholders.clear();
    }

    @Nullable
    public Placeholder get(String identifier) {
        return this.placeholders.get(identifier);
    }

    public Map<String, Placeholder> getMap() {
        return this.placeholders;
    }
}
