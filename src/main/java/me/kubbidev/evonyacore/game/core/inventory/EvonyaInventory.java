package me.kubbidev.evonyacore.game.core.inventory;

public enum EvonyaInventory {

    DEFAULT_ITEM(DefaultItems.class);

    private final Class<? extends CustomInventory> customInventory;

    EvonyaInventory(Class<? extends CustomInventory> customInventory) {
        this.customInventory = customInventory;
    }

    public Class<? extends CustomInventory> getCustomInventory() {
        return customInventory;
    }
}
