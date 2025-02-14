package me.kubbidev.evonyacore.players;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public enum Role {

    TANJIRO("TANJIRO", Group.SLAYERS, Material.BLAZE_ROD),
    ZENITSU("ZEN'ITSU", Group.SLAYERS, Material.GLOWSTONE_DUST),
    INOSUKE("INOSUKE", Group.SLAYERS, Material.GRILLED_PORK),
    TOMIOKA("TOMIOKA", Group.SLAYERS, Material.WATER_BUCKET),
    SHINOBU("SHINOBU", Group.SLAYERS, Material.RED_ROSE),
    KYOJURO("KYOJURO", Group.SLAYERS, Material.FLINT_AND_STEEL),
    TENGEN("TENGEN", Group.SLAYERS, Material.DIAMOND_PICKAXE),
    MUICHIRO("MUICHIRO", Group.SLAYERS, Material.CLAY_BALL),
    MITSURI("MITSURI", Group.SLAYERS, Material.QUARTZ_ORE),
    SANEMI("SANEMI", Group.SLAYERS, Material.SUGAR),
    OBANAI("OBANAI", Group.SLAYERS, Material.POTION),
    GYOMEI("GYOMEI", Group.SLAYERS, Material.STONE_AXE),
    KANAO("KANAO", Group.SLAYERS, Material.DIAMOND_BOOTS),
    //13

    MUZAN("MUZAN", Group.DEMONS, Material.REDSTONE),
    KOKUSHIBO("KOKUSHIBO", Group.DEMONS, Material.WATCH),
    DOMA("DOMA", Group.DEMONS, Material.SNOW_BALL),
    AKAZA("AKAZA", Group.DEMONS, Material.APPLE),
    DAKI("DAKI", Group.DEMONS, Material.IRON_BARDING),
    GYUTARO("GYUTARO", Group.DEMONS, Material.DIAMOND_HOE),
    GYOKKO("GYOKKO", Group.DEMONS, Material.FLOWER_POT),
    NAKIME("NAKIME", Group.DEMONS, Material.MAGMA_CREAM),
    KAIGAKU("KAIGAKU", Group.DEMONS, Material.YELLOW_FLOWER),
    RUI("RUI", Group.DEMONS, Material.WEB),
    ENMU("ENMU", Group.DEMONS, Material.SPIDER_EYE),
    HANTENGU("HANTENGU", Group.DEMONS, Material.RABBIT_FOOT),
    NEZUKO("NEZUKO", Group.DEMONS, Material.FERMENTED_SPIDER_EYE);
    //13


    private final String name;
    private final Group group;
    private final Material material;

    public static final List<Role> VALUES;

    static {
        VALUES = new ArrayList<>();
        VALUES.addAll(Arrays.asList(values()));
    }

    Role(String name, Group group, Material material) {
        this.name = name;
        this.group = group;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public boolean isDemon() {
        return (group == Group.DEMONS);
    }

    public ChatColor getColor() {
        return isDemon() ? ChatColor.RED : ChatColor.GREEN;
    }

    public String getPrefix() {
        return getColor() + getName();
    }

    public Material getMaterial() {
        return material;
    }

    enum Group {

        SLAYERS,
        DEMONS
    }
}
