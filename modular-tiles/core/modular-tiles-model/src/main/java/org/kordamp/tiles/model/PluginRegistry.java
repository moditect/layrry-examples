/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {
    private static final PluginRegistry INSTANCE = new PluginRegistry();
    private final Map<ModuleLayer, Map<String, TilePlugin>> plugins = new ConcurrentHashMap<>();
    private final Set<TilePlugin> deferredPlugins = new LinkedHashSet<>();

    public void registerPlugins(ModuleLayer key, Collection<TilePlugin> plugins) {
        TileContext context = TileContext.getInstance();

        Map<String, TilePlugin> pluginMap = this.plugins.computeIfAbsent(key, k -> new LinkedHashMap<>());

        plugins.forEach(tilePlugin -> {
            if (context == null) {
                // JavaFX Toolkit has not been initialized yet -> tilePlugin is loaded during boot time
                registerDeferredPlugin(tilePlugin);
                return;
            }

            if (!pluginMap.containsKey(tilePlugin.getId())) {
                handlePluginRegistration(tilePlugin, context);
            }
        });
    }

    public void unregisterPlugins(ModuleLayer key) {
        TileContext context = TileContext.getInstance();

        if (plugins.containsKey(key)) {
            plugins.get(key).values().forEach(tilePlugin -> tilePlugin.unregister(context)
                .thenAccept(this::untrackPlugin));
            plugins.remove(key);
        }
    }

    public void initializeDeferredPlugins() {
        TileContext context = TileContext.getInstance();

        // defensive copying to allow removal on the spot
        new LinkedHashSet<>(deferredPlugins).forEach(tilePlugin -> {
            deferredPlugins.remove(tilePlugin);
            handlePluginRegistration(tilePlugin, context);
        });
    }

    private void registerDeferredPlugin(TilePlugin plugin) {
        deferredPlugins.add(plugin);
    }

    private void trackPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        plugins.computeIfAbsent(key, k -> new LinkedHashMap<>())
            .put(plugin.getId(), plugin);
    }

    private void untrackPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        plugins.computeIfAbsent(key, k -> new LinkedHashMap<>())
            .remove(plugin.getId());
    }

    private void handlePluginRegistration(TilePlugin tilePlugin, TileContext context) {
        tilePlugin.register(context)
            .thenAccept(this::trackPlugin)
            .exceptionally(throwable -> {
                // TODO: something went terribly wrong
                throwable.printStackTrace();
                return null;
            });
    }

    public static PluginRegistry getInstance() {
        return INSTANCE;
    }
}
