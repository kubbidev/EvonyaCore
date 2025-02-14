package me.kubbidev.evonyacore.commands;

import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.PlayerManager;
import me.kubbidev.nexuspowered.Commands;
import me.kubbidev.nexuspowered.command.context.CommandContext;
import me.kubbidev.nexuspowered.command.tabcomplete.CompletionSupplier;
import me.kubbidev.nexuspowered.command.tabcomplete.TabCompleter;
import me.kubbidev.nexuspowered.util.Players;
import me.kubbidev.nexuspowered.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

public final class MessageCommand {
    private MessageCommand() {}

    private static final CompletionSupplier COMPLETION = CompletionSupplier.startsWith(() ->
            Stream.concat(Players.all().stream().map(HumanEntity::getName), Stream.of("on", "off"))
    );

    public static void register(EvonyaPlugin plugin) {
        Commands.create()
                .assertPermission("evonyacore.message")
                .description("Send a private message to a player")
                .assertUsage("<on|off|target> [message]")
                .assertPlayer()
                .tabHandler(context -> TabCompleter.<Player>create()
                        .at(0, COMPLETION)
                        .at(1, CompletionSupplier.startsWith("<message>"))
                        .handle(context))
                .handler(context -> {
                    String arg = context.rawArg(0);
                    assert arg != null;
                    switch (arg.toLowerCase(Locale.ROOT)) {
                        case "on":
                            open(context);
                            break;
                        case "off":
                            close(context);
                            break;
                        default:
                            message(context);
                            break;
                    }
                }).register("message", "msg", "tell", "me", "whisper");
    }

    private static void message(CommandContext<Player> context) {
        Player sender = context.sender();
        Optional<Player> parse = context.arg(0).parse(Player.class);
        if (!parse.isPresent()) {
            context.reply("&cThis player is not online.");
            return;
        }
        Player target = parse.get();
        if (target == sender) {
            context.reply("&cYou cannot send a message to yourself.");
            return;
        }
        EPlayer eTarget = PlayerManager.wrapPlayer(target);
        if (!eTarget.isPrivateMessage()) {
            context.reply("&cThis player does not receive private messages.");
            return;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < context.args().size(); i++) {
            message.append(context.arg(i)).append(' ');
        }
        send(sender, target, message.toString());
        send(target, sender, message.toString());

        EPlayer eSender = PlayerManager.wrapPlayer(sender);
        eSender.setLastPrivateMessage(target.getUniqueId());
        eTarget.setLastPrivateMessage(sender.getUniqueId());
    }

    private static final String TEMPLATE = "&3%s &7» &3%s &8: &f%s";

    public static void send(
            CommandSender sender,
            CommandSender target,
            String message
    ) {
        target.sendMessage(Text.colorize(String.format(
                TEMPLATE,
                sender.getName(),
                target.getName(),
                message
        )));
    }

    private static void open(CommandContext<Player> context) {
        EPlayer player = PlayerManager.wrapPlayer(context.sender());
        if (player.isPrivateMessage()) {
            context.reply("&eYour private messages are already opened!");
        } else {
            player.setPrivateMessage(true);
            context.reply("&aYou've just opened your private messages!");
        }
    }

    private static void close(CommandContext<Player> context) {
        EPlayer player = PlayerManager.wrapPlayer(context.sender());
        if (!player.isPrivateMessage()) {
            context.reply("&eYour private messages are already closed!");
        } else {
            player.setPrivateMessage(false);
            context.reply("&cYou've just closed your private messages!");
        }
    }
}
