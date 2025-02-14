package me.kubbidev.evonyacore.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldManager implements Listener {

    private static final List<World> gameInstanceWorlds = new ArrayList<>();

    public void addWorld(World world) {
        gameInstanceWorlds.add(world);
    }

    public static World createNewWorld(int id) {
        final String worldName = "Arena-" + id;
        final File worldDir = new File("ARENA");

        if (worldDir.exists() && worldDir.isDirectory())
            FileUtils.recursiveCopy(worldDir, new File(worldName));

        final World world = loadWorld(worldName);
        gameInstanceWorlds.add(world);
        return world;
    }

    public static World loadWorld(String worldName) {
        final WorldCreator wc = new WorldCreator(worldName);

        wc.type(WorldType.FLAT);
        wc.generateStructures(false);

        return wc.createWorld();
    }

    public static void unloadWorld(World world) {
        Bukkit.unloadWorld(world, false);
    }

    public static void deleteWorld(World world) {
        final File worldDir = new File(world.getName());

        if (worldDir.exists())
            return;

        unloadWorld(world);
        FileUtils.deleteFile(new File(world.getName()));
    }

    public static void deleteAllWorlds() {
        gameInstanceWorlds.forEach(WorldManager::deleteWorld);
    }
}
