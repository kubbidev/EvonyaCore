package me.kubbidev.evonyacore.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

    private final int amount;

    private Location location;
    private Color[] colors;

    public Fireworks(int amount) {
        this.amount = amount;
    }

    public Fireworks setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Fireworks setColors(Color... colors) {
        this.colors = colors;
        return this;
    }

    public void spawnFireworks() {
        final Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(1);
        fwm.addEffect(FireworkEffect.builder().withColor(colors).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            final Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}
