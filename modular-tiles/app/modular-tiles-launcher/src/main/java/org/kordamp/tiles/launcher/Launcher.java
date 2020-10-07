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
package org.kordamp.tiles.launcher;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.moditect.layrry.Layers;
import org.moditect.layrry.config.LayersConfig;
import org.moditect.layrry.config.toml.TomlLayersConfigParser;
import org.moditect.layrry.internal.LayersFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Launcher {
    public static void main(String... args) throws IOException {
        // 1. Detect OS
        String osName = System.getProperty("os.name").toLowerCase();

        // 2. Map OS to JavaFX classifier
        String classifier = "";
        if (osName.contains("windows")) {
            classifier = "win";
        } else if (osName.contains("mac")) {
            classifier = "mac";
        } else if (osName.contains("linux")) {
            classifier = "linux";
        } else {
            throw new IllegalStateException("JavaFX is not supported on " + System.getProperty("os.name"));
        }

        // 3. Load module versions
        Properties props = new Properties();
        props.load(Launcher.class.getClassLoader().getResourceAsStream("META-INF/versions.properties"));
        props.put("classifier", classifier);

        // 4. Load config template
        StringWriter layerConfig = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("META-INF/layers.toml");
        mustache.execute(layerConfig, props);
        layerConfig.flush();

        // 5. Create layers from config
        Path rootDir = Paths.get(".").toAbsolutePath();
        if (args != null && args.length > 0) {
            rootDir = Paths.get(args[0]).toAbsolutePath();
        }
        LayersConfig config = new TomlLayersConfigParser().parse(new ByteArrayInputStream(layerConfig.toString().getBytes()));
        Layers layers = new LayersFactory().createLayers(config, rootDir);

        // 6. Launch application
        layers.run(config.getMain().getModule() + "/" + config.getMain().getClazz());
    }
}
