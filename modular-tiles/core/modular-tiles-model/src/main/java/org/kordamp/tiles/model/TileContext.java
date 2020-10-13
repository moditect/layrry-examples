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

import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.animation.AnimationTimer;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TileContext {
    private static final Random RND = new Random();

    private static TileContext instance;
    private final int tileWidth;
    private final int tileHeight;
    private final FlowGridPane tileContainer;
    private final Set<HeartbeatListener> listeners = new CopyOnWriteArraySet<>();
    private final Heartbeat heartbeat;

    private TileContext(int tileWidth, int tileHeight, FlowGridPane tileContainer) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileContainer = tileContainer;
        this.heartbeat = new Heartbeat();
        this.heartbeat.start();
    }

    public void stop() {
        heartbeat.stop();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public FlowGridPane getTileContainer() {
        return tileContainer;
    }

    public void addHeartbeatListener(HeartbeatListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeHeartbeatListener(HeartbeatListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public static TileContext getInstance() {
        return instance;
    }

    public static TileContext of(int tileWidth, int tileHeight, FlowGridPane tileContainer) {
        if (null == instance) {
            instance = new TileContext(tileWidth, tileHeight, tileContainer);
        }
        return instance;
    }

    public interface HeartbeatListener {
        void handleHeartbeat(long now, Random random);
    }

    private class Heartbeat extends AnimationTimer {
        private long lastTimerCall = System.nanoTime();

        @Override
        public void handle(long now) {
            if (now > lastTimerCall + 3_500_000_000L) {
                listeners.forEach(listener -> listener.handleHeartbeat(now, RND));
                lastTimerCall = now;
            }
        }
    }
}
