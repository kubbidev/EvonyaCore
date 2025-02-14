package me.kubbidev.evonyacore.listeners;

import me.kubbidev.evonyacore.CityFunction;
import me.kubbidev.evonyacore.LobbyFunction;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    private final LobbyFunction lobbyFunction;
    private final CityFunction cityFunction;

    public BlockListener(LobbyFunction lobbyFunction, CityFunction cityFunction) {
        this.lobbyFunction = lobbyFunction;
        this.cityFunction = cityFunction;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final EPlayer player = PlayerManager.wrapPlayer(event.getPlayer());

        if (player.getWorld().equals(cityFunction.getCity()))
            return;

        if ((!player.getWorld().equals(lobbyFunction.getLobby())) && player.isBypass())
            return;

        if(player.getWorld().equals(lobbyFunction.getLobby()))
            event.setCancelled(true);

        if (player.hasGameInstance()) {
            if (!player.getGameInstance().getPlacedBlock().contains(block.getLocation()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlock();
        final EPlayer player = PlayerManager.wrapPlayer(event.getPlayer());

        if (player.getWorld().equals(cityFunction.getCity()))
            return;

        if ((!player.getWorld().equals(lobbyFunction.getLobby())) && player.isBypass())
            return;

        if(player.getWorld().equals(lobbyFunction.getLobby()))
            event.setCancelled(true);

        if (player.hasGameInstance()) {
            player.getGameInstance().getPlacedBlock().add(block.getLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getClickedBlock() == null)
            return;

        if (player.getWorld().equals(lobbyFunction.getLobby()))
            event.setCancelled(true);
    }

    @EventHandler
    public void tntExplosion(ExplosionPrimeEvent event) {
        final World world = event.getEntity().getWorld();

        if (world.equals(lobbyFunction.getLobby()) || world.equals(cityFunction.getCity())) {
            event.setCancelled(true);
        }
    }
}
