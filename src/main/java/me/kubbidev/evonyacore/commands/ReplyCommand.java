package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.nexuspowered.Commands;
import me.kubbidev.nexuspowered.command.tabcomplete.CompletionSupplier;
import me.kubbidev.nexuspowered.command.tabcomplete.TabCompleter;
import me.kubbidev.nexuspowered.util.Players;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public final class ReplyCommand {
    private ReplyCommand() {}

    public static void register(EvonyaPlugin plugin) {
        Commands.create()
                .assertPermission("evonyacore.reply")
                .description("Reply to the last received private message")
                .assertUsage("<message>")
                .assertPlayer()
                .tabHandler(context -> TabCompleter.<Player>create()
                        .at(0, CompletionSupplier.startsWith("<message>"))
                        .handle(context))
                .handler(context -> {
                    Player sender = context.sender();
                    EPlayer eSender = PlayerManager.wrapPlayer(sender);

                    UUID lastPrivateMessage = eSender.getLastPrivateMessage();
                    if (lastPrivateMessage == null) {
                        context.reply("&cNobody sent you a private message.");
                        return;
                    }

                    Optional<Player> parse = Players.get(lastPrivateMessage);
                    if (!parse.isPresent()) {
                        context.reply("&cThe last received private message is not online.");
                        return;
                    }
                    Player target = parse.get();
                    EPlayer eTarget = PlayerManager.wrapPlayer(target);
                    if (!eTarget.isPrivateMessage()) {
                        context.reply("&cThis player does not receive private messages.");
                        return;
                    }
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < context.args().size(); i++) {
                        message.append(context.arg(i)).append(' ');
                    }
                    MessageCommand.send(sender, target, message.toString());
                    MessageCommand.send(target, sender, message.toString());
                    eTarget.setLastPrivateMessage(sender.getUniqueId());
                }).register("reply", "r");
    }
}
