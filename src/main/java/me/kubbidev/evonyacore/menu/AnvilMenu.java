package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.EvonyaPlugin;
import net.wesjd.anvilgui.AnvilGUI;

public abstract class AnvilMenu {

    protected final AnvilGUI.Builder BUILDER;

    protected final PlayerMenuUtility playerMenuUtility;
    protected final EvonyaPlugin plugin;

    public AnvilMenu(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        this.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;

        this.BUILDER = new AnvilGUI.Builder();
    }

    public abstract void open();
}
