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
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class HasMoneyCondition extends Requirement {
    private final long money;

    public HasMoneyCondition(long money) {
        this.money = money;
        this.getDatabaseInfo().put("value",String.valueOf(money));
    }

    @Override
    public boolean apply(Player player) {
        if (!plugin.getLoader().getVault().hooked()) {
            return false;
        }

        Economy economy = plugin.getLoader().getVault().economy();

        return economy != null && economy.has(player, money);
    }

    public static HasMoneyCondition create(String value) {
        if (value == null) {
            throw new InvalidConditionException("Value cannot be null. Please insert a valid number as 'value' in the config.");
        }

        try {
            long amount = Long.parseLong(value);
            if (amount < 0) {
                throw new InvalidConditionException("Value cannot be negative. Please insert a valid number as 'value' in the config.");
            }

            return new HasMoneyCondition(amount);
        } catch (NumberFormatException e) {
            throw new InvalidConditionException("Value is not a valid number. Please insert a valid number as 'value' in the config.");
        }
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MONEY;
    }
}
