package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.spiritchat.data.IgnoreManager;
import games.negative.spiritchat.permission.Perm;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.stream.Collectors;

public class CommandIgnore extends Command {
    private final IgnoreManager ignoreManager;

    public CommandIgnore(IgnoreManager ignoreManager) {
        super(CommandBuilder.builder()
            .name("ignore")
            .aliases("unignore")
            .description("Ignore a player's chat messages")
            .permission(Perm.IGNORE_COMMAND)
            .parameter("player", context -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()))
            .smartTabComplete(true));
            
        this.ignoreManager = ignoreManager;
    }

    @Override
    public void execute(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage(Component.text("Only players can use this command!"));
            return;
        }

        String[] args = context.args();
        if (args.length == 0) {
            // List ignored players
            var ignored = ignoreManager.getIgnoredPlayers(player.getUniqueId());
            if (ignored.isEmpty()) {
                player.sendMessage(Component.text("You are not ignoring anyone.", NamedTextColor.YELLOW));
                return;
            }

            player.sendMessage(Component.text("Ignored players:", NamedTextColor.YELLOW));
            for (var uuid : ignored) {
                var ignoredName = Bukkit.getOfflinePlayer(uuid).getName();
                player.sendMessage(Component.text("- " + ignoredName, NamedTextColor.GRAY));
            }
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Component.text("Player not found!", NamedTextColor.RED));
            return;
        }

        if (target.equals(player)) {
            player.sendMessage(Component.text("You cannot ignore yourself!", NamedTextColor.RED));
            return;
        }

        if (ignoreManager.isIgnored(player.getUniqueId(), target.getUniqueId())) {
            ignoreManager.unignorePlayer(player.getUniqueId(), target.getUniqueId());
            player.sendMessage(Component.text("No longer ignoring " + target.getName(), NamedTextColor.GREEN));
        } else {
            ignoreManager.ignorePlayer(player.getUniqueId(), target.getUniqueId());
            player.sendMessage(Component.text("Now ignoring " + target.getName(), NamedTextColor.GREEN));
        }
    }
}
