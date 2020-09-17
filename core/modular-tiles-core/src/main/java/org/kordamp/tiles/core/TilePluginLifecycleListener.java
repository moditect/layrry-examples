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
package org.kordamp.tiles.core;

import javafx.application.Platform;
import org.kordamp.tiles.model.TileContext;
import org.kordamp.tiles.model.TileProvider;
import org.moditect.layrry.platform.PluginDescriptor;
import org.moditect.layrry.platform.PluginLifecycleListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TilePluginLifecycleListener implements PluginLifecycleListener {
    private final Map<ModuleLayer, Set<TileProvider>> providersByLayer = new ConcurrentHashMap<>();

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        Set<TileProvider> providers = new HashSet<>();

        ModuleLayer layer = plugin.getModuleLayer();

        ServiceLoader<TileProvider> services = ServiceLoader.load(layer, TileProvider.class);
        services.forEach(tileProvider -> {
            if (tileProvider.getClass().getModule().getLayer() == layer) {
                Platform.runLater(() -> tileProvider.register(TileContext.getInstance()));
                providers.add(tileProvider);
            }
        });

        providersByLayer.put(layer, providers);
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        Set<TileProvider> providers = providersByLayer.computeIfAbsent(layer, l -> Collections.emptySet());
        providers.forEach(provider -> Platform.runLater(() -> provider.unregister(TileContext.getInstance())));
        providersByLayer.remove(layer);
    }
}
