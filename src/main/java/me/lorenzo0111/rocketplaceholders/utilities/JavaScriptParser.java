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

package me.lorenzo0111.rocketplaceholders.utilities;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JavaScript Parser
 */
public class JavaScriptParser<T> {
    private static ScriptEngineManager engine;
    private final Map<String,Object> bindings;

    public JavaScriptParser() {
        this.bindings = new HashMap<>();
        boolean exists = new ScriptEngineManager().getEngineByName("JavaScript") != null;

        if (exists) {
            engine = new ScriptEngineManager();
            return;
        }

        if (engine != null) {
            return;
        }

        ServicesManager manager = Bukkit.getServicesManager();
        if (manager.isProvidedFor(ScriptEngineManager.class)) {
            RegisteredServiceProvider<ScriptEngineManager> provider = manager.getRegistration(ScriptEngineManager.class);
            Objects.requireNonNull(provider);
            engine = provider.getProvider();
            RocketPlaceholders.getInstance()
                    .getLogger()
                    .info("Hooked into " + provider.getPlugin().getName() + " for JavaScript support.");
            return;
        }

        JavaPlugin plugin = RocketPlaceholders.getInstance();
        plugin.getLogger().severe("JavaScript engine not found. Consider installing JShader from the same author of this plugin.");
        plugin.getLogger().severe("Download it from: https://git.io/Jnq1M");
        plugin.getLogger().severe("JavaScript expression will cause errors, do not report it before trying the shader.");
    }

    /**
     * Parse a js expression with the parser
     * @param str expression
     * @return expression result
     * @throws ScriptException if something went wrong
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public T parse(String str) throws ScriptException {
        RocketPlaceholders.getInstance().debug("Parsing expression: " + str);

        this.bind("Server", Bukkit.getServer());
        this.applyBinding(engine);

        T result = (T) engine.getEngineByName("JavaScript").eval(str);

        this.bindings.clear();
        engine.getBindings().clear();

        RocketPlaceholders.getInstance().debug("Expression returned: " + result);
        return result;
    }

    public void bind(String key, Object value) {
        this.bindings.put(key,value);
    }

    private void applyBinding(final ScriptEngineManager engine) {
        bindings.forEach(engine::put);
    }

}
