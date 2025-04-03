package games.negative.spiritchat.config;

import com.google.common.collect.Maps;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.alumina.message.Message;
import lombok.Getter;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
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
        private Map<String, String> colorPresets = new HashMap<>() {{
            put("sunset", "<gradient:#FF512F:#DD2476>");
            put("ocean", "<gradient:#2193b0:#6dd5ed>");
            put("forest", "<gradient:#134E5E:#71B280>");
            put("fire", "<gradient:#FF4B2B:#FF416C>");
            put("rainbow", "<gradient:#ff0000:#ffa500:#ffff00:#008000:#0000ff:#4b0082>");
            put("gold", "<gradient:#FFD700:#FDB931>");
            put("purple", "<gradient:#8E2DE2:#4A00E0>");
            put("emerald", "<gradient:#348F50:#56B4D3>");
            put("cotton-candy", "<gradient:#E8B4BC:#FAF0E6>");
            put("sky", "<gradient:#00B4DB:#0083B0>");
            put("cherry", "<gradient:#EB3349:#F45C43>");
            put("cosmic", "<gradient:#1F1C2C:#928DAB>");
            put("autumn", "<gradient:#DAD299:#B0DAB9>");
            put("valentine", "<gradient:#EF629F:#EECDA3>");
            put("aurora", "<gradient:#00C9FF:#92FE9D>");
            put("royal", "<gradient:#141E30:#243B55>");
            put("pride-rainbow", "<gradient:#FF0018:#FFA52C:#FFFF41:#008018:#0000F9:#86007D>");
            put("trans", "<gradient:#55CDFC:#F7A8B8:#FFFFFF:#F7A8B8:#55CDFC>");
            put("bi", "<gradient:#D60270:#9B4F96:#0038A8>");
            put("pan", "<gradient:#FF1B8D:#FFD700:#00B5FF>");
            put("ace", "<gradient:#000000:#A4A4A4:#FFFFFF:#800080>");
            put("nonbinary", "<gradient:#FCF434:#FFFFFF:#9C59D1:#000000>");
        }};

        public boolean isUseItemDisplay() {
            return useItemDisplay;
        }

        public boolean isUseStaticFormat() {
            return useStaticFormat;
        }

        public String getGlobalFormat() {
            return globalFormat;
        }

        public Optional<String> groupFormat(String groupName) {
            return Optional.ofNullable(groupFormats.get(groupName));
        }

        public Map<String, String> getPresets() {
            return colorPresets;
        }

        public Optional<String> getPreset(String presetName) {
            return Optional.ofNullable(colorPresets.get(presetName.toLowerCase()));
        }
    }

    @Configuration
    public static class Messages {

        @Comment("Reload message template")
        public List<String> reloaded = List.of("<green>Spiritchat reloaded.</green>");

        @Comment("Reset message template")
        public List<String> reset = List.of("<green>Your chat color has been reset.</green>");

        @Comment("Glow color set message template")
        public List<String> glowColorSet = List.of("<green>Your glow color has been set to %color%.</green>");

        @Comment("Glow disabled message template")
        public List<String> glowDisabled = List.of("<green>Your glow effect has been disabled.</green>");

        @Comment("Admin command help template")
        public List<String> adminCommandHelp = List.of("<gray>Usage: /spiritchat reload | reset</gray>");

        @Comment("Glow help message template")
        public List<String> glowHelp = List.of(
            "<yellow>Glow Commands:",
            "<gray>/glow <color> <yellow>- Sets your glow color",
            "<gray>/glow off <yellow>- Disables your glow effect",
            "",
            "<yellow>Available Colors:",
            "<gray>white, orange, magenta, light_blue, yellow, lime"
        );

        @Comment("Private message format when sending")
        public List<String> privateMessageSent = List.of("<gray>To %receiver%: %message%</gray>");

        @Comment("Private message format when receiving")
        public List<String> privateMessageReceived = List.of("<gray>From %sender%: %message%</gray>");

        @Comment("No one to reply to message")
        public List<String> noReplyTarget = List.of("<red>You have no one to reply to!</red>");

        public Message getReloaded() {
            return new Message(String.join("\n", reloaded));
        }

        public Message getReset() {
            return new Message(String.join("\n", reset));
        }

        public Message getGlowColorSet() {
            return new Message(String.join("\n", glowColorSet));
        }

        public Message getGlowDisabled() {
            return new Message(String.join("\n", glowDisabled));
        }

        public Message getAdminCommandHelp() {
            return new Message(String.join("\n", adminCommandHelp));
        }

        public Message getGlowHelp() {
            return new Message(String.join("\n", glowHelp));
        }

        public Message getPrivateMessageSent() {
            return new Message(String.join("\n", privateMessageSent));
        }

        public Message getPrivateMessageReceived() {
            return new Message(String.join("\n", privateMessageReceived));
        }

        public Message getNoReplyTarget() {
            return new Message(String.join("\n", noReplyTarget));
        }
    }
}
