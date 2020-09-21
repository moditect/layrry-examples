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
import org.kordamp.tiles.model.TilePlugin;
import org.moditect.layrry.platform.PluginDescriptor;
import org.moditect.layrry.platform.PluginLifecycleListener;

import java.util.ServiceLoader;
import java.util.Set;

public class TilePluginLifecycleListener implements PluginLifecycleListener {
    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        ServiceLoader<TilePlugin> plugins = ServiceLoader.load(layer, TilePlugin.class);
        plugins.forEach(tilePlugin -> {
            if (tilePlugin.getClass().getModule().getLayer() == layer) {
                try {
                    Platform.runLater(() -> {
                        tilePlugin.register(TileContext.getInstance());
                        PluginRegistry.getInstance().registerPlugin(tilePlugin);
                    });
                } catch (IllegalStateException ignored) {
                    // JavaFX Toolkit may not have been initialized if plugin is loaded during boot time
                }
            }
        });
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        ModuleLayer layer = plugin.getModuleLayer();

        Set<TilePlugin> plugins = PluginRegistry.getInstance().getPlugins(layer);
        plugins.forEach(tilePlugin -> {
            try {
                Platform.runLater(() -> {
                    tilePlugin.unregister(TileContext.getInstance());
                    PluginRegistry.getInstance().unregisterPlugin(tilePlugin);
                });
            } catch (IllegalStateException iae) {
                // something series happened, deal with it
                iae.printStackTrace();
            }
        });

        PluginRegistry.getInstance().clearPlugins(layer);
    }
}
