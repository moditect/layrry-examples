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

import eu.hansolo.tilesfx.Tile;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractTilePlugin implements TilePlugin, TileContext.HeartbeatListener {
    private static final String TILE_ID = "TILE_ID";
    private Tile tile;

    @Override
    public final CompletionStage<TilePlugin> register(TileContext context) {
        final CompletableFuture<TilePlugin> promise = new CompletableFuture<>();

        try {
            Platform.runLater(() -> {
                try {
                    createAndAddToContainer(context);
                    promise.complete(this);
                } catch (Throwable t) {
                    promise.completeExceptionally(t);
                }
            });
        } catch (IllegalStateException ise) {
            // plugin failed to initialize
            promise.completeExceptionally(ise);
        }

        return promise;
    }

    @Override
    public final CompletionStage<TilePlugin> unregister(TileContext context) {
        final CompletableFuture<TilePlugin> promise = new CompletableFuture<>();

        try {
            Platform.runLater(() -> {
                try {
                    removeFromContainer(context);
                    promise.complete(this);
                } catch (Throwable t) {
                    promise.completeExceptionally(t);
                }
            });
        } catch (IllegalStateException iae) {
            // plugin failed to initialize
            promise.completeExceptionally(iae);
        }

        return promise;
    }

    @Override
    public String getId() {
        return getClass().getName();
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
        tile.getProperties().put(TILE_ID, getId());
        context.getTileContainer().getChildren().add(tile);
        context.addHeartbeatListener(this);
        tile.setRunning(true);
    }

    private void removeFromContainer(TileContext context) {
        context.removeHeartbeatListener(this);
        context.getTileContainer().getChildren().removeIf(this::idMatches);
        tile.setRunning(false);
        tile.stop();
    }

    protected abstract Tile createTile(TileContext context);

    private boolean idMatches(final Node node) {
        return idMatcher(getId()).apply(node);
    }

    private Function<Node, Boolean> idMatcher(final String id) {
        return node -> id.equals(node.getProperties().get(TILE_ID));
    }
}
