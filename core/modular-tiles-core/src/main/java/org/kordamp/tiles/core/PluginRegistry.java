package org.kordamp.tiles.core;

import org.kordamp.tiles.model.TilePlugin;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {
    private static final PluginRegistry INSTANCE = new PluginRegistry();
    private final Map<ModuleLayer, Set<TilePlugin>> plugins = new ConcurrentHashMap<>();

    public void registerPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        Set<TilePlugin> set = plugins.computeIfAbsent(key, k -> new LinkedHashSet<>());

        set.add(plugin);
    }

    public boolean isPluginRegistered(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        Set<TilePlugin> set = plugins.computeIfAbsent(key, k -> new LinkedHashSet<>());

        return set.contains(plugin);
    }

    public void unregisterPlugin(TilePlugin plugin) {
        ModuleLayer key = plugin.getClass().getModule().getLayer();

        Set<TilePlugin> set = plugins.computeIfAbsent(key, k -> new LinkedHashSet<>());

        set.remove(plugin);

        if (set.isEmpty()) {
            plugins.remove(key);
        }
    }

    public Set<TilePlugin> getPlugins(ModuleLayer key) {
        return Collections.unmodifiableSet(plugins.computeIfAbsent(key, k -> new LinkedHashSet<>()));
    }

    public void clearPlugins(ModuleLayer key) {
        plugins.remove(key);
    }

    public static PluginRegistry getInstance() {
        return INSTANCE;
    }
}
