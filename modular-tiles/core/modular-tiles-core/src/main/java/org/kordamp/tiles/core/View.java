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

import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.kordamp.tiles.model.TileContext;

public class View {
    private final FlowGridPane tileContainer;

    public View(int tileWidth, int tileHeight) {
        tileContainer = new FlowGridPane(3,1);
        TileContext.of(tileWidth, tileHeight, tileContainer);

        tileContainer.setHgap(5);
        tileContainer.setVgap(5);
        tileContainer.setAlignment(Pos.CENTER);
        tileContainer.setCenterShape(true);
        tileContainer.setPadding(new Insets(5));
        tileContainer.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public Node getContent() {
        return tileContainer;
    }
}
