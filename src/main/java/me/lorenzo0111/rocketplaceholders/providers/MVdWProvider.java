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

package me.lorenzo0111.rocketplaceholders.providers;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class MVdWProvider extends Provider {
    public MVdWProvider(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager) {
        super(plugin,placeholdersManager);
        placeholdersManager.hook(this);
        this.reload();
    }

    public void reload() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin,() -> manager.getAll().forEach((s, placeholder) -> PlaceholderAPI.registerPlaceholder(plugin, String.format("rp_%s",s), (event) -> this.provide(event.getOfflinePlayer(),event.getPlaceholder()))));
    }

    @Override
    public String parse(Placeholder placeholder, OfflinePlayer player, String text) {
        return placeholder.parseJS(PlaceholderAPI.replacePlaceholders(player,text));
    }

}
