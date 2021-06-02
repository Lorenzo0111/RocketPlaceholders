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

package me.lorenzo0111.rocketplaceholders.creator.dynamic;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * A placeholder with a dynamic text that is updated when called
 */
public class DynamicPlaceholder extends Placeholder implements DynamicObject {
    private Callable<String> callable;

    /**
     * @param key Configuration key. Set to null if you are using the api.
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main callable text of the placeholder
     * @param nodes ConditionNodes of the placeholder
     */
    public DynamicPlaceholder(@Nullable String key, @NotNull String identifier, JavaPlugin owner, @NotNull Callable<String> text, @Nullable List<ConditionNode> nodes) {
        super(key, identifier, owner, DynamicObject.FAKE_TEXT, nodes);
        this.setCallable(text);
    }

    /**
     * @param key Configuration key. Set to null if you are using the api.
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main callable text of the placeholder
     * @param nodes ConditionNodes of the placeholder
     * @param parseJS Should parse JavaScript expression
     */
    public DynamicPlaceholder(@Nullable String key, @NotNull String identifier, JavaPlugin owner, @NotNull Callable<String> text, @Nullable List<ConditionNode> nodes, boolean parseJS) {
        super(key, identifier, owner, DynamicObject.FAKE_TEXT, nodes, parseJS);
        this.setCallable(text);
    }

    /**
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Callable text of the placeholder
     *
     * @deprecated  Replaced with {@link DynamicPlaceholder#DynamicPlaceholder(String, String, JavaPlugin, Callable, List)}
     */
    @Deprecated
    public DynamicPlaceholder(@NotNull String identifier, JavaPlugin owner, @NotNull Callable<String> text) {
        super(identifier, owner, DynamicObject.FAKE_TEXT);
        this.setCallable(text);
    }

    /**
     * @return The the dynamic text
     */
    @Override
    public String getText() {
        try {
            return this.getDynamicText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Parse javascript
     * @param text Text to parse
     * @return Parsed text
     */
    @Override
    public String parseJS(String text) {
        return super.parseJS(text);
    }

    /**
     * @param callable Callable of the dynamic placeholder
     */
    @Override
    public void setCallable(Callable<String> callable) {
        this.callable = callable;
    }

    /**
     * @return Text of the callable
     * @throws Exception If something does not work
     */
    @Override
    public String getDynamicText() throws Exception {
        return callable.call();
    }
}
