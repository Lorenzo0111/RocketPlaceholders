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

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserText implements ConfigurationSerializable {
    private final String identifier;
    private final UUID user;
    private String text;

    public UserText(String identifier, UUID user) {
        this.identifier = identifier;
        this.user = user;
    }

    @SuppressWarnings("unused")
    public UserText(Map<String,Object> data) {
        this.identifier = data.get("placeholder").toString();
        this.user = UUID.fromString(data.get("user").toString());

        if (data.containsKey("text")) {
            this.text = data.get("text").toString();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceholder() {
        return identifier;
    }

    public UUID getUser() {
        return user;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("placeholder", identifier);
        map.put("user", user.toString());
        map.put("text", text);

        return map;
    }

    @Override
    public String toString() {
        return "{" +
                "identifier=" + identifier +
                ", user=" + user +
                ", text='" + text + '\'' +
                '}';
    }
}
