package games.negative.spiritchat.config;

import com.google.common.collect.Maps;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.alumina.message.Message;
import lombok.Getter;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class SpiritChatConfig {

    @Comment({
            "",
            "Whether or not to check for updates",
            "and send notifications to the console and operators."
    })
    private boolean checkForUpdates = true;

    @Comment({
            "",
            "Whether or not to send bStats data.",
            "bStats is a service that collects data about the server.",
            "This data is used to improve the plugin and",
            "is completely anonymous."
    })
    private boolean bstats = true;

    @Comment({
            "",
            "Section of the configuration that",
            "handles everything with chat formatting."
    })
    private Format chatFormat = new Format();

    @Comment({
            "",
            "The messages sent from the plugin."
    })
    private Messages messages = new Messages();

    public boolean checkForUpdates() {
        return checkForUpdates;
    }

    public boolean bStats() {
        return bstats;
    }

    @NotNull
    public Format format() {
        return chatFormat;
    }

    @NotNull
    public Messages messages() {
        return messages;
    }

    public boolean playDingSound() {
        return true;
    }

    @Configuration
    public static class Format {

        @Comment({
                "",
                "Whether or not to use {i} and {item} to display",
                "the item a player is holding in chat."
        })
        private boolean useItemDisplay = true;

        @Comment({
                "",
                "Whether or not to use the static format for chat messages.",
        })
        private boolean useStaticFormat = true;

        @Comment({
                "",
                "Whether or not to allow MiniMessage formatting in chat.",
                "This will allow players to use MiniMessage format codes like <red> and <bold>",
                "alongside legacy color codes if they have permission."
        })
        private boolean allowMiniMessage = true;

        @Comment({
                "",
                "The global format for all chat messages.",
                "Only applicable when 'use-static-format' is true!",
                " ",
                "Placeholders:",
                "  %display-name% - The player's display-name.",
                "  %username% - The player's username.",
                "  %message% - The message sent by the player.",
                "  (+ all PlaceholderAPI placeholders (if enabled))",
        })
        private String globalFormat = "<gray>%username%</gray> <dark_gray>></dark_gray> %message%";

        @Comment({
                "",
                "The format for group chat messages.",
                "Only applicable when 'use-static-format' is false!",
                " ",
                "Placeholders:",
                "  %display-name% - The player's display-name.",
                "  %username% - The player's username.",
                "  %message% - The message sent by the player.",
                "  (+ all PlaceholderAPI placeholders (if enabled))",
        })
        private Map<String, String> groupFormats = Maps.newHashMap(Map.of(
                "default", "<gray>%username%</gray> <dark_gray>></dark_gray> %message%",
                "admin", "<red>[Admin]</red> <white>%username%</white> <dark_gray>></dark_gray> <red>%message%</red>"
        ));

        @Comment({
                "",
                "Color presets that can be used with /color preset <name>",
                "Format: 'name': '<color format>'",
                "You can use gradient or regular colors"
        })
        private Map<String, String> colorPresets = Maps.newHashMap(Map.of(
                "sunset", "<gradient:#FF512F:#DD2476>",
                "ocean", "<gradient:#2193b0:#6dd5ed>",
                "forest", "<gradient:#134E5E:#71B280>",
                "fire", "<gradient:#FF4B2B:#FF416C>",
                "rainbow", "<gradient:#ff0000:#ffa500:#ffff00:#008000:#0000ff:#4b0082:#ee82ee>",
                "gold", "<gradient:#FFD700:#FDB931>",
                "purple", "<gradient:#8E2DE2:#4A00E0>",
                "emerald", "<gradient:#348F50:#56B4D3>"
        ));

        public boolean useItemDisplay() {
            return useItemDisplay;
        }

        public boolean useStaticFormat() {
            return useStaticFormat;
        }

        public boolean allowMiniMessage() {
            return allowMiniMessage;
        }

        @NotNull
        public Optional<String> globalFormat() {
            return Optional.ofNullable(globalFormat);
        }

        @CheckReturnValue
        public Optional<String> groupFormat(@NotNull String group) {
            return Optional.ofNullable(groupFormats.get(group));
        }

        @CheckReturnValue
        public Optional<String> getPreset(@NotNull String name) {
            return Optional.ofNullable(colorPresets.get(name.toLowerCase()));
        }

        @NotNull
        public Map<String, String> getPresets() {
            return new HashMap<>(colorPresets);
        }
    }

    @Getter
    @Configuration
    public static class Messages {

        @Comment({
                "",
                "The message sent when someone uses the admin command",
                "without any arguments or incorrectly."
        })
        private Message adminCommandHelp = new Message(
                "",
                "<color:#2bbce0>SpiritChat</color>",
                "<white><click:suggest_command:'/spiritchat reload'>/spiritchat reload</click></white> <gray>- Reload the plugin's configurations</gray>",
                ""
        );

        @Comment({
                "",
                "The message sent when the plugin's configurations",
                "are reloaded using the admin command."
        })
        private Message reloaded = new Message("<b><color:#2bbce0>SpiritChat</color></b> <dark_gray>></dark_gray> <gray>Reloaded the plugin's configurations!</gray>");

        @Comment({
                "",
                "The message sent when the player's chat color is reset."
        })
        private Message reset = new Message("<b><color:#2bbce0>SpiritChat</color></b> <dark_gray>></dark_gray> <gray>Your chat color has been reset to <white>White</white>!</gray>");

    }

}
