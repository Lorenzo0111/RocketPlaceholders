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

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.PlaceholderSettings;
import me.lorenzo0111.rocketplaceholders.utilities.JavaScriptParser;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder
 */
public class Placeholder {
    private final PlaceholderSettings settings;
    private final String identifier;
    private String text;
    private List<ConditionNode> conditionNodes;
    private transient final JavaPlugin owner;
    private transient JavaScriptParser<String> engine;

    /**
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Main text of the placeholder
     * @param nodes ConditionNodes of the placeholder
     * @param settings Placeholder settings
     */
    public Placeholder(@NotNull String identifier, JavaPlugin owner, @NotNull String text, @Nullable List<ConditionNode> nodes, @Nullable PlaceholderSettings settings) {
        this.identifier = identifier;

        this.settings = settings;

        if (settings != null && settings.parseJs())
            this.engine = new JavaScriptParser<>();

        if (text.contains("%rp_")) {
            this.text = ChatColor.translateAlternateColorCodes('&', "&cError! You can't use rp placeholders in the text.");
        } else {
            this.text = ChatColor.translateAlternateColorCodes('&', text);
        }

        this.conditionNodes = nodes;

        this.owner = owner;
    }

    /**
     * @param identifier Identifier of the placeholder
     * @param owner Plugin that created the placeholder
     * @param text Text of the placeholder
     *
     * @deprecated  Replaced with {@link Placeholder#Placeholder( String, JavaPlugin, String, List, PlaceholderSettings)}
     */
    @Deprecated
    public Placeholder(@NotNull String identifier, JavaPlugin owner, @NotNull String text) {
        this.identifier = identifier;
        this.settings = null;

        if (text.contains("%rp_")) {
            this.text = ChatColor.translateAlternateColorCodes('&', "&cError! You can't use rp placeholders in the text.");
        } else {
            this.text = ChatColor.translateAlternateColorCodes('&', text);
        }
        this.owner = owner;
    }

    /**
     * Parse javascript
     * @param text Text to parse
     * @return Parsed text
     */
    public String parseJS(String text) {
        if (this.settings == null || !this.settings.parseJs()) {
            return text;
        }

        try {
            String result = engine.parse(text);
            if (result == null) {
                throw new InvalidConditionException("The expression returned a null value.");
            }

            return result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return "JavaScript error";
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;
        Placeholder that = (Placeholder) target;
        return Objects.equals(identifier, that.identifier) && Objects.equals(text, that.text) && Objects.equals(conditionNodes, that.conditionNodes) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, text, conditionNodes, owner);
    }

    @Override
    public String toString() {
        return "Placeholder{" +
                "identifier='" + identifier + '\'' +
                ", text='" + text + '\'' +
                ", conditionNodes=" + conditionNodes +
                ", owner=" + owner +
                '}';
    }

    public boolean setText(String text) {
        this.text = text;

        if (this.settings.key() == null) return false;

        return this.edit("text", text);
    }

    /**
     * Edit a value from the file
     * @param path Path of the setting
     * @param value Value of the setting
     * @return true if success
     */
    public boolean edit(String path,Object value) {
        File file = this.getFile();
        FileConfiguration config = this.getConfig();

        try {
            config.set(path, value);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * @return The placeholder config
     */
    public FileConfiguration getConfig() {
        if (getFile() == null) return null;

        return YamlConfiguration.loadConfiguration(getFile());
    }

    /**
     * @return The placeholder file
     */
    public File getFile() {
        if (this.settings.key() == null) return null;

        File placeholdersDir = RocketPlaceholders.getInstance()
                .getPlaceholdersDir();

        File file = new File(placeholdersDir, this.settings.key());
        if (!file.exists()) return null;

        return file;
    }

    /**
     * @return Identifier of the placeholder
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return Text of the placeholder
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return Plugin that created the placeholder
     */
    public JavaPlugin getOwner() {
        return owner;
    }

    /**
     * @return All condition nodes or null
     */
    @Nullable
    public List<ConditionNode> getConditionNodes() {
        return this.conditionNodes;
    }

    /**
     * @return True if the plugin has any condition node
     */
    public boolean hasConditionNodes() {
        return this.conditionNodes != null;
    }

    /**
     * @return Placeholder settings
     */
    @Nullable
    public PlaceholderSettings getSettings() {
        return settings;
    }

    public boolean hasKey() {
        return settings != null && settings.key() != null;
    }
}
