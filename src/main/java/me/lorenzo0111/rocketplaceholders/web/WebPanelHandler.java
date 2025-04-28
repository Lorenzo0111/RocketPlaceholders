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
import com.google.gson.JsonSyntaxException;
import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.api.IWebPanelHandler;
import me.lorenzo0111.rocketplaceholders.api.WebEdit;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidResponseException;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class WebPanelHandler implements IWebPanelHandler {
    private final RocketPlaceholders plugin;
    private final URL add;
    private final String get;

    public WebPanelHandler(RocketPlaceholders plugin) throws MalformedURLException {
        this.plugin = plugin;
        this.add = new URL("https://editor.rocketplugins.space/new");
        this.get = "https://editor.rocketplugins.space/raw/";
    }

    @Override
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

    @Override
    @Nullable
    public String save() throws IOException {
        HttpsURLConnection connection = this.createConnection(add,"POST");

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

    @Override
    public @NotNull WebEdit load(String code) throws InvalidResponseException {
        try {
            URL url = new URL(get + code);
            HttpsURLConnection connection = this.createConnection(url,"GET");

            Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);

            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            reader.close();
            br.close();
            Gson serializer = new Gson();
            return serializer.fromJson(response.toString(), WebEdit.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new InvalidResponseException(e);
        }
    }

    @Override
    public void applyEdit(WebEdit edit) {
        plugin.getLoader().getFoliaLib().getScheduler().runAsync((task) -> {
            Storage storage = RocketPlaceholders.getApi().getPlaceholdersManager().getStorageManager().getInternalPlaceholders();

            List<String> remove = edit.getRemove();
            storage.getMap()
                    .entrySet()
                    .removeIf((entry) -> remove.contains(entry.getKey()) && entry.getValue().getFile().delete());

            Map<String, String> rename = edit.getRename();
            for (Map.Entry<String,String> entry : rename.entrySet()) {
                for (Placeholder placeholder : storage.getMap().values()) {
                    if (!placeholder.getIdentifier().equals(entry.getKey())) continue;

                    placeholder.edit("placeholder", entry.getValue());
                    RocketPlaceholders.getApi().getPlaceholdersManager()
                            .getConfigManager()
                            .reload(entry.getKey(), placeholder);
                    break;
                }
            }

            List<Placeholder> edits = edit.getEdited();
            for (Placeholder placeholder : edits) {
                if (storage.contains(placeholder.getIdentifier())) {
                    storage.getMap().remove(placeholder.getIdentifier());
                }

                try {
                    placeholder.serialize(new File(plugin.getPlaceholdersDir(), placeholder.getIdentifier().toLowerCase() + ".yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private @NotNull HttpsURLConnection createConnection(@NotNull URL url, String method) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestProperty("User-Agent", String.format("%s/%s (%s)", plugin.getName(),plugin.getDescription().getVersion(),getClass().getSimpleName()));
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestMethod(method);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        return connection;
    }

    public RocketPlaceholders getPlugin() {
        return plugin;
    }
}
