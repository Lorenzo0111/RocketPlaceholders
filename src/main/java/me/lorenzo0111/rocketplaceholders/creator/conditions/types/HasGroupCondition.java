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

import com.google.common.base.Joiner;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.RequirementType;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HasGroupCondition extends Requirement {
    private final List<String> groups;

    public HasGroupCondition(List<String> groups) {
        this.groups = groups;
        this.getDatabaseInfo().put("value", Joiner.on("||").join(groups));
    }

    @Override
    public boolean apply(Player player) {
        if (!plugin.getLoader().getVault().hooked())
            return false;

        Permission permission = plugin.getLoader().getVault().permissions();

        boolean inGroup = true;

        for (String group : groups) {
            if (!permission.playerInGroup(player, group)) {
                inGroup = false;
                break;
            }
        }

        return permission != null && inGroup;
    }

    public static HasGroupCondition create(String value) throws InvalidConditionException {
        if (value == null) {
            throw new InvalidConditionException("Group cannot be null. Please try to set a valid permission as 'value' in the config.");
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            throw new InvalidConditionException("You cannot use this condition without Vault plugin.");
        }

        return new HasGroupCondition(Arrays.asList(value.split("\\|\\|")));
    }

    @Override
    public RequirementType getType() {
        return RequirementType.GROUP;
    }
}
