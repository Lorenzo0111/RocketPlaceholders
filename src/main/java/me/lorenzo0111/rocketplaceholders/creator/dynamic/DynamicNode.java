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

import me.lorenzo0111.rocketplaceholders.creator.Node;

import java.util.concurrent.Callable;

/**
 * A dynamic node is a node with a text that change
 */
public abstract class DynamicNode extends Node implements DynamicObject {
    private Callable<String> callable;

    /**
     * @param condition Condition to view the text
     * @param text      Callable of the text that can be seen if the player respects the condition
     */
    public DynamicNode(Object condition, Callable<String> text) {
        super(condition, DynamicObject.FAKE_TEXT);
        this.setCallable(text);
    }

    /**
     * @return The the dynamic text
     */
    @Override
    public String getText() {
        try {
            return getDynamicText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
