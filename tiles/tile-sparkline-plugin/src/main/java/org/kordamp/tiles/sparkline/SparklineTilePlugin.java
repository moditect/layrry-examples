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
package org.kordamp.tiles.sparkline;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.paint.Stop;
import org.kordamp.tiles.model.AbstractTilePlugin;
import org.kordamp.tiles.model.TileContext;

import java.util.Random;

public class SparklineTilePlugin extends AbstractTilePlugin {
    @Override
    protected Tile createTile(TileContext context) {
        return TileBuilder.create()
            .skinType(Tile.SkinType.SPARK_LINE)
            .prefSize(context.getTileWidth(), context.getTileHeight())
            .title("SparkLine Tile")
            .unit("mb")
            .gradientStops(new Stop(0, Tile.GREEN),
                new Stop(0.5, Tile.YELLOW),
                new Stop(1.0, Tile.RED))
            .strokeWithGradient(true)
            .build();
    }

    @Override
    public void handleHeartbeat(long now, Random random) {
        getTile().setValue(random.nextDouble() * getTile().getRange() * 1.5 + getTile().getMinValue());
    }
}
