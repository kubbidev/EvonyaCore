package me.kubbidev.evonyacore.utils;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.ConstructorAccessor;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.ForwardingMultimap;
import me.kubbidev.nexuspowered.item.ItemStackBuilder;
import me.kubbidev.nexuspowered.util.Text;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Head {
    private final String name;

    ItemStackBuilder
    private List<String> lore;
    private HeadType headType;
    private OfflinePlayer owner;
    private int amount;

    /**
     * Create a new 1.18 head
     *
     * @param name
     * Head name
     */
    public Head(String name) {
        this.name = name;

        this.headType = null;
        this.lore = new ArrayList<>();
        this.amount = 1;
        this.owner = null;
    }

    /**
     * Set head lore
     *
     * @param lore
     * Head lore
     */
    public Head setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Set head type
     *
     * @param headType
     * Head type
     */
    public Head setHeadType(HeadType headType) {
        this.headType = headType;
        return this;
    }

    /**
     * Set head owner
     *
     * @param owner
     * Head owner
     */
    public Head setOwner(OfflinePlayer owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Set head amount
     *
     * @param amount
     * Head amount
     */
    public Head setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    private static final Class<?> GAME_PROFILE = MinecraftReflection.getGameProfileClass();

    private static final ConstructorAccessor CREATE_UUID_STRING = Accessors.getConstructorAccessorOrNull(
            GAME_PROFILE, UUID.class, String.class);

    private static final MethodAccessor GET_PROPERTIES = Accessors.getMethodAccessorOrNull(
            GAME_PROFILE, "getProperties");

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ItemStack parseItem() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, this.amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        Object profile = CREATE_UUID_STRING.invoke(UUID.randomUUID(), null);
        if (this.headType != null) {
            ForwardingMultimap properties = (ForwardingMultimap) GET_PROPERTIES.invoke(profile);
            properties.put("textures", new WrappedSignedProperty("textures", this.headType.getTexture(), null).getHandle());
        }
        else if (this.owner != null) {
            profile = WrappedGameProfile.fromOfflinePlayer(this.owner);
        }

        FieldAccessor profileField = Accessors.getFieldAccessorOrNull(skullMeta.getClass(), "profile", GAME_PROFILE);
        profileField.set(skullMeta, profile);
        skullMeta.setDisplayName(Text.colorize(this.name));

        if (this.lore != null) {
            skullMeta.setLore(this.lore.stream().map(Text::colorize).collect(Collectors.toList()));
        }

        skull.setItemMeta(skullMeta);
        return skull;
    }
}
