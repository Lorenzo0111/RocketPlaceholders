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

import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.RequirementType;
import org.bukkit.entity.Player;

public class HasPermissionCondition extends Requirement {
    private final String permission;

    public HasPermissionCondition(String permission) {
        this.permission = permission;
        this.getDatabaseInfo().put("value",permission);
    }

    @Override
    public boolean apply(Player player) {
        return player.hasPermission(permission);
    }

    @Override
    public RequirementType getType() {
        return RequirementType.PERMISSION;
    }

    public static HasPermissionCondition create(String value) {
        return new HasPermissionCondition(value);
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + getType() + "," +
                "permission='" + permission + "'" +
                "}";
    }
}
