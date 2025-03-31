package games.negative.spiritchat.permission;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Perm extends Permission {

    private static final String PREFIX = "spiritchat";

    public static final Perm ADMIN = new Perm("admin");
    public static final Perm UPDATE_NOTIFICATIONS = new Perm("updates");
    public static final Perm CHAT_COLORS = new Perm("chat-colors");
    public static final Perm CHAT_ITEM = new Perm("chat-item");
    public static final Perm COLOR_GUI = new Perm("color-gui");
    public static final Perm COLOR_BLACK = new Perm("color.black", "Allows usage of black color in chat", PermissionDefault.OP);
    public static final Perm COLOR_DARK_BLUE = new Perm("color.dark_blue", "Allows usage of dark blue color in chat", PermissionDefault.OP);
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
    public static final Perm CHAT_MINIMESSAGE = new Perm("chat.minimessage", "Allows usage of MiniMessage formatting in chat", PermissionDefault.OP);
    public static final Perm COLOR_PRESETS = new Perm("color.presets", "Allows usage of color presets", PermissionDefault.OP);

    public Perm(@NotNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue, @Nullable Map<String, Boolean> children) {
        super(PREFIX + "." + name, description, defaultValue, children);
        Bukkit.getPluginManager().addPermission(this);
    }

    public Perm(@NotNull String name) {
        this(name, null, null, null);
    }

    public Perm(@NotNull String name, @Nullable String description) {
        this(name, description, null, null);
    }

    public Perm(@NotNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue) {
        this(name, description, defaultValue, null);
    }

    public Perm(@NotNull String name, @Nullable String description, @Nullable Map<String, Boolean> children) {
        this(name, description, null, children);
    }

    public Perm(@NotNull String name, @Nullable PermissionDefault defaultValue) {
        this(name, null, defaultValue, null);
    }

    public Perm(@NotNull String name, @Nullable Map<String, Boolean> children) {
        this(name, null, null, children);
    }
}
