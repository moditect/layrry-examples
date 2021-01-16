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
package org.kordamp.tiles.core;

import org.kordamp.tiles.model.PluginRegistry;
import org.kordamp.tiles.model.TileContext;
import org.kordamp.tiles.model.TilePlugin;
import org.moditect.layrry.platform.PluginDescriptor;
import org.moditect.layrry.platform.PluginLifecycleListener;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;

public class TilePluginLifecycleListener implements PluginLifecycleListener {
    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        // Load all plugins
        ServiceLoader<TilePlugin> loader = ServiceLoader.load(layer, TilePlugin.class);

        // collect and filter plugins by _this_ layer
        Collection<TilePlugin> plugins = new LinkedHashSet<>();
        loader.forEach(tilePlugin -> {
            if (tilePlugin.getClass().getModule().getLayer() == layer) {
                plugins.add(tilePlugin);
            }
        });

        // register plugins of _this_ layer
        PluginRegistry.getInstance()
            .registerPlugins(layer, plugins);
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // unregister plugins of _this_ layer
        PluginRegistry.getInstance()
            .unregisterPlugins(plugin.getModuleLayer());
    }
}
