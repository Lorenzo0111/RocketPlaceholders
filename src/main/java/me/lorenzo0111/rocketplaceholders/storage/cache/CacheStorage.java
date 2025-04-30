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

package me.lorenzo0111.rocketplaceholders.storage.cache;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.storage.user.VolatileUserStorage;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheStorage extends VolatileUserStorage {
    private final Map<UUID, Map<String, Long>> lastUpdate = new HashMap<>();

    @Override
    public void setText(Placeholder placeholder, UUID owner, @Nullable String text) {
        if (placeholder.getSettings().cacheDuration() <= 0)
            return;

        super.setText(placeholder, owner, text);

        if (text == null) return;
        lastUpdate.computeIfAbsent(owner, k -> new HashMap<>()).put(placeholder.getIdentifier(), System.currentTimeMillis());
    }

    @Override
    public @Nullable String getText(Placeholder placeholder, UUID owner) {
        long cacheDuration = placeholder.getSettings().cacheDuration();
        if (cacheDuration <= 0) return null;

        Map<String, Long> userCache = lastUpdate.get(owner);
        if (userCache == null || !userCache.containsKey(placeholder.getIdentifier())) return null;

        long lastUpdateTime = userCache.get(placeholder.getIdentifier());
        if (lastUpdateTime + cacheDuration > System.currentTimeMillis()) {
            return super.getText(placeholder, owner);
        }

        userCache.remove(placeholder.getIdentifier());
        super.setText(placeholder, owner, null);

        RocketPlaceholders.getInstance().debug("Cache expired for " + placeholder.getIdentifier() + " of " + owner);

        return null;
    }
}
