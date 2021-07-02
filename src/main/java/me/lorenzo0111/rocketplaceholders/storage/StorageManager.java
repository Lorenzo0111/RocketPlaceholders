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

package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    private final Storage internalPlaceholders;
    private final Storage externalPlaceholders;

    public StorageManager() {
        this.internalPlaceholders = new Storage();
        this.externalPlaceholders = new Storage();
    }

    public Storage getInternalPlaceholders() {
        return this.internalPlaceholders;
    }

    public Storage getExternalPlaceholders() {
        return this.externalPlaceholders;
    }

    @Nullable
    public Placeholder get(String identifier) {
        if (this.internalPlaceholders.getMap().containsKey(identifier)) {
            return this.internalPlaceholders.get(identifier);
        }

        return this.externalPlaceholders.get(identifier);
    }

    public Map<String, Placeholder> getAll() {
        final Map<String, Placeholder> all = new HashMap<>();

        all.putAll(this.internalPlaceholders.getMap());
        all.putAll(this.externalPlaceholders.getMap());

        return all;
    }
}
