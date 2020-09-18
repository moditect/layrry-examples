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
import javafx.scene.Node;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractTileProvider implements TileProvider {
    private static final String TILE_ID = "TILE_ID";

    @Override
    public void register(TileContext context) {
        Optional<Node> existingTile = context.getTileContainer().getChildren()
            .stream()
            .filter(this::idMatches)
            .findAny();

        if (existingTile.isEmpty()) {
            Tile tile = createTile(context);
            tile.getProperties().put(TILE_ID, getTileId());
            context.getTileContainer().getChildren().add(tile);
        }
    }

    @Override
    public void unregister(TileContext context) {
        context.getTileContainer().getChildren().removeIf(this::idMatches);
    }

    protected abstract Tile createTile(TileContext context);

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
