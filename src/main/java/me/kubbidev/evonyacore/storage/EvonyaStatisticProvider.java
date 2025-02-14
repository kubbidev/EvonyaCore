package me.kubbidev.evonyacore.storage;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.EvonyaStatistic;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class EvonyaStatisticProvider {

    private final EPlayer player;
    private final File file;

    public EvonyaStatisticProvider(EPlayer player) {
        this.player = player;

        this.loadDir();
        this.file = new File(EvonyaPlugin.INSTANCE.getDataFolder() + File.separator + "statistic" + File.separator + player.getUniqueId() + ".yml");
    }

    private void loadDir() {
        final File dir = new File(EvonyaPlugin.INSTANCE.getDataFolder() + File.separator + "statistic");
        if (!dir.exists())
            dir.mkdir();
    }

    public void loadStatistic() {
        if (!PlayerManager.hasEvonyaStatistic(player))
            this.loadFromFile();
    }

    public void saveStatistic() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EvonyaStatistic evonyaStatistic = player.getEvonyaStatistic();
        try {
            yaml.set("username", player.getUsername());
            yaml.set("kills", evonyaStatistic.getKills());
            yaml.set("deaths", evonyaStatistic.getDeaths());
            yaml.set("games", evonyaStatistic.getGames());
            yaml.set("wins", evonyaStatistic.getWins());
            yaml.set("dmgInflicted", evonyaStatistic.getDmgInflicted());
            yaml.set("dmgReceived", evonyaStatistic.getDmgReceived());
            yaml.set("timePlayed", evonyaStatistic.getTimePlayed());

            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromFile() {
        final YamlConfiguration yaml;

        if (!this.file.exists())
            yaml = setupYaml();
        else yaml = getYaml();

        final EvonyaStatistic evonyaStatistic = new EvonyaStatistic(player);

        evonyaStatistic.setKills(yaml.getInt("kills"));
        evonyaStatistic.setDeaths(yaml.getInt("deaths"));
        evonyaStatistic.setGames(yaml.getInt("games"));
        evonyaStatistic.setWins(yaml.getInt("wins"));
        evonyaStatistic.setDmgInflicted(yaml.getDouble("dmgInflicted"));
        evonyaStatistic.setDmgReceived(yaml.getDouble("dmgReceived"));
        evonyaStatistic.setTimePlayed(yaml.getLong("timePlayed"));

        PlayerManager.getEvonyaStatistics().add(evonyaStatistic);
    }

    private YamlConfiguration setupYaml() {
        final YamlConfiguration yaml;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final EvonyaStatistic evonyaStatistic = new EvonyaStatistic(player);
            yaml = YamlConfiguration.loadConfiguration(file);

            yaml.addDefault("uuid", evonyaStatistic.getUuid());
            yaml.addDefault("username", evonyaStatistic.getUsername());
            yaml.addDefault("kills", evonyaStatistic.getKills());
            yaml.addDefault("deaths", evonyaStatistic.getDeaths());
            yaml.addDefault("games", evonyaStatistic.getGames());
            yaml.addDefault("wins", evonyaStatistic.getWins());
            yaml.addDefault("dmgInflicted", evonyaStatistic.getDmgInflicted());
            yaml.addDefault("dmgReceived", evonyaStatistic.getDmgReceived());
            yaml.addDefault("timePlayed", evonyaStatistic.getTimePlayed());

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
