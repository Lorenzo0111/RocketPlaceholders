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

package me.lorenzo0111.rocketplaceholders.api.utils;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderBuilder to build a placeholder
 */
@SuppressWarnings("unused")
public class PlaceholderBuilder {
    private final String identifier;
    private final String text;
    private final JavaPlugin owner;
    private final List<ConditionNode> nodes = new ArrayList<>();

    /**
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main text of the placeholder
     * @see me.lorenzo0111.rocketplaceholders.creator.Placeholder
     */
    public PlaceholderBuilder(String identifier, JavaPlugin owner, String text) {
        this.identifier = identifier;
        this.text = text;
        this.owner = owner;
    }

    /**
     * @param requirement Requirement to view the text
     * @param text Text of the condition
     * @see me.lorenzo0111.rocketplaceholders.creator.conditions.engine.Requirements
     * @return The same instance
     */
    public PlaceholderBuilder createConditionNode(Requirement requirement, String text) {
        nodes.add(new ConditionNode(requirement, text));

        return this;
    }


    /**
     * @return Built placeholder
     */
    public Placeholder build() {
        return new Placeholder(null, identifier, owner, text, nodes);
    }
}
