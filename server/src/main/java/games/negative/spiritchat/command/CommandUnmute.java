package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.spiritchat.data.MuteManager;
import games.negative.spiritchat.permission.Perm;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.stream.Collectors;

public class CommandUnmute extends Command {
    private final MuteManager muteManager;

    public CommandUnmute(MuteManager muteManager) {
        super(CommandBuilder.builder()
                .name("unmute")
                .description("Unmute a player")
                .permission(Perm.UNMUTE_COMMAND)
                .parameter("Nobody is muted", context -> getMutedPlayerNames(muteManager))
                .smartTabComplete(true));
        this.muteManager = muteManager;
    }

    private static List<String> getMutedPlayerNames(MuteManager muteManager) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> muteManager.isMuted(player.getUniqueId()))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void execute(CommandContext context) {
        String[] args = context.args();
        if (args.length == 0) {
            context.sender().sendMessage(Component.text("Usage: /unmute <player>", NamedTextColor.RED));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            context.sender().sendMessage(Component.text("Player not found!", NamedTextColor.RED));
            return;
        }

        if (!muteManager.isMuted(target.getUniqueId())) {
            context.sender().sendMessage(Component.text(target.getName() + " is not muted!", NamedTextColor.RED));
            return;
        }

        muteManager.unmutePlayer(target.getUniqueId());
        context.sender().sendMessage(Component.text("Unmuted " + target.getName(), NamedTextColor.GREEN));
        target.sendMessage(Component.text("You have been unmuted!", NamedTextColor.GREEN));
    }
}
