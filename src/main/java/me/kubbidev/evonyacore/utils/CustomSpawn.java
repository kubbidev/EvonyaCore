package me.kubbidev.evonyacore.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class CustomSpawn {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;

    public CustomSpawn(double x, double y, double z, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z, yaw, 0);
    }
}
