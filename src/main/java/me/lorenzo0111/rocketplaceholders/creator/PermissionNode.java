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

import java.util.Objects;

/**
 * Permission node that contains permission conditions
 */
public class PermissionNode {
    private final String permission;
    private final String text;

    /**
     * @param permission Permission to view the text
     * @param text Text that can be seen if the player has the permission
     */
    public PermissionNode(String permission, String text) {
        this.permission = permission;
        this.text = text;
    }

    /**
     * @return Text that can be seen if the player has the permission
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return Permission to view the text
     */
    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;
        PermissionNode that = (PermissionNode) target;
        return Objects.equals(permission, that.permission) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission, text);
    }

    @Override
    public String toString() {
        return "PermissionNode{" +
                "permission='" + this.permission + '\'' +
                ", text='" + this.text + '\'' +
                '}';
    }

}
