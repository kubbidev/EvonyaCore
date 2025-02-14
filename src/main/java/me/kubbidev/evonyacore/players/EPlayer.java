package me.kubbidev.evonyacore.players;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.exceptions.EvonyaStatisticDoesNotExistException;
import me.kubbidev.evonyacore.game.GameManager;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.utils.EvonyaSounds;
import me.kubbidev.nexuspowered.protocol.Protocol;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EPlayer {

    private EvonyaStatistic statistic;

    //ACCOUNT
    private final UUID uniqueId;
    private final String username;
    private Rank rank;

    private int hosts;
    private int preWL;
    private boolean privateMessage;

    private UUID lastPrivateMessage;

    private State state;
    private boolean bypass;

    public EPlayer(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.rank = Rank.JOUEUR;

        this.hosts = 0;
        this.preWL = 0;
        this.privateMessage = true;

        this.lastPrivateMessage = null;

        this.state = State.LOBBY;
        this.bypass = false;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public Rank getPlayerRank() {
        return rank;
    }

    public void setPlayerRank(Rank rank) {
        this.rank = rank;
    }

    public int getHosts() {
        return hosts;
    }

    public void setHosts(int hosts) {
        this.hosts = hosts;
    }

    public int getPreWL() {
        return preWL;
    }

    public void setPreWL(int preWL) {
        this.preWL = preWL;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(boolean privateMessage) {
        this.privateMessage = privateMessage;
    }

    public UUID getLastPrivateMessage() {
        return lastPrivateMessage;
    }

    public boolean hasLastPrivateMessage() {
        return (lastPrivateMessage != null);
    }

    public void setLastPrivateMessage(UUID lastPrivateMessage) {
        this.lastPrivateMessage = lastPrivateMessage;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    // -----| Game Methods |----------------------------------------------------------------------------------------------------------------

    public GameInstance getGameInstance() {
        return GameManager.getGameInstance(this);
    }

    public boolean hasGameInstance() {
        return GameManager.hasGameInstance(this);
    }

    public boolean isPlaying() {
        return (state == State.DEMONSLAYER_PLAYING);
    }

    public List<EPlayer> getWorldPlayers() {
        return getWorld().getPlayers().stream().map(PlayerManager::wrapPlayer).collect(Collectors.toList());
    }

    public boolean isHost() {
        return getGameInstance().getCoHost().contains(this);
    }
    
    public void refresh() {
        getInventory().clear();
        setLevel(0);
        setExp(0);

        setAllowFlight(false);
        setHealth(20.0D);
        setExhaustion(20.0F);
        setFlySpeed(0.1F);
        setWalkSpeed(0.2F);
        setFireTicks(0);

        getPlayer().getActivePotionEffects().forEach(potion -> getPlayer().removePotionEffect(potion.getType()));
        getInventory().setArmorContents(null);
        updateInventory();

        setBypass(false);
    }

    // -----| Utils Methods |----------------------------------------------------------------------------------------------------------------

    public Player getPlayer() {
        final Player player = Bukkit.getPlayer(uniqueId);

        if (player == null)
            throw new RuntimeException("Cannot get player with uuid : " + uniqueId);
        return player;
    }

    public EvonyaStatistic getEvonyaStatistic() {
        try {
            statistic.getUsername();
        } catch (Exception e) {
            try {
                statistic = PlayerManager.wrapEvonyaStatistic(this);
            } catch (Exception exception) {
                try {
                    statistic = PlayerManager.wrapEvonyaStatistic(uniqueId);
                } catch (EvonyaStatisticDoesNotExistException ex) {
                    throw new RuntimeException(ex);
                }
                EvonyaPlugin.LOGGER.severe("Could not obtain EvonyaStatistic via EvonyaBukkitPlayer! Trying UUID...");
            }
        }
        return statistic;
    }

    // -----| Bukkit Methods |----------------------------------------------------------------------------------------------------------------

    public boolean isOnline() {
        try {
            getPlayer();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void setPlayerListHeaderFooter(String header, String footer) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        container.getChatComponents()
                .write(0, WrappedChatComponent.fromText(header))
                .write(1, WrappedChatComponent.fromText(footer));

        Protocol.sendPacket(getPlayer(), container);
    }

    public void playSound(EvonyaSounds evonyaSounds) {
        getPlayer().playSound(getLocation(), evonyaSounds.getSound(), evonyaSounds.getVolume(), evonyaSounds.getPitch());
    }

    public void setExp(float v) {
        getPlayer().setExp(v);
    }

    public void setLevel(int i) {
        getPlayer().setLevel(i);
    }

    public boolean getAllowFlight() {
        return getPlayer().getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        getPlayer().setAllowFlight(b);
    }

    public boolean isFlying() {
        return getPlayer().isFlying();
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        getPlayer().setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        getPlayer().setWalkSpeed(v);
    }

    public void setExhaustion(float v) {
        getPlayer().setExhaustion(v);
    }

    public void setFireTicks(int v) {
        getPlayer().setFireTicks(v);
    }

    public void setHealth(double v) {
        getPlayer().setHealth(v);
    }

    public double getHealth() {
        return getPlayer().getHealth();
    }

    public void sendTitle(String s, String s1) {
        getPlayer().sendTitle(Color.translate(s), Color.translate(s1));
    }

    public void resetTitle() {
        getPlayer().resetTitle();
    }

    public void sendMessage(String s) {
        getPlayer().sendMessage(Color.translate(s));
    }

    public PlayerInventory getInventory() {
        return getPlayer().getInventory();
    }

    public void updateInventory() {
        getPlayer().updateInventory();
    }

    public void closeInventory() {
        getPlayer().closeInventory();
    }

    public World getWorld() {
        return getPlayer().getWorld();
    }

    public void teleport(Location loc) {
        getPlayer().teleport(loc);
    }

    public void openInventory(Inventory inventory) {
        getPlayer().openInventory(inventory);
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public void setGameMode(GameMode mode) {
        getPlayer().setGameMode(mode);
    }

    private static final Class<?> CRAFT_PLAYER
            = MinecraftReflection.getCraftPlayerClass();

    private static final Class<?> ENTITY_PLAYER
            = MinecraftReflection.getEntityPlayerClass();

    private static final MethodAccessor GET_HANDLE
            = Accessors.getMethodAccessor(CRAFT_PLAYER, "getHandle");

    private static final FieldAccessor PING
            = Accessors.getFieldAccessorOrNull(ENTITY_PLAYER, "ping", int.class);

    public int getPing() {
        return (int) PING.get(GET_HANDLE.invoke(getPlayer()));
    }

    public void setScoreboard(Scoreboard scoreboard) {
        getPlayer().setScoreboard(scoreboard);
    }

    public void performCommand(String command) {
        getPlayer().performCommand(command);
    }
}