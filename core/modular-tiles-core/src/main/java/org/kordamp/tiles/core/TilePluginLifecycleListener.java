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

import org.kordamp.tiles.model.PluginRegistry;
import org.kordamp.tiles.model.TileContext;
import org.kordamp.tiles.model.TilePlugin;
import org.moditect.layrry.platform.PluginDescriptor;
import org.moditect.layrry.platform.PluginLifecycleListener;

import java.util.ServiceLoader;

public class TilePluginLifecycleListener implements PluginLifecycleListener {
    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        ServiceLoader<TilePlugin> plugins = ServiceLoader.load(layer, TilePlugin.class);
        plugins.forEach(tilePlugin -> {
            if (tilePlugin.getClass().getModule().getLayer() == layer) {
                tilePlugin.register(TileContext.getInstance());
            }
        });
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        PluginRegistry.getInstance().clearPlugins(layer, TileContext.getInstance());
    }
}
