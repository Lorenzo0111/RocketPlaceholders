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
import me.lorenzo0111.rocketplaceholders.utilities.JavaScriptParser;
import org.bukkit.entity.Player;

import javax.script.ScriptException;

public class JSCondition extends Requirement {
    private final transient JavaScriptParser<Boolean> engine;
    private final String expression;

    public JSCondition(String expression) {
        engine = new JavaScriptParser<>();

        this.expression = expression;
        this.getDatabaseInfo().put("value",expression);
    }

    @Override
    public boolean apply(Player player) {
        try {
            engine.bind("Player",player);
            Boolean result = engine.parse(expression);
            if (result == null) {
                plugin.getLogger().severe("Expression '" + expression + "' has to return a boolean. Returning as false..");
                return false;
            }

            return result;
        } catch (ScriptException e) {
            plugin.getLogger().info("Error while parsing javascript expression '" + expression + "'. If you want to see the error set debug to true in the config.");
            plugin.debug(e.getMessage());
            plugin.getLogger().info("Returning as false..");
        }

        return false;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.JAVASCRIPT;
    }
}
