package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.spiritchat.SpiritChatPlugin;
import games.negative.spiritchat.permission.Perm;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandMessage extends Command {
    private static final Map<UUID, UUID> lastMessaged = new HashMap<>();
    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    public CommandMessage() {
        super(CommandBuilder.builder()
                .name("msg")
                .aliases("m", "w", "tell", "whisper")
                .description("Send a private message to a player")
                .permission(Perm.MSG_COMMAND)
                .parameter("player", context -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()))
                .parameter("message")
                .smartTabComplete(true));
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        if (!(context.sender() instanceof Player sender)) {
            context.sender().sendMessage("Only players can use this command!");
            return;
        }

        String[] args = context.args();
        if (args.length < 2) {
            sender.sendMessage("Usage: /msg <player> <message>");
            return;
        }

        handleMessage(sender, args[0], String.join(" ", args).substring(args[0].length()).trim());
    }

    private void handleMessage(Player sender, String targetName, String message) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage("Player not found!");
            return;
        }

        if (target == sender) {
            sender.sendMessage("You cannot message yourself!");
            return;
        }

        if (SpiritChatPlugin.instance().getIgnoreManager().isIgnored(sender.getUniqueId(), target.getUniqueId()) ||
            SpiritChatPlugin.instance().getIgnoreManager().isIgnored(target.getUniqueId(), sender.getUniqueId())) {
            sender.sendMessage("You cannot message this player!");
            return;
        }

        String finalMessage = !sender.hasPermission(Perm.MSG_COLOR) ? 
            "<white>" + MINIMESSAGE.escapeTags(message) + "</white>" : 
            message;

        sender.sendMessage(SpiritChatPlugin.messages().getPrivateMessageSent()
                .create()
                .replace("%receiver%", target.getName())
                .replace("%message%", finalMessage)
                .asComponent());

        target.sendMessage(SpiritChatPlugin.messages().getPrivateMessageReceived()
                .create()
                .replace("%sender%", sender.getName())
                .replace("%message%", finalMessage)
                .asComponent());

        lastMessaged.put(target.getUniqueId(), sender.getUniqueId());
        lastMessaged.put(sender.getUniqueId(), target.getUniqueId());
    }

    public static class CommandReply extends Command {
        
        public CommandReply() {
            super(CommandBuilder.builder()
                    .name("r")
                    .aliases("reply")
                    .description("Reply to the last person who messaged you")
                    .permission(Perm.MSG_COMMAND)
                    .parameter("message")
                    .smartTabComplete(true));
        }

        @Override
        public void execute(@NotNull CommandContext context) {
            if (!(context.sender() instanceof Player sender)) {
                context.sender().sendMessage("Only players can use this command!");
                return;
            }

            UUID lastMessagedUUID = lastMessaged.get(sender.getUniqueId());
            if (lastMessagedUUID == null) {
                sender.sendMessage(SpiritChatPlugin.messages().getNoReplyTarget()
                        .create()
                        .asComponent());
                return;
            }

            Player target = Bukkit.getPlayer(lastMessagedUUID);
            if (target == null) {
                sender.sendMessage("Player is no longer online!");
                return;
            }

            String[] args = context.args();
            if (args.length == 0) {
                sender.sendMessage("Usage: /r <message>");
                return;
            }

            String message = String.join(" ", args);
            String finalMessage = !sender.hasPermission(Perm.MSG_COLOR) ? 
                "<white>" + MINIMESSAGE.escapeTags(message) + "</white>" : 
                message;

            sender.sendMessage(SpiritChatPlugin.messages().getPrivateMessageSent()
                    .create()
                    .replace("%receiver%", target.getName())
                    .replace("%message%", finalMessage)
                    .asComponent());

            target.sendMessage(SpiritChatPlugin.messages().getPrivateMessageReceived()
                    .create()
                    .replace("%sender%", sender.getName())
                    .replace("%message%", finalMessage)
                    .asComponent());

            lastMessaged.put(target.getUniqueId(), sender.getUniqueId());
        }
    }

    public static void clearLastMessaged(UUID player) {
        lastMessaged.remove(player);
    }
}
