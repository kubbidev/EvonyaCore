package me.kubbidev.evonyacore.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.kubbidev.nexuspowered.protocol.Protocol;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Minecraft 1.18 Message
 *
 * @version 0.1.0
 * @author Alkaaz
 */
public class Message {

    private final String message;

    private String command;
    private String hoverMessage;

    /**
     * Create a new 1.18 message
     *
     * @param message
     * message
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * Set command to run or suggest
     *
     * @param command
     * command
     */
    public Message setCommand(String command) {
        this.command = command;
        return this;
    }

    /**
     * Set hoverMessage text
     *
     * @param hoverMessage
     * hoverMessage
     */
    public Message setHoverMessage(String hoverMessage) {
        this.hoverMessage = hoverMessage;
        return this;
    }

    /**
     * Send the message to a player
     *
     * @param player
     * player
     */
    public void sendActionBar(Player player) {
        sendActionbar(player, WrappedChatComponent.fromText(Color.translate(this.message)));
    }

    private void sendMessage(Player player, WrappedChatComponent component) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.CHAT);
        container.getBytes().write(0, (byte) 1);
        container.getChatComponents().write(0, component);
        Protocol.sendPacket(player, container);
    }

    private void sendActionbar(Player player, WrappedChatComponent component) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.CHAT);
        container.getBytes().write(0, (byte) 2);
        container.getChatComponents().write(0, component);
        Protocol.sendPacket(player, container);
    }

    /**
     * Send the message to a player
     *
     * @param player
     * player
     */
    public void sendSuggestCommand(Player player) {
        Objects.requireNonNull(this.command, "Command field cant be null");
        Objects.requireNonNull(this.hoverMessage, "HoverMessage field cant be null");

        String json = "{\"text\":\""
                + Color.translate(this.message) + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""
                + Color.translate(this.hoverMessage) + "\"},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + this.command + "\"}}";
        sendMessage(player, WrappedChatComponent.fromJson(json));
    }

    /**
     * Send the message to a player
     *
     * @param player
     * player
     */
    public void sendRunCommand(Player player) {
        Objects.requireNonNull(this.command, "Command field cant be null");
        Objects.requireNonNull(this.hoverMessage, "HoverMessage field cant be null");

        String json = "{\"text\":\""
                + Color.translate(this.message) + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""
                + Color.translate(this.hoverMessage) + "\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + this.command + "\"}}";
        sendMessage(player, WrappedChatComponent.fromJson(json));
    }
}