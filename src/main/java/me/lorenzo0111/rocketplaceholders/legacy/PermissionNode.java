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

package me.lorenzo0111.rocketplaceholders.legacy;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.types.HasPermissionCondition;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <b>Warning:</b> This class will be removed in the next version.
 * Please use {@link me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode} instead
 */
@Deprecated
public class PermissionNode extends ConditionNode {

    public PermissionNode(RocketPlaceholders plugin, String permission, String text) {
        super(new HasPermissionCondition(plugin,permission), text);
    }

    public static List<PermissionNode> createPermissionNodes(RocketPlaceholders plugin, ConfigurationSection section) {
        final List<PermissionNode> nodes = new ArrayList<>();
        for (String nodeKey : section.getKeys(false)) {
            if (section.getString(nodeKey + ".permission") != null && section.getString(nodeKey + ".text") != null) {
                final PermissionNode node = new PermissionNode(plugin,section.getString(nodeKey + ".permission"), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(section.getString(nodeKey + ".text"))));
                nodes.add(node);
            }
        }

        return nodes;
    }

}