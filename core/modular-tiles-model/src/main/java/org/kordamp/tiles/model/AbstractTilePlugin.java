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

import eu.hansolo.tilesfx.Tile;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public abstract class AbstractTilePlugin implements TilePlugin, TileContext.HeartbeatListener {
    private static final String TILE_ID = "TILE_ID";
    private Tile tile;

    @Override
    public final void register(TileContext context) {
        if (context == null) {
            // JavaFX Toolkit has not been initialized yet -> plugin is loaded during boot time
            PluginRegistry.getInstance().registerDeferredPlugin(this);
            return;
        }

        Optional<Node> existingTile = context.getTileContainer().getChildren()
            .stream()
            .filter(this::idMatches)
            .findAny();

        if (existingTile.isEmpty()) {
            try {
                Platform.runLater(() -> {
                    createAndAddToContainer(context);
                    PluginRegistry.getInstance().registerPlugin(this);
                });
            } catch (IllegalStateException ise) {
                // plugin failed to initialize
                // TODO: mark it as failed and handle
                ise.printStackTrace();
            }
        }
    }

    @Override
    public final void unregister(TileContext context) {
        try {
            Platform.runLater(() -> {
                context.removeHeartbeatListener(this);
                context.getTileContainer().getChildren().removeIf(this::idMatches);
                PluginRegistry.getInstance().unregisterPlugin(this);
                cleanup();
            });
        } catch (IllegalStateException iae) {
            // something serious happened, deal with it somehow
            // FIXME: potential memory leak
            iae.printStackTrace();
        }
    }

    public final Tile getTile() {
        return tile;
    }

    @Override
    public void handleHeartbeat(long now, Random random) {
        // left for implementors
    }

    private void createAndAddToContainer(TileContext context) {
        tile = createTile(context);
        tile.getProperties().put(TILE_ID, getTileId());
        context.getTileContainer().getChildren().add(tile);
        context.addHeartbeatListener(this);
    }

    protected abstract Tile createTile(TileContext context);

    protected void cleanup() {
        // left for implementors
    }

    protected String getTileId() {
        return getClass().getName();
    }

    private boolean idMatches(final Node node) {
        return idMatcher(getTileId()).apply(node);
    }

    private Function<Node, Boolean> idMatcher(final String id) {
        return node -> id.equals(node.getProperties().get(TILE_ID));
    }
}
