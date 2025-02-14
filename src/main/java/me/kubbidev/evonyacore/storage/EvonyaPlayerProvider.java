package me.kubbidev.evonyacore.storage;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.EPlayer;
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

    public EPlayer loadAccount() {
        if (!PlayerManager.hasEvonyaPlayer(player))
            return this.loadFromFile();
        else return PlayerManager.wrapPlayer(player);
    }

    public EPlayer saveAccount() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EPlayer ePlayer = PlayerManager.wrapPlayer(player);
        try {
            yaml.set("username", player.getName());
            yaml.set("playerRank", ePlayer.getPlayerRank().getValue());
            yaml.set("hosts", ePlayer.getHosts());
            yaml.set("pwl", ePlayer.getPreWL());
            yaml.set("privateMessage", ePlayer.isPrivateMessage());

            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ePlayer;
    }

    private EPlayer loadFromFile() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EPlayer ePlayer = new EPlayer(player.getUniqueId(), player.getName());
        final Rank rank = Rank.getRankByValue(yaml.getInt("playerRank"));

        ePlayer.setPlayerRank(rank);
        ePlayer.setHosts(yaml.getInt("hosts"));
        ePlayer.setPreWL(yaml.getInt("pwl"));
        ePlayer.setPrivateMessage(yaml.getBoolean("privateMessage"));

        PlayerManager.getEvonyaPlayers().add(ePlayer);

        return ePlayer;
    }

    private YamlConfiguration setupYaml() {
        final YamlConfiguration yaml;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final EPlayer ePlayer = new EPlayer(player.getUniqueId(), player.getName());
            yaml = YamlConfiguration.loadConfiguration(file);

            yaml.addDefault("uuid", ePlayer.getUniqueId());
            yaml.addDefault("username", ePlayer.getUsername());
            yaml.addDefault("playerRank", ePlayer.getPlayerRank().getValue());
            yaml.addDefault("hosts", ePlayer.getHosts());
            yaml.addDefault("pwl", ePlayer.getPreWL());
            yaml.addDefault("privateMessage", ePlayer.isPrivateMessage());

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
