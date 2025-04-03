package games.negative.spiritchat;

import com.google.common.base.Preconditions;
import de.exlll.configlib.NameFormatters;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.config.Configuration;
import games.negative.alumina.logger.Logs;
import games.negative.alumina.message.Message;
import games.negative.alumina.util.Tasks;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import games.negative.spiritchat.command.CommandSpiritChat;
import games.negative.spiritchat.command.CommandColor;
import games.negative.spiritchat.command.CommandGlow;
import games.negative.spiritchat.command.CommandMute;
import games.negative.spiritchat.command.CommandUnmute;
import games.negative.spiritchat.command.CommandIgnore;
import games.negative.spiritchat.command.CommandMessage;
import games.negative.spiritchat.config.SpiritChatConfig;
import games.negative.spiritchat.config.SpiritChatPlayerColors;
import games.negative.spiritchat.config.serializer.MessageSerializer;
import games.negative.spiritchat.listener.PlayerChatListener;
import games.negative.spiritchat.data.MuteManager;
import games.negative.spiritchat.data.IgnoreManager;
import games.negative.spiritchat.update.UpdateCheckTask;

import java.io.File;
import java.util.Optional;

public class SpiritChatPlugin extends AluminaPlugin {

    private static SpiritChatPlugin instance;
    private final Configuration<SpiritChatConfig> config;
    private final Configuration<SpiritChatPlayerColors> colorsConfig;
    private LuckPerms luckperms;
    private Metrics metrics;
    private MuteManager muteManager;
    private IgnoreManager ignoreManager;

    public SpiritChatPlugin() {
        this.config = Configuration.config(new File(getDataFolder(), "main.yml"), SpiritChatConfig.class, builder -> {
            builder.setNameFormatter(NameFormatters.LOWER_KEBAB_CASE);
            builder.inputNulls(true);
            builder.outputNulls(false);
            builder.addSerializer(Message.class, new MessageSerializer());
            builder.header("""
                    |---------------------------------------------|
                    |                SpiritChat                   |
                    |              Version: %s                 |
                    |---------------------------------------------|
                    """.formatted(getPluginMeta().getVersion()));
            builder.footer("""
                    Author: ericlmao
                    """);
            return builder;
        });

        this.colorsConfig = Configuration.config(new File(getDataFolder(), "colors.yml"), SpiritChatPlayerColors.class, builder -> {
            builder.setNameFormatter(NameFormatters.LOWER_KEBAB_CASE);
            builder.inputNulls(true);
            builder.outputNulls(false);
            return builder;
        });
    }

    private void handleGlowOffCommand(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage("Only players can use this command!");
            return;
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getEntryTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
        }

        // Save glow status
        SpiritChatPlugin.instance().getColorConfiguration().get().setGlowColor(player.getUniqueId(), null);
        SpiritChatPlugin.instance().getColorConfiguration().save();

        SpiritChatPlugin.messages().getGlowDisabled().create().send(context.sender());
    }

    @Override
    public void load() {
        instance = this;

        // Initialize configurations
        this.config.reload();
        this.colorsConfig.reload();
    }

    @Override
    public void enable() {
        // Check for updates every 10 minutes
        Tasks.async(new UpdateCheckTask(getPluginMeta().getVersion()), 20 * 15, 20 * 60 * 10);

        if (config().bStats()) {
            Logs.info("Enabling bStats. Thank you for helping us improve the plugin! You can disable this at any time in the config.");
            this.metrics = new Metrics(this, 24829);
        } else {
            Logs.warning("bStats is disabled. We ask that you have this enabled to help us improve the plugin. This can be changed in the config!");
        }

        loadLuckPerms();

        this.muteManager = new MuteManager();
        this.ignoreManager = new IgnoreManager();

        CommandColor commandColor = new CommandColor();
        registerListener(new PlayerChatListener(commandColor));
        registerCommand(new CommandSpiritChat());
        registerCommand(commandColor);
        registerCommand(new CommandGlow());
        registerCommand(new CommandMute(muteManager));
        registerCommand(new CommandUnmute(muteManager));
        registerCommand(new CommandIgnore(ignoreManager));
        registerCommand(new CommandMessage());
        registerCommand(new CommandMessage.CommandReply());
    }

    @Override
    public void disable() {
        if (metrics != null) metrics.shutdown();
    }

    public void reload() {
        config.reload();
        colorsConfig.reload();
    }

    private void loadLuckPerms() {
        long start = System.currentTimeMillis();

        try {
            Class.forName("net.luckperms.api.LuckPermsProvider");

            luckperms = LuckPermsProvider.get();

            Logs.info("Successfully loaded LuckPerms support in %sms.".formatted(System.currentTimeMillis() - start));
        } catch (Exception e) {
            Logs.error("Failed to load LuckPerms support! Some features may not work. Install LuckPerms to gain full functionality!");
        }
    }

    @NotNull
    public Configuration<SpiritChatConfig> configuration() {
        return Preconditions.checkNotNull(config, "Configuration has not been initialized");
    }

    @NotNull
    public Configuration<SpiritChatPlayerColors> getColorConfiguration() {
        return Preconditions.checkNotNull(colorsConfig, "Color configuration has not been initialized");
    }

    @CheckReturnValue
    public Optional<LuckPerms> getLuckPerms() {
        return Optional.ofNullable(luckperms);
    }

    @NotNull
    public static SpiritChatPlugin instance() {
        Preconditions.checkNotNull(instance, "SpiritChatPlugin has not been initialized yet.");

        return instance;
    }

    @NotNull
    public static SpiritChatConfig config() {
        return instance().configuration().get();
    }

    @NotNull
    public static SpiritChatPlayerColors colors() {
        return instance().getColorConfiguration().get();
    }

    @NotNull
    public static SpiritChatConfig.Messages messages() {
        return config().messages();
    }

    @CheckReturnValue
    public static Optional<LuckPerms> luckperms() {
        return instance().getLuckPerms();
    }

    @NotNull
    public MuteManager getMuteManager() {
        return muteManager;
    }

    @NotNull
    public IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }

    public void playDingSound(Player player) {
        if (config().playDingSound()) {
            player.playSound(player.getLocation(), "entity.arrow_hit_player", 1.0f, 1.0f);
        }
    }

}
