package me.kubbidev.evonyacore;

import me.kubbidev.evonyacore.events.PlayerDisconnectEvent;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.utils.CustomSpawn;
import me.kubbidev.evonyacore.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LobbyFunction {

    private final World lobby;

    public LobbyFunction(){
            this.lobby = Bukkit.getWorld("HUB");
    }

    public World getLobby() {
        return lobby;
    }

    public void lobbyEnter(EvonyaPlayer player) {

        if (player.hasGameInstance())
            Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(player, player.getGameInstance()));

        final Random random = new Random();
        final int randomInt = random.ints(0, SpawnPoints.VALUES.size()).findFirst().orElse(0);

        player.teleport(SpawnPoints.VALUES.get(randomInt).getSpawn().getLocation(lobby));

        player.refresh();
        player.setGameMode(GameMode.ADVENTURE);
        player.setState(State.LOBBY);

        if (player.getPlayerRank().isHigherThan(Rank.JOUEUR)) {
            final String role = player.getPlayerRank().getScoreBoardPrefix();
            final String name = player.getUsername();

            lobby.getPlayers().stream().map(PlayerManager::wrapEvonyaPlayer).forEach(evonyaPlayer -> evonyaPlayer.sendMessage(role + name + "&r&o vient de rejoindre!"));
        }

        giveLobbyItems(player);

        Bukkit.getServer().getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> {
            welcomeMessage(player);
            sendTitle(player);
        }, 60L);
    }

    private void welcomeMessage(EvonyaPlayer player) {
        player.sendMessage(" ");
        player.sendMessage(" &f(&c!&f) Bienvenue sur &c&lEVONYA");
        player.sendMessage(" ");
        player.sendMessage("&8┃ &fNouveauté(s) et Modification(s) :");
        player.sendMessage("  &8•&f Aucune &cmodification(s) récente(s)&f...");
        player.sendMessage(" ");
        player.sendMessage("&8┃ &fMode de jeux :");
        player.sendMessage("  &8•&a ✔ &6&lDemon Slayer &f(V1)");
        player.sendMessage(" ");
        player.sendMessage("&8»&f En vous souhaitant une &abonne &cexpérience &fde &cjeu&f!");
        player.sendMessage(" ");
    }

    private void sendTitle(EvonyaPlayer player) {
        player.resetTitle();
        player.sendTitle("&f&l»&c&l»&f&l» &c&lEVONYA &f&l«&c&l«&f&l«", "&f&k|&c&k|&f&k| &fBienvenue &f&k|&c&k|&f&k|");
    }

    public void giveLobbyItems(EvonyaPlayer player) {
        player.getInventory().setItem(4, new Item(Material.COMPASS).setName("&8┃ &b&lNaviguation&f (Clic-Droit)").parseItem());
        player.updateInventory();
    }

    enum SpawnPoints {

        ONE(new CustomSpawn(17.5, 67, 0.5, 90)),
        TWO(new CustomSpawn(14.5, 67, 1.5, 90)),
        THREE(new CustomSpawn(14.5, 67, -0.5, 90)),
        FOUR(new CustomSpawn(16.5, 67, -2.5, 90)),
        FIVE(new CustomSpawn(18.5, 67, -2.5, 90)),
        SIX(new CustomSpawn(20.5, 67, -0.5, 90)),
        SEVEN(new CustomSpawn(20.5, 67, 1.5, 90)),
        EIGHT(new CustomSpawn(18.5, 67, 3.5, 90)),
        NINE(new CustomSpawn(16.5, 67, 3.5, 90));


        private final CustomSpawn customSpawn;

        public static final List<SpawnPoints> VALUES;

        static {
            VALUES = new ArrayList<>();
            VALUES.addAll(Arrays.asList(values()));
        }

        SpawnPoints(CustomSpawn customSpawn) {
            this.customSpawn = customSpawn;
        }

        public CustomSpawn getSpawn() {
            return customSpawn;
        }
    }
}
