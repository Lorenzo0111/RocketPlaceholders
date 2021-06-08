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

package me.lorenzo0111.rocketplaceholders.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebPanelHandler {
    private final RocketPlaceholders plugin;
    private final URL url;

    public WebPanelHandler(RocketPlaceholders plugin) throws MalformedURLException {
        this.plugin = plugin;
        this.url = new URL("https://editor.rp.rocketplugins.space");
    }

    @NotNull
    public String generate() {
        Gson gson = new Gson();
        Storage storage = plugin.getStorageManager().getInternalPlaceholders();
        return gson.toJson(storage.getMap().values());
    }

    @NotNull
    private String format(String json) {
        JsonObject object = new JsonObject();
        object.addProperty("content", json);
        return object.getAsString();
    }

    @Nullable
    public String save() throws IOException {

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestProperty("User-Agent", plugin.toString());
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = this.format(this.generate()).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        }

    }

    public RocketPlaceholders getPlugin() {
        return plugin;
    }
}
