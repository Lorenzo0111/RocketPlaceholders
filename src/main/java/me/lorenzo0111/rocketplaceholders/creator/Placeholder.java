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

package me.lorenzo0111.rocketplaceholders.creator;

import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.InvalidConditionException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder
 */
public class Placeholder {
    private boolean parseJS = false;
    private final String identifier;
    private final String key;
    private final String text;
    private List<ConditionNode> conditionNodes;
    private final JavaPlugin owner;
    private ScriptEngine engine;

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;
        Placeholder that = (Placeholder) target;
        return Objects.equals(identifier, that.identifier) && Objects.equals(text, that.text) && Objects.equals(conditionNodes, that.conditionNodes) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, text, conditionNodes, owner);
    }

    @Override
    public String toString() {
        return "Placeholder{" +
                "identifier='" + identifier + '\'' +
                ", text='" + text + '\'' +
                ", conditionNodes=" + conditionNodes +
                ", owner=" + owner +
                '}';
    }

    /**
     * @param key Configuration key. Set to null if you are using the api.
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main text of the placeholder
     * @param nodes ConditionNodes of the placeholder
     */
    public Placeholder(@Nullable String key, @NotNull String identifier, JavaPlugin owner, @NotNull String text,@Nullable List<ConditionNode> nodes) {
        this.identifier = identifier;
        this.key = key;

        if (text.contains("%rp_")) {
            this.text = ChatColor.translateAlternateColorCodes('&', "&cError! You can't use rp placeholders in the text.");
        } else {
            this.text = ChatColor.translateAlternateColorCodes('&', text);
        }

        this.conditionNodes = nodes;

        this.owner = owner;
    }

    /**
     * @param key Configuration key. Set to null if you are using the api.
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main text of the placeholder
     * @param nodes ConditionNodes of the placeholder
     * @param parseJS Should parse JavaScript expression
     */
    public Placeholder(@Nullable String key, @NotNull String identifier, JavaPlugin owner, @NotNull String text,@Nullable List<ConditionNode> nodes, boolean parseJS) {
        this(key,identifier,owner,text,nodes);

        this.parseJS = parseJS;

        if (parseJS) {
            this.engine = new ScriptEngineManager().getEngineByName("javascript");
            this.engine.put("Server", Bukkit.getServer());
        }
    }

    /**
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Text of the placeholder
     *
     * @deprecated  Replaced with {@link Placeholder#Placeholder(String, String, JavaPlugin, String, List)}
     */
    @Deprecated
    public Placeholder(@NotNull String identifier, JavaPlugin owner, @NotNull String text) {
        this.identifier = identifier;
        this.key = null;

        if (text.contains("%rp_")) {
            this.text = ChatColor.translateAlternateColorCodes('&', "&cError! You can't use rp placeholders in the text.");
        } else {
            this.text = ChatColor.translateAlternateColorCodes('&', text);
        }
        this.owner = owner;
    }


    /**
     * @return Identifier of the placeholder
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return Text of the placeholder
     */
    public String getText() {
        return this.text;
    }

    /**
     * Parse javascript
     * @param text Text to parse
     * @return Parsed text
     */
    public String parseJS(String text) {
        if (!this.parseJS) {
            return text;
        }

        try {
            Object result = engine.eval(text);
            if (!(result instanceof String)) {
                throw new InvalidConditionException("The expression should return a string.");
            }

            return (String) result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return "JavaScript error";
    }

    /**
     * @return Plugin that created the placeholder
     */
    public JavaPlugin getOwner() {
        return owner;
    }

    /**
     * @return All condition nodes or null
     */
    @Nullable
    public List<ConditionNode> getConditionNodes() {
        return this.conditionNodes;
    }

    /**
     * @return True if the plugin has any condition node
     */
    public boolean hasConditionNodes() {
        return this.conditionNodes != null;
    }

    /**
     * @return Configuration key if present.
     */
    @Nullable
    public String getKey() {
        return key;
    }

    public boolean hasKey() {
        return key != null;
    }
}
