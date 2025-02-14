package me.kubbidev.evonyacore.queue;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.events.PlayerConnectionEvent;
import me.kubbidev.evonyacore.game.core.GameInstance;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.players.EvonyaPlayer;
import me.kubbidev.evonyacore.utils.Item;
import me.kubbidev.evonyacore.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class QueueSystem {

    private final LobbyFunction lobbyFunction;
    private final List<Queue> queueList;


    public QueueSystem(LobbyFunction lobbyFunction) {
        this.lobbyFunction = lobbyFunction;
        this.queueList = new ArrayList<>();
    }

    public List<Queue> getQueueList() {
        return queueList;
    }

    public void connect(EvonyaPlayer player, GameInstance gameInstance) {
        final Queue queue = getQueue(gameInstance);

        if (isQueue(player)) {
            player.sendMessage(EvonyaPlugin.PREFIX + "Vous êtes déjà dans une &cfile d'attente&f.");
        }
        else {
            queueRegularClean(player, queue);
        }
    }

    public boolean isQueue(EvonyaPlayer player) {
        return this.queueList.stream().anyMatch(queue -> queue.containsPlayer(player));
    }

    public Queue getQueue(EvonyaPlayer player) {
        return this.queueList.stream().filter(queue -> queue.containsPlayer(player)).findFirst().orElse(null);
    }

    public Queue getQueue(GameInstance gameInstance) {

        Queue queue = getQueueFromList(gameInstance);

        if (queue == null) queue = createQueue(gameInstance);
        return queue;
    }

    private void queueRegularClean(EvonyaPlayer player, Queue queue) {
        queue.addPlayer(player);
        player.getInventory().setItem(4, new Item(Material.BARRIER).setName("&8┃ &c&lQuitter la file d'attente&f (Clic-Droit)").parseItem());
        player.updateInventory();

        this.sendConnectionMessage(player, queue);

        final BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!queue.containsPlayer(player)) {
                    this.cancel();
                    return;
                }
                new Message("File d'attente &7[&e" + (queue.getPlayers().indexOf(player) + 1) + "&7/&e" + queue.getPlayers().size() + "&7]").sendActionBar(player.getPlayer());
            }
        }.runTaskTimer(EvonyaPlugin.INSTANCE, 1, 5);

        Bukkit.getServer().getScheduler().runTaskLater(EvonyaPlugin.INSTANCE, () -> {
            runnable.cancel();
            queue.removePlayer(player);

            if (queue.getPlayers().isEmpty())
                queueList.remove(queue);
            lobbyFunction.giveLobbyItems(player);

            final GameInstance gameInstance = queue.getGame();
            final PlayerConnectionEvent event = new PlayerConnectionEvent(player, gameInstance);

            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;

            gameInstance.getConnectionUtils().connect(player);

        }, getQueueTime(queue) * 20);
    }

    private Queue createQueue(GameInstance gameInstance) {
        final Queue queue = new Queue(gameInstance);

        this.queueList.add(queue);
        return queue;
    }

    private Queue getQueueFromList(GameInstance gameInstance) {
        for (Queue queue : this.queueList) {
            if (queue.getUniqueId() == gameInstance.getUniqueId())
                return queue;
        }
        return null;
    }

    private void sendConnectionMessage(EvonyaPlayer player, Queue queue) {
        player.sendMessage(" ");
        player.sendMessage(EvonyaPlugin.PREFIX + "Vous avez bien été ajouté à la &3file d'attente&f...");
        player.sendMessage(EvonyaPlugin.PREFIX + "Attente éstimée : &c" + getQueueTime(queue) + "&f seconde(s).");
        player.sendMessage(" ");
    }

    private long getQueueTime(Queue queue) {
        return queue.getPlayers().size();
    }
}
