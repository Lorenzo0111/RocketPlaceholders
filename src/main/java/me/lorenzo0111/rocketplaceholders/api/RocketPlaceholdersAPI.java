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

package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import org.jetbrains.annotations.Nullable;

/**
 * API of RocketPlaceholders
 */
@SuppressWarnings("unusde")
public interface RocketPlaceholdersAPI {

    /**
     * Add a placeholder via api
     * @param placeholder Placeholder to add
     */
    void addPlaceholder(Placeholder placeholder);

    /**
     * Get a placeholder from the storage
     * @param identifier Identifier of the placeholder
     * @return Placeholder that has that identifier or null
     */
    @Nullable
    Placeholder getPlaceholder(String identifier);

    /**
     * Get the placeholder manager
     * @return PlaceholdersManager
     */
    PlaceholdersManager getPlaceholdersManager();
}
