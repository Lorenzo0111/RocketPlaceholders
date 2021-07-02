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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class VaultHook {
    private boolean economySetup = false;
    private Economy economy;

    private boolean permissionSetup = false;
    private Permission permission;

    private final JavaPlugin plugin;

    private boolean vault = false;

    public VaultHook(JavaPlugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault"))
            vault = true;
    }

    public @Nullable Economy economy() {
        if (!vault)
            return null;

        if (economy == null && !economySetup) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.economy = rsp.getProvider();
                this.plugin.getLogger().info("Hooked with Vault Economy.");
                economySetup = true;
            }
        }

        return this.economy;
    }

    public Permission permissions() {
        if (!vault)
            return null;

        if (permission == null && !permissionSetup) {
            RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
            if (rsp != null) {
                this.permission = rsp.getProvider();
                this.plugin.getLogger().info("Hooked with Vault Permission Manager.");
                permissionSetup = true;
            }
        }

        return this.permission;
    }

    public boolean hooked() {
        return vault;
    }
}
