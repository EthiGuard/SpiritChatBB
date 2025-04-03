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

import java.util.stream.Collectors;

public class CommandMute extends Command {
    private final MuteManager muteManager;

    public CommandMute(MuteManager muteManager) {
        super(CommandBuilder.builder()
                .name("mute")
                .aliases("tempmute")
                .description("Mute a player")
                .permission(Perm.MUTE_COMMAND)
                .parameter("player", context -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()))
                .parameter("[duration]")
                .smartTabComplete(true));
        this.muteManager = muteManager;
    }

    @Override
    public void execute(CommandContext context) {
        String[] args = context.args();
        if (args.length == 0) {
            context.sender().sendMessage(Component.text()
                .append(Component.text("Usage: ", NamedTextColor.RED))
                .append(Component.text("/mute <player> [duration] ", NamedTextColor.GRAY))
                .append(Component.text("- Duration format: 1s/1m/1h/1d", NamedTextColor.WHITE)));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            context.sender().sendMessage(Component.text("Player not found!", NamedTextColor.RED));
            return;
        }

        long duration = 0;
        if (args.length > 1) {
            try {
                duration = parseDuration(args[1]);
            } catch (IllegalArgumentException e) {
                context.sender().sendMessage(Component.text("Invalid duration format! Use s/m/h/d (e.g. 1h)", NamedTextColor.RED));
                return;
            }
        }

        muteManager.mutePlayer(target.getUniqueId(), duration);
        String durationText = duration == 0 ? "permanently" : "for " + formatDuration(duration);
        context.sender().sendMessage(Component.text("Muted " + target.getName() + " " + durationText, NamedTextColor.GREEN));
    }

    private long parseDuration(String input) {
        long multiplier;
        char unit = input.charAt(input.length() - 1);
        String number = input.substring(0, input.length() - 1);
        
        switch (unit) {
            case 's' -> multiplier = 1000;
            case 'm' -> multiplier = 60000;
            case 'h' -> multiplier = 3600000;
            case 'd' -> multiplier = 86400000;
            default -> throw new IllegalArgumentException("Invalid time unit");
        }

        return Long.parseLong(number) * multiplier;
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        if (seconds < 60) return seconds + (seconds == 1 ? " second" : " seconds");
        if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + (minutes == 1 ? " minute" : " minutes");
        }
        if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + (hours == 1 ? " hour" : " hours");
        }
        long days = seconds / 86400;
        return days + (days == 1 ? " day" : " days");
    }
}
