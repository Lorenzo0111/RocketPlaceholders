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

package me.lorenzo0111.rocketplaceholders.storage.user;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.api.ITextStorage;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VolatileUserStorage implements ITextStorage<UUID> {
    protected final List<UserText> storage = new ArrayList<>();

    @Override
    public void setText(Placeholder placeholder, UUID owner, @Nullable String text) {
        UserText user = storage.stream()
                .filter(userText -> userText.getUser().equals(owner) && userText.getPlaceholder().equals(placeholder.getIdentifier()))
                .findFirst()
                .orElse(new UserText(placeholder.getIdentifier(), owner));

        storage.remove(user);

        if (text != null) {
            user.setText(text);
            storage.add(user);
        }

        RocketPlaceholders.getInstance().debug("Created new UserText: " + user);
    }

    @Override
    public @Nullable String getText(Placeholder placeholder, UUID owner) {
        return storage.stream()
                .filter(userText -> userText.getUser().equals(owner) && userText.getPlaceholder().equals(placeholder.getIdentifier()))
                .map(UserText::getText)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean persistent() {
        return false;
    }

}
