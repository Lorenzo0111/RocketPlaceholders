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
import me.lorenzo0111.rocketplaceholders.providers.ProviderUtils;
import org.bukkit.entity.Player;

public class TextCondition extends Requirement {
    private final String one;
    private final String two;

    public TextCondition(String one, String two) {
        this.one = one;
        this.two = two;

        this.getDatabaseInfo().put("one", one);
        this.getDatabaseInfo().put("two", two);
    }

    @Override
    public boolean apply(Player player) {
        String one = ProviderUtils.setPlaceholders(plugin,this.one,player);
        String two = ProviderUtils.setPlaceholders(plugin,this.two,player);

        return one.equals(two);
    }

    public static TextCondition create(String value) {
        if (value == null) {
            throw new InvalidConditionException("Value cannot be null. Please insert two valid strings as 'value' in the config.");
        }

        String[] strings = value.split("%%");
        if (strings.length != 2) {
            throw new InvalidConditionException("Invalid value. Please insert two valid strings as 'value' in the config with the format 'string1%%string2'.");
        }

        return new TextCondition(strings[0],strings[1]);
    }

    @Override
    public RequirementType getType() {
        return RequirementType.TEXT;
    }
}
