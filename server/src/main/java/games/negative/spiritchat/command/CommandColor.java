// File: server/src/main/java/games/negative/spiritchat/command/CommandColor.java
package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.spiritchat.permission.Perm;
import games.negative.spiritchat.SpiritChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import games.negative.spiritchat.menu.ColorMenuHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommandColor extends Command implements Listener {

    public static final String[] BASIC_COLORS = new String[] {
        "<red>",
        "<yellow>",
        "<green>",
        "<blue>",
        "<dark_red>",
        "<gold>",
        "<dark_green>",
        "<dark_blue>",
        "<dark_purple>",
        "<gray>",
        "<dark_gray>",
        "<white>"
    };

    private static final Pattern HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");
    private static final Map<String, String> COLOR_NAMES = new HashMap<>() {{
        put("<red>", "Red");
        put("<yellow>", "Yellow");
        put("<green>", "Green");
        put("<blue>", "Blue");
        put("<dark_red>", "Dark Red");
        put("<gold>", "Gold");
        put("<dark_green>", "Dark Green");
        put("<dark_blue>", "Dark Blue");
        put("<dark_purple>", "Dark Purple");
        put("<gray>", "Gray");
        put("<dark_gray>", "Dark Gray");
        put("<white>", "White");
        put("<aqua>", "Aqua"); 
    }};

    public CommandColor() {
        super(CommandBuilder.builder()
            .name("color")
            .aliases("colour", "chatcolor", "chatcolour")
            .description("Opens the color selection GUI")
            .permission(Perm.COLOR_GUI)
            .smartTabComplete(true));

        Map<String, ColorConfig> colors = new HashMap<>();
        colors.put("dark_green", new ColorConfig("dark_green", Perm.COLOR_DARK_GREEN.getName(), "Sets your chat color to dark green", "<dark_green>"));
        colors.put("dark_red", new ColorConfig("dark_red", Perm.COLOR_DARK_RED.getName(), "Sets your chat color to dark red", "<dark_red>"));
        colors.put("dark_purple", new ColorConfig("dark_purple", Perm.COLOR_DARK_PURPLE.getName(), "Sets your chat color to dark purple", "<dark_purple>"));
        colors.put("gold", new ColorConfig("gold", Perm.COLOR_GOLD.getName(), "Sets your chat color to gold", "<gold>"));
        colors.put("gray", new ColorConfig("gray", Perm.COLOR_GRAY.getName(), "Sets your chat color to gray", "<gray>"));
        colors.put("dark_gray", new ColorConfig("dark_gray", Perm.COLOR_DARK_GRAY.getName(), "Sets your chat color to dark gray", "<dark_gray>"));
        colors.put("blue", new ColorConfig("blue", Perm.COLOR_BLUE.getName(), "Sets your chat color to blue", "<blue>"));
        colors.put("green", new ColorConfig("green", Perm.COLOR_GREEN.getName(), "Sets your chat color to green", "<green>"));
        colors.put("aqua", new ColorConfig("aqua", Perm.COLOR_AQUA.getName(), "Sets your chat color to aqua", "<aqua>"));
        colors.put("red", new ColorConfig("red", Perm.COLOR_RED.getName(), "Sets your chat color to red", "<red>"));
        colors.put("light_purple", new ColorConfig("light_purple", Perm.COLOR_LIGHT_PURPLE.getName(), "Sets your chat color to light purple", "<light_purple>"));
        colors.put("yellow", new ColorConfig("yellow", Perm.COLOR_YELLOW.getName(), "Sets your chat color to yellow", "<yellow>"));

        // Register all color subcommands
        colors.forEach((name, config) -> 
            injectSubCommand(CommandBuilder.builder()
                .name(config.name())
                .permission(config.permission())
                .description(config.description()),
                ctx -> setColorCommand(ctx, config.tag())));

        // Add reset command
        injectSubCommand(CommandBuilder.builder()
                .name("reset")
                .permission(Perm.COLOR_GUI.getName())
                .description("Resets your chat color to white"),
            ctx -> setColorCommand(ctx, "<white>"));

        // Add custom color command
        injectSubCommand(CommandBuilder.builder()
                .name("custom")
                .permission(Perm.COLOR_CUSTOM.getName())
                .description("Sets a custom hex color")
                .parameter("hex"),
            this::handleCustomColorCommand);

        // Add gradient command
        injectSubCommand(CommandBuilder.builder()
                .name("gradient")
                .permission(Perm.COLOR_CUSTOM.getName())
                .description("Sets a gradient color")
                .parameter("colors"),
            this::handleGradientColorCommand);
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage("Only players can use this command.");
            return;
        }

        String[] arguments = context.args();
        if (arguments.length > 0 && arguments[0].startsWith("#")) {
            handleCustomColor(player, arguments[0]);
            return;
        }

        player.sendMessage(MINIMESSAGE.deserialize("<yellow>Use /color <color> to set your chat color.</yellow>"));
    }

    private void setColorCommand(CommandContext context, String colorTag) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Only players can use this command!</red>"));
            return;
        }

        SpiritChatPlugin.colors().removeCustomColor(player.getUniqueId());
        setPlayerColor(player, colorTag);
        String colorName = getColorName(colorTag);
        player.sendMessage(MINIMESSAGE.deserialize("<green>Your chat color has been set to " + colorTag + colorName + getClosingTag(colorTag) + "</green>"));
        SpiritChatPlugin.instance().playDingSound(player);
    }

    private void handleCustomColorCommand(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Only players can use this command!</red>"));
            return;
        }

        String[] arguments = context.args();
        if (arguments.length == 0) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Usage: /color custom <#RRGGBB></red>"));
            return;
        }

        String hex = arguments[0];
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }

        if (!HEX_PATTERN.matcher(hex).matches()) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Invalid hex color! Format: #RRGGBB</red>"));
            return;
        }

        SpiritChatPlugin.colors().setCustomColor(player.getUniqueId(), hex);
        SpiritChatPlugin.instance().getColorConfiguration().save();
        context.sender().sendMessage(MINIMESSAGE.deserialize("<green>Your chat color has been set to <color:" + hex + ">this color</color></green>"));
        SpiritChatPlugin.instance().playDingSound(player);
    }

    private void handleGradientColorCommand(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Only players can use this command!</red>"));
            return;
        }

        String[] arguments = context.args();
        if (arguments.length < 2) {
            context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Usage: /color gradient <color1> <color2> [color3] ...</red>"));
            return;
        }

        StringBuilder gradientBuilder = new StringBuilder("<gradient:");
        for (String color : arguments) {
            if (!color.startsWith("#")) {
                color = "#" + color;
            }
            if (!HEX_PATTERN.matcher(color).matches()) {
                context.sender().sendMessage(MINIMESSAGE.deserialize("<red>Invalid hex color: " + color + " Format: #RRGGBB</red>"));
                return;
            }
            gradientBuilder.append(color).append(":");
        }
        gradientBuilder.setLength(gradientBuilder.length() - 1); // Remove the last colon
        gradientBuilder.append(">");

        String gradient = gradientBuilder.toString();
        SpiritChatPlugin.colors().setCustomColor(player.getUniqueId(), gradient);
        context.sender().sendMessage(MINIMESSAGE.deserialize("<green>Your chat color has been set to " + gradient + "this gradient</gradient></green>"));
        SpiritChatPlugin.instance().playDingSound(player); // Play ding sound
    }

    private void handleCustomColor(Player player, String hex) {
        if (!player.hasPermission(Perm.COLOR_CUSTOM)) {
            player.sendMessage(MINIMESSAGE.deserialize("<red>You don't have permission to use custom colors!</red>"));
            return;
        }

        if (!HEX_PATTERN.matcher(hex).matches()) {
            player.sendMessage(MINIMESSAGE.deserialize("<red>Invalid hex color! Format: #RRGGBB</red>"));
            return;
        }

        SpiritChatPlugin.colors().setCustomColor(player.getUniqueId(), hex);
        player.sendMessage(MINIMESSAGE.deserialize("<green>Your chat color has been set to <color:" + hex + ">this color</color></green>"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ColorMenuHolder holder)) return;

        event.setCancelled(true);
        event.setResult(org.bukkit.event.Event.Result.DENY);        
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
    }
    
    

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ColorMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) 
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (event.getDestination().getHolder() instanceof ColorMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof ColorMenuHolder)) return;

        event.getPlayer().setItemOnCursor(null);
        
        if (event.getPlayer() instanceof Player player) {
            player.getInventory().forEach(item -> {
                if (item != null && item.hasItemMeta() &&
                    item.getItemMeta().getPersistentDataContainer()
                        .has(new NamespacedKey(SpiritChatPlugin.instance(), "ghost-item"), 
                             PersistentDataType.BYTE)) {
                    player.getInventory().remove(item);
                }
            });
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        ItemStack item = event.getItem().getItemStack();
        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(SpiritChatPlugin.instance(), "ghost-item"), 
                     PersistentDataType.BYTE)) {
            event.setCancelled(true);
        }
    }

    public static final MiniMessage MINIMESSAGE = MiniMessage.builder().postProcessor(component -> component.decoration(TextDecoration.ITALIC, false)).build();

    public String getClosingTag(String openingTag) {
        if (openingTag == null || openingTag.isEmpty()) return "";
        if (!openingTag.startsWith("<")) return "";
        
        String tag = openingTag.substring(1, openingTag.length() - 1);
        if (tag.contains(":")) {
            tag = tag.substring(0, tag.indexOf(":"));
        }
        return "</" + tag + ">";
    }

    public static String getColorName(String colorTag) {
        if (colorTag == null || colorTag.isEmpty()) return "Default";
        return COLOR_NAMES.getOrDefault(colorTag, "Custom");
    }

    public void setPlayerColor(Player player, String colorTag) {
        SpiritChatPlugin.colors().setColor(player.getUniqueId(), colorTag);
        SpiritChatPlugin.instance().getColorConfiguration().save();
    }
    
    private String getCandleColorTag(Material material) {
        switch (material) {
            case RED_CANDLE:
                return "<red>";
            case YELLOW_CANDLE:
                return "<yellow>";
            case GREEN_CANDLE:
                return "<green>";
            case BLUE_CANDLE:
                return "<blue>";
            case BLACK_CANDLE:
                return "<black>";
            case WHITE_CANDLE:
                return "<white>";
            case GRAY_CANDLE:
                return "<gray>";
            case LIGHT_GRAY_CANDLE:
                return "<dark_gray>";
            case PINK_CANDLE:
                return "<light_purple>";
            case PURPLE_CANDLE:
                return "<dark_purple>";
            case ORANGE_CANDLE:
                return "<gold>";
            case LIME_CANDLE:
                return "<dark_green>";
            case CYAN_CANDLE:
                return "<aqua>";
            case LIGHT_BLUE_CANDLE:
                return "<dark_blue>";
            case MAGENTA_CANDLE:
                return "<red>";
            default:
                return null;
        }
    }
    
    private record ColorConfig(String name, String permission, String description, String tag) {}
}
