package me.kubbidev.evonyacore.storage;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class EvonyaPlayerProvider {

    private final Player player;
    private final File file;

    public EvonyaPlayerProvider(Player player) {
        this.player = player;

        this.loadDir();
        this.file = new File(EvonyaPlugin.INSTANCE.getDataFolder() + File.separator + "player" + File.separator + player.getUniqueId() + ".yml");
    }

    private void loadDir() {
        final File dir = new File(EvonyaPlugin.INSTANCE.getDataFolder() + File.separator + "player");
        if (!dir.exists())
            dir.mkdir();
    }

    public EvonyaPlayer loadAccount() {
        if (!PlayerManager.hasEvonyaPlayer(player))
            return this.loadFromFile();
        else return PlayerManager.wrapEvonyaPlayer(player);
    }

    public EvonyaPlayer saveAccount() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EvonyaPlayer evonyaPlayer = PlayerManager.wrapEvonyaPlayer(player);
        try {
            yaml.set("username", player.getName());
            yaml.set("playerRank", evonyaPlayer.getPlayerRank().getValue());
            yaml.set("hosts", evonyaPlayer.getHosts());
            yaml.set("pwl", evonyaPlayer.getPreWL());
            yaml.set("privateMessage", evonyaPlayer.isPrivateMessage());

            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return evonyaPlayer;
    }

    private EvonyaPlayer loadFromFile() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EvonyaPlayer evonyaPlayer = new EvonyaPlayer(player.getUniqueId(), player.getName());
        final Rank rank = Rank.getRankByValue(yaml.getInt("playerRank"));

        evonyaPlayer.setPlayerRank(rank);
        evonyaPlayer.setHosts(yaml.getInt("hosts"));
        evonyaPlayer.setPreWL(yaml.getInt("pwl"));
        evonyaPlayer.setPrivateMessage(yaml.getBoolean("privateMessage"));

        PlayerManager.getEvonyaPlayers().add(evonyaPlayer);

        return evonyaPlayer;
    }

    private YamlConfiguration setupYaml() {
        final YamlConfiguration yaml;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final EvonyaPlayer evonyaPlayer = new EvonyaPlayer(player.getUniqueId(), player.getName());
            yaml = YamlConfiguration.loadConfiguration(file);

            yaml.addDefault("uuid", evonyaPlayer.getUniqueId());
            yaml.addDefault("username", evonyaPlayer.getUsername());
            yaml.addDefault("playerRank", evonyaPlayer.getPlayerRank().getValue());
            yaml.addDefault("hosts", evonyaPlayer.getHosts());
            yaml.addDefault("pwl", evonyaPlayer.getPreWL());
            yaml.addDefault("privateMessage", evonyaPlayer.isPrivateMessage());

            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return yaml;
    }

    public YamlConfiguration getYaml() {
        return YamlConfiguration.loadConfiguration(this.file);
    }
}
