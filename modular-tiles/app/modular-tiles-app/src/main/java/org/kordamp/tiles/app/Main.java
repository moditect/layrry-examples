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
package org.kordamp.tiles.app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.tiles.core.View;
import org.kordamp.tiles.model.PluginRegistry;
import org.kordamp.tiles.model.TileContext;

public class Main extends Application {
    private static final int TILE_WIDTH = 150;
    private static final int TILE_HEIGHT = 150;

    @Override
    public void start(Stage stage) {
        View view = new View(TILE_WIDTH, TILE_HEIGHT);

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(10);

        Scene scene = new Scene(view.getContent());
        scene.setCamera(camera);

        PluginRegistry.getInstance().initializeDeferredPlugins();

        stage.setTitle("Modular TilesFX");
        stage.setScene(scene);
        stage.setMinWidth((TILE_WIDTH * 3) + 20);
        stage.setMinHeight(TILE_HEIGHT + 30);
        stage.show();
    }

    @Override
    public void stop() {
        TileContext.getInstance().stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
