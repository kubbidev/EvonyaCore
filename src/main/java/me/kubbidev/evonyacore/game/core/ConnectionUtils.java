package me.kubbidev.evonyacore.game.core;

import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.Rank;
import me.kubbidev.evonyacore.players.State;
import me.kubbidev.evonyacore.utils.Item;
import org.bukkit.GameMode;
import org.bukkit.Material;

public class ConnectionUtils {

    private final GameInstance gameInstance;

    public ConnectionUtils(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    /**
     *
     * different methods to join the arena
     * public games methods and private for host
     *
     */
    public void connect(EPlayer player) {
        switch (gameInstance.getGameState()) {
            case STARTING:
            case WAITING:
                setPlayerArena(player);
                break;
            case ENDED:
            case STARTED:
                setPlayerSpectateArena(player);
                break;
        }
    }

    public void setPlayerArena(EPlayer player) {

        player.teleport(gameInstance.getSpawnLocation());
        player.refresh();
        player.setGameMode(GameMode.ADVENTURE);
        player.setState(State.DEMONSLAYER_LOBBY);

        player.getInventory().setItem(8, new Item(Material.DARK_OAK_DOOR).setName("&8┃ &c&lQuitter&f (Clic-Droit)").parseItem());
        player.updateInventory();

        player.sendMessage(" ");
        player.sendMessage("  &f(&c!&f) Vous avez rejoint le serveur de &c&l&n" + gameInstance.getHost().getUsername() + "&f.");
        player.sendMessage(" ");
        player.sendMessage("  &8•&r Jeu : &6Demon Slayer");
        player.sendMessage("  &8•&r Pour toutes questions appelez un " + Rank.MODERATEUR.getPrefix());
        player.sendMessage(" ");
        player.sendMessage("[&6NOUVEAU&f]  &eGestion des &lco-hosts&e customisable!");
        player.sendMessage(" ");

        if (player.isHost()) {
            if (gameInstance.getGameState() == GameState.WAITING) {
                player.getInventory().setItem(4, new Item(Material.NETHER_STAR).setName("&8┃ &b&lConfiguration&f (Clic-Droit)").parseItem());
                player.updateInventory();
            }
        }
        gameInstance.getPlayers().add(player);
        gameInstance.getPlayers().forEach(players -> players.sendMessage(
                "&a" + player.getUsername() + "&f a rejoint la partie &8[&e" + gameInstance.getPlayers().size() + "&7/&e" + gameInstance.getSlots() + "&8]"));
    }

    public void setPlayerSpectateArena(EPlayer player) {
        player.teleport(gameInstance.getSpawnLocation());
        player.refresh();
        player.setGameMode(GameMode.SPECTATOR);
        player.setState(State.DEMONSLAYER_SPECTATOR);
    }
}
