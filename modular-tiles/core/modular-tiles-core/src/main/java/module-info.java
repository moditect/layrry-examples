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

import org.kordamp.tiles.core.TilePluginLifecycleListener;
import org.kordamp.tiles.model.TilePlugin;
import org.moditect.layrry.platform.PluginLifecycleListener;

module org.kordamp.tiles.core {
    exports org.kordamp.tiles.core;

    requires org.kordamp.tiles.model;
    requires eu.hansolo.tilesfx;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.moditect.layrry.platform;

    uses TilePlugin;
    provides PluginLifecycleListener with TilePluginLifecycleListener;
}