package games.negative.spiritchat.config;

import de.exlll.configlib.Configuration;
import de.exlll.configlib.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Configuration
public class SpiritChatPlayerColors {
    private Map<String, String> colors;
    public Map<String, String> glowColors;
    private Map<String, String> customColors;

    public SpiritChatPlayerColors() {
        this.colors = new HashMap<>();
        this.glowColors = new HashMap<>();
        this.customColors = new HashMap<>();
    }

    public String getGlowColor(UUID uuid) {
        return glowColors.get(uuid.toString());
    }

    public void setGlowColor(UUID uuid, String color) {
        if (color == null) {
            glowColors.clear();
        } else {
            glowColors.put(uuid.toString(), color);
        }
    }

    public Map<String, String> getGlowColors() {
        return glowColors;
    }
    private static final Pattern HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$|^<gradient:#[0-9A-Fa-f]{6}:#[0-9A-Fa-f]{6}>$");

    @Comment("Stores player color preferences using their UUID as key")
    public Map<String, String> playerColors = new HashMap<>();

    public String getColor(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        return playerColors.getOrDefault(uuid.toString(), "<white>");
    }

    public void setColor(@NotNull UUID uuid, @Nullable String colorTag) {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        if (colorTag != null && !colorTag.isEmpty()) {
            playerColors.put(uuid.toString(), colorTag);
        }
    }

    public boolean hasColor(UUID uuid) {
        return playerColors.containsKey(uuid.toString());
    }

    public String getCustomColor(UUID uuid) {
        return customColors.get(uuid.toString());
    }

    public void setCustomColor(UUID uuid, String color) {
        if (color != null && (HEX_PATTERN.matcher(color).matches() || color.startsWith("<gradient:"))) {
            customColors.put(uuid.toString(), color);
        }
    }

    public boolean hasCustomColor(UUID uuid) {
        return customColors.containsKey(uuid.toString());
    }

    public void removeCustomColor(UUID uuid) {
        customColors.remove(uuid.toString());
    }

    public Map<String, String> getPlayerColors() {
        return new HashMap<>(playerColors);
    }

    public Map<String, String> getCustomColors() {
        return new HashMap<>(customColors);
    }

    public void save() {
        // This method will be handled by ConfigLib's serialization
    }

    public void reload() {
        // ConfigLib will handle the reloading automatically
        // No need to manually clear the maps
    }
}
