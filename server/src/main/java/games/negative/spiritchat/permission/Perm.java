package games.negative.spiritchat.permission;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Perm extends Permission {

    public Perm(String node) {
        super(PREFIX + "." + node);
    }

    public Perm(String node, String description, PermissionDefault defaultPerm) {
        super(PREFIX + "." + node, description, defaultPerm);
    }

    private static final String PREFIX = "spiritchat";

    public static final Perm ADMIN = new Perm("admin");
    public static final Perm UPDATE_NOTIFICATIONS = new Perm("updates");
    public static final Perm CHAT_COLORS = new Perm("chat-colors");
    public static final Perm CHAT_ITEM = new Perm("chat-item");
    public static final Perm COLOR_CMD = new Perm("color-gui");
    public static final Perm COLOR_BLACK = new Perm("color.black", "Allows usage of black color in chat", PermissionDefault.OP);
    public static final Perm COLOR_DARK_GREEN = new Perm("color.dark_green", "Allows usage of dark green color in chat", PermissionDefault.OP);
    public static final Perm COLOR_DARK_RED = new Perm("color.dark_red", "Allows usage of dark red color in chat", PermissionDefault.OP);
    public static final Perm COLOR_DARK_PURPLE = new Perm("color.dark_purple", "Allows usage of dark purple color in chat", PermissionDefault.OP);
    public static final Perm COLOR_GOLD = new Perm("color.gold", "Allows usage of gold color in chat", PermissionDefault.OP);
    public static final Perm COLOR_GRAY = new Perm("color.gray", "Allows usage of gray color in chat", PermissionDefault.OP);
    public static final Perm COLOR_DARK_GRAY = new Perm("color.dark_gray", "Allows usage of dark gray color in chat", PermissionDefault.OP);
    public static final Perm COLOR_BLUE = new Perm("color.blue", "Allows usage of blue color in chat", PermissionDefault.OP);
    public static final Perm COLOR_GREEN = new Perm("color.green", "Allows usage of green color in chat", PermissionDefault.OP);
    public static final Perm COLOR_AQUA = new Perm("color.aqua");
    public static final Perm COLOR_RED = new Perm("color.red", "Allows usage of red color in chat", PermissionDefault.OP);
    public static final Perm COLOR_LIGHT_PURPLE = new Perm("color.lightpurple");
    public static final Perm COLOR_YELLOW = new Perm("color.yellow");
    public static final Perm COLOR_WHITE = new Perm("color.white", "Allows usage of white color in chat", PermissionDefault.OP);
    public static final Perm COLOR_CUSTOM = new Perm("color.custom");
    public static final Perm CHAT_FORMAT = new Perm("chat.format", "Allows usage of formatting in chat", PermissionDefault.OP);
    public static final Perm COLOR_PRESETS = new Perm("color.presets", "Allows usage of color presets", PermissionDefault.OP);
    public static final Perm GLOW = new Perm("glow", "Allows usage of glow effects", PermissionDefault.OP);
    public static final Perm GLOW_BLACK = new Perm("glow.black", "Allows usage of black glow", PermissionDefault.OP);
    public static final Perm GLOW_DARK_BLUE = new Perm("glow.dark_blue", "Allows usage of dark blue glow", PermissionDefault.OP);
    public static final Perm GLOW_GREEN = new Perm("glow.green", "Allows usage of green glow", PermissionDefault.OP);
    public static final Perm GLOW_DARK_AQUA = new Perm("glow.dark_aqua", "Allows usage of dark aqua glow", PermissionDefault.OP);
    public static final Perm GLOW_DARK_RED = new Perm("glow.dark_red", "Allows usage of dark red glow", PermissionDefault.OP);
    public static final Perm GLOW_DARK_PURPLE = new Perm("glow.dark_purple", "Allows usage of dark purple glow", PermissionDefault.OP);
    public static final Perm GLOW_GOLD = new Perm("glow.gold", "Allows usage of gold glow", PermissionDefault.OP);
    public static final Perm GLOW_GRAY = new Perm("glow.gray", "Allows usage of gray glow", PermissionDefault.OP);
    public static final Perm GLOW_DARK_GRAY = new Perm("glow.dark_gray", "Allows usage of dark gray glow", PermissionDefault.OP);
    public static final Perm GLOW_BLUE = new Perm("glow.blue", "Allows usage of blue glow", PermissionDefault.OP);
    public static final Perm GLOW_LIME = new Perm("glow.lime", "Allows usage of lime glow", PermissionDefault.OP);
    public static final Perm GLOW_AQUA = new Perm("glow.aqua", "Allows usage of aqua glow", PermissionDefault.OP);
    public static final Perm GLOW_RED = new Perm("glow.red", "Allows usage of red glow", PermissionDefault.OP);
    public static final Perm GLOW_LIGHT_PURPLE = new Perm("glow.light_purple", "Allows usage of light purple glow", PermissionDefault.OP);
    public static final Perm GLOW_YELLOW = new Perm("glow.yellow", "Allows usage of yellow glow", PermissionDefault.OP);
    public static final Perm GLOW_WHITE = new Perm("glow.white", "Allows usage of white glow", PermissionDefault.OP);
    public static final Perm GLOW_RAINBOW = new Perm("glow.rainbow", "Allows usage of rainbow glow", PermissionDefault.OP);
    public static final Perm GLOW_PRIDE = new Perm("glow.pride", "Allows usage of pride glow", PermissionDefault.OP);
    public static final Perm MUTE_COMMAND = new Perm("mute", "Allows usage of mute command", PermissionDefault.OP);
    public static final Perm UNMUTE_COMMAND = new Perm("unmute", "Allows usage of unmute command", PermissionDefault.OP);
    public static final Perm IGNORE_COMMAND = new Perm("ignore", "Allows usage of ignore command", PermissionDefault.TRUE);
    public static final Perm MSG_COMMAND = new Perm("msg", "Allows usage of private messaging", PermissionDefault.TRUE);
    public static final Perm MSG_COLOR = new Perm("msg.color", "Allows usage of colors in private messages", PermissionDefault.OP);
};
