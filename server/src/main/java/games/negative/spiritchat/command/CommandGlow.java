package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.alumina.config.Configuration;
import games.negative.spiritchat.SpiritChatPlugin;
import games.negative.spiritchat.config.SpiritChatPlayerColors;
import games.negative.spiritchat.permission.Perm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandGlow extends Command implements Listener {

    // Valid glow colors including additional ones
    private static final Set<String> VALID_COLORS = new HashSet<>(Arrays.asList(
            "black", "dark_blue", "green", "dark_aqua", "dark_red",
            "dark_purple", "gold", "gray", "dark_gray", "blue",
            "lime", "aqua", "red", "light_purple", "yellow", "white",
            "rainbow", "trans", "bi", "ace", "nonbinary", "lesbian"
    ));

    // Map to track active animated glow tasks for players
    private static final Map<String, BukkitRunnable> activeGlowTasks = new HashMap<>();

    public CommandGlow() {
        super(CommandBuilder.builder()
                .name("glow")
                .description("Sets your glow color")
                .permission(Perm.GLOW.getName())
                .smartTabComplete(true)
        );

        injectSubCommand(CommandBuilder.builder()
                .name("off")
                .description("Disables your glow effect")
                .permission(Perm.GLOW.getName()),
            this::handleGlowOffCommand);

        injectSubCommand(CommandBuilder.builder()
                .name("black")
                .description("Sets your glow color to black")
                .permission(Perm.GLOW_BLACK.getName()),
            context -> handleGlowColorCommand(context, "black"));

        injectSubCommand(CommandBuilder.builder()
                .name("dark_blue")
                .description("Sets your glow color to dark blue")
                .permission(Perm.GLOW_DARK_BLUE.getName()),
            context -> handleGlowColorCommand(context, "dark_blue"));

        injectSubCommand(CommandBuilder.builder()
                .name("green")
                .description("Sets your glow color to green")
                .permission(Perm.GLOW_GREEN.getName()),
            context -> handleGlowColorCommand(context, "green"));

        injectSubCommand(CommandBuilder.builder()
                .name("dark_aqua")
                .description("Sets your glow color to dark aqua")
                .permission(Perm.GLOW_DARK_AQUA.getName()),
            context -> handleGlowColorCommand(context, "dark_aqua"));

        injectSubCommand(CommandBuilder.builder()
                .name("dark_red")
                .description("Sets your glow color to dark red")
                .permission(Perm.GLOW_DARK_RED.getName()),
            context -> handleGlowColorCommand(context, "dark_red"));

        injectSubCommand(CommandBuilder.builder()
                .name("dark_purple")
                .description("Sets your glow color to dark purple")
                .permission(Perm.GLOW_DARK_PURPLE.getName()),
            context -> handleGlowColorCommand(context, "dark_purple"));

        injectSubCommand(CommandBuilder.builder()
                .name("gold")
                .description("Sets your glow color to gold")
                .permission(Perm.GLOW_GOLD.getName()),
            context -> handleGlowColorCommand(context, "gold"));

        injectSubCommand(CommandBuilder.builder()
                .name("gray")
                .description("Sets your glow color to gray")
                .permission(Perm.GLOW_GRAY.getName()),
            context -> handleGlowColorCommand(context, "gray"));

        injectSubCommand(CommandBuilder.builder()
                .name("dark_gray")
                .description("Sets your glow color to dark gray")
                .permission(Perm.GLOW_DARK_GRAY.getName()),
            context -> handleGlowColorCommand(context, "dark_gray"));

        injectSubCommand(CommandBuilder.builder()
                .name("blue")
                .description("Sets your glow color to blue")
                .permission(Perm.GLOW_BLUE.getName()),
            context -> handleGlowColorCommand(context, "blue"));

        injectSubCommand(CommandBuilder.builder()
                .name("lime")
                .description("Sets your glow color to lime")
                .permission(Perm.GLOW_LIME.getName()),
            context -> handleGlowColorCommand(context, "lime"));

        injectSubCommand(CommandBuilder.builder()
                .name("aqua")
                .description("Sets your glow color to aqua")
                .permission(Perm.GLOW_AQUA.getName()),
            context -> handleGlowColorCommand(context, "aqua"));

        injectSubCommand(CommandBuilder.builder()
                .name("red")
                .description("Sets your glow color to red")
                .permission(Perm.GLOW_RED.getName()),
            context -> handleGlowColorCommand(context, "red"));

        injectSubCommand(CommandBuilder.builder()
                .name("light_purple")
                .description("Sets your glow color to light purple")
                .permission(Perm.GLOW_LIGHT_PURPLE.getName()),
            context -> handleGlowColorCommand(context, "light_purple"));

        injectSubCommand(CommandBuilder.builder()
                .name("yellow")
                .description("Sets your glow color to yellow")
                .permission(Perm.GLOW_YELLOW.getName()),
            context -> handleGlowColorCommand(context, "yellow"));

        injectSubCommand(CommandBuilder.builder()
                .name("white")
                .description("Sets your glow color to white")
                .permission(Perm.GLOW_WHITE.getName()),
            context -> handleGlowColorCommand(context, "white"));

        injectSubCommand(CommandBuilder.builder()
                .name("rainbow")
                .description("Sets your glow color to rainbow")
                .permission(Perm.GLOW_RAINBOW.getName()),
            context -> handleGlowColorCommand(context, "rainbow"));

        injectSubCommand(CommandBuilder.builder()
                .name("pride")
                .description("Sets your glow color to pride")
                .permission(Perm.GLOW_PRIDE.getName()),
            context -> handleGlowColorCommand(context, "pride"));

        injectSubCommand(CommandBuilder.builder()
                        .name("trans")
                        .description("Sets your glow color to trans")
                        .permission(Perm.GLOW_PRIDE.getName()),
                context -> handleGlowColorCommand(context, "trans"));
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        context.sender().sendMessage("Usage: /glow <color> or /glow off");
    }

    private void handleGlowOffCommand(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage("Only players can use this command!");
            return;
        }

        // Cancel any existing glow task
        BukkitRunnable existingTask = activeGlowTasks.remove(player.getName());
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Remove from existing team
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
            team.unregister();
        }

        // Disable glowing effect
        player.setGlowing(false);

        // Save glow status
        SpiritChatPlugin.instance().getColorConfiguration().get().setGlowColor(player.getUniqueId(), null);
        SpiritChatPlugin.instance().getColorConfiguration().save();

        SpiritChatPlugin.messages().getGlowDisabled().create().send(context.sender());
    }

    private void handleGlowColorCommand(CommandContext context, String color) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage("Only players can use this command!");
            return;
        }

        // First disable any existing glow
        BukkitRunnable existingTask = activeGlowTasks.remove(player.getName());
        if (existingTask != null) {
            existingTask.cancel();
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team oldTeam = scoreboard.getEntryTeam(player.getName());
        if (oldTeam != null) {
            oldTeam.removeEntry(player.getName());
            oldTeam.unregister();
        }

        // Create new team for the player
        String teamName = player.getName() + "_glow";
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
        team = scoreboard.registerNewTeam(teamName);
        
        team.addEntry(player.getName());
        player.setGlowing(true);

        // Save glow status
        SpiritChatPlugin.instance().getColorConfiguration().get().setGlowColor(player.getUniqueId(), color);
        SpiritChatPlugin.instance().getColorConfiguration().save();

        BukkitRunnable newTask = null;
        if (color.equalsIgnoreCase("rainbow")) {
            newTask = startRainbowGlow(player, team);
        } else if (color.equalsIgnoreCase("pride")) {
            newTask = startPrideGlow(player, team);
        } else if (color.equalsIgnoreCase("trans")) {
            newTask = starttransGlow(player, team);
        } else {
            team.setColor(ChatColor.valueOf(color.toUpperCase()));
        }

        if (newTask != null) {
            activeGlowTasks.put(player.getName(), newTask);
        }

        SpiritChatPlugin.messages().getGlowColorSet().create().replace("%color%", color).send(context.sender());
    }

    public BukkitRunnable startRainbowGlow(Player player, Team team) {
        new BukkitRunnable() {
            final ChatColor[] colors = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE, ChatColor.DARK_PURPLE};
            int index = 0;

            @Override
            public void run() {
                if (!player.isOnline() || !team.hasEntry(player.getName())) {
                    this.cancel();
                    return;
                }

                team.setColor(colors[index]);
                index = (index + 1) % colors.length;
            }
        }.runTaskTimer(SpiritChatPlugin.instance(), 0, 20);
        return null;
    }

    public BukkitRunnable startPrideGlow(Player player, Team team) {
        new BukkitRunnable() {
            final ChatColor[] colors = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE, ChatColor.DARK_PURPLE};
            int index = 0;

            @Override
            public void run() {
                if (!player.isOnline() || !team.hasEntry(player.getName())) {
                    this.cancel();
                    return;
                }

                team.setColor(colors[index]);
                index = (index + 1) % colors.length;
            }
        }.runTaskTimer(SpiritChatPlugin.instance(), 0, 20);
        return null;
    }

    public BukkitRunnable starttransGlow(Player player, Team team) {
        new BukkitRunnable() {
            final ChatColor[] colors = {ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.WHITE, ChatColor.AQUA};
            int index = 0;

            @Override
            public void run() {
                if (!player.isOnline() || !team.hasEntry(player.getName())) {
                    this.cancel();
                    return;
                }

                team.setColor(colors[index]);
                index = (index + 1) % colors.length;
            }
        }.runTaskTimer(SpiritChatPlugin.instance(), 0, 20);
        return null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String savedGlowColor = getColorConfiguration().get().getGlowColor(player.getUniqueId());

        if (savedGlowColor != null) {
            // Small delay to ensure player is fully loaded
            Bukkit.getScheduler().runTaskLater(SpiritChatPlugin.instance(), () -> {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                String teamName = player.getName() + "_glow";
                Team team = scoreboard.getTeam(teamName);
                if (team != null) {
                    team.unregister();
                }
                team = scoreboard.registerNewTeam(teamName);
                team.addEntry(player.getName());
                player.setGlowing(true);

                if (savedGlowColor.equalsIgnoreCase("rainbow")) {
                    startRainbowGlow(player, team);
                } else if (savedGlowColor.equalsIgnoreCase("pride")) {
                    startPrideGlow(player, team);
                } else if (savedGlowColor.equalsIgnoreCase("trans")) {
                    starttransGlow(player, team);
                } else {
                    try {
                        team.setColor(ChatColor.valueOf(savedGlowColor.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                        // Invalid color saved, remove it
                        getColorConfiguration().get().setGlowColor(player.getUniqueId(), null);
                        getColorConfiguration().save();
                        team.unregister();
                        player.setGlowing(false);
                    }
                }
            }, 5L);
        }
    }

    private @NotNull Configuration<SpiritChatPlayerColors> getColorConfiguration() {
        return SpiritChatPlugin.instance().getColorConfiguration();
    }
}

// BOTTOM - DO NOT REMOVE