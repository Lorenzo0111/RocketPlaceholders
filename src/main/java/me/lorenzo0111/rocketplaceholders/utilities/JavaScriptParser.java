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

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaScript Parser
 */
public class JavaScriptParser<T> {
    private final Map<String,Object> bindings;

    /**
     * @throws JavetException if something is wrong
     */
    public JavaScriptParser() throws JavetException {
        this.bindings = new HashMap<>();
    }

    /**
     * Parse a js expression with the parser
     * @param str expression
     * @return expression result
     * @throws JavetException if something went wrong
     */
    @Nullable
    public T parse(String str) throws JavetException {
        this.bind("Server", Bukkit.getServer());
        System.out.println(bindings);
        V8Runtime runtime = this.prepare();
        T result = runtime.getExecutor(str).executePrimitive();
        this.bindings.clear();
        runtime.close();
        return result;
    }

    private V8Runtime prepare() throws JavetException {
        final V8Runtime runtime = V8Host.getV8Instance().createV8Runtime();
        runtime.allowEval(true);
        this.applyBinding(runtime);
        return runtime;
    }

    public void bind(String key, Object value) {
        this.bindings.put(key,value);
    }

    private void applyBinding(final V8Runtime runtime) {
        bindings.forEach((k,v) -> {
            try {
                runtime.getGlobalObject().set(k,v);
            } catch (JavetException e) {
                e.printStackTrace();
            }
        });
    }

}
