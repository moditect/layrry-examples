/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.tiles.model;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {
    private static final PluginRegistry INSTANCE = new PluginRegistry();
    private final Map<ModuleLayer, Set<TilePlugin>> plugins = new ConcurrentHashMap<>();
    private final Set<TilePlugin> deferredPlugins = new LinkedHashSet<>();

    void registerDeferredPlugin(TilePlugin plugin) {
        deferredPlugins.add(plugin);
    }

    void registerPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        Set<TilePlugin> set = plugins.computeIfAbsent(key, k -> new LinkedHashSet<>());

        set.add(plugin);
    }

    void unregisterPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        Set<TilePlugin> set = plugins.computeIfAbsent(key, k -> new LinkedHashSet<>());

        set.remove(plugin);

        if (set.isEmpty()) {
            plugins.remove(key);
        }
    }

    public void clearPlugins(ModuleLayer key, TileContext context) {
        if (plugins.containsKey(key)) {
            plugins.get(key).forEach(tilePlugin -> {
                tilePlugin.unregister(context);
            });
            plugins.remove(key);
        }
    }

    public void initializeDeferredPlugins(TileContext context) {
        Set<TilePlugin> toBeInitialized = new LinkedHashSet<>(deferredPlugins);
        toBeInitialized.forEach(tilePlugin -> {
            deferredPlugins.remove(tilePlugin);
            tilePlugin.register(context);
        });

        // TODO: check if any deferred plugins failed initialization
        // Must check with an event inside Platform.runLater()
        // Post results on another thread
    }

    public static PluginRegistry getInstance() {
        return INSTANCE;
    }
}
