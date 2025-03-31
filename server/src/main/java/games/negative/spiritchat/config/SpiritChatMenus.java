package games.negative.spiritchat.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;
import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Configuration
public class SpiritChatMenus {

    @Comment({
            "Main color selection menu settings",
            "This menu shows the basic color options"
    })
    private MainMenu mainMenu = new MainMenu();

    @Comment({
            "Custom color selection menu settings",
            "This menu shows preset custom colors"
    })
    private CustomMenu customMenu = new CustomMenu();

    // Explicit getters if @Getter doesn't work
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public CustomMenu getCustomMenu() {
        return customMenu;
    }

    @Configuration
    @Getter
    public static class MainMenu {
        @Comment("Title of the main color selection menu")
        private String title = "<dark_gray>Color Selection</dark_gray>";

        @Comment("Material for the border items")
        private String borderMaterial = "GRAY_STAINED_GLASS_PANE";

        @Comment("Material for the exit button")
        private String exitButtonMaterial = "BARRIER";

        @Comment("Material for the reset button")
        private String resetButtonMaterial = "STRUCTURE_VOID";

        @Comment("Material for the custom colors button")
        public String customColorsButtonMaterial = "PAPER";

        @Comment({
                "Layout of the color buttons",
                "Valid positions: 20,21,22,23,24,29,30,31,32,33"
        })
        public int[] colorSlots = {20, 21, 22, 23, 24, 29, 30, 31, 32, 33};

        public String getCustomColorsButtonMaterial() {
            return customColorsButtonMaterial;
        }

        public int[] getColorSlots() {
            return colorSlots;
        }
    }

    @Configuration
    @Getter
    public static class CustomMenu {
        @Comment("Title of the custom color selection menu")
        private String title = "<dark_gray>Color Selection</dark_gray>";

        @Comment("Material for the border items")
        private String borderMaterial = "GRAY_STAINED_GLASS_PANE";

        @Comment("Material for the back button")
        private String backButtonMaterial = "ARROW";

        @Comment("Material for the exit button")
        private String exitButtonMaterial = "BARRIER";

        @Comment("Material for the reset button")
        private String resetButtonMaterial = "STRUCTURE_VOID";

        @Comment({
                "Predefined custom colors",
                "Format: 'Display Name': '#HEXCOLOR'"
        })
        private Map<String, String> customColors = new LinkedHashMap<>() {{
            put("<color:#FF69B4><italic:false>Hot Pink", "#FF69B4");
            put("<color:#A020F0><italic:false>Purple", "#A020F0");
            put("<color:#FFA500><italic:false>Orange", "#FFA500");
            put("<color:#8B4513><italic:false>Brown", "#8B4513");
            put("<color:#40E0D0><italic:false>Turquoise", "#40E0D0");
            put("<color:#FF7F50><italic:false>Coral", "#FF7F50");
            put("<color:#FF4500><italic:false>Sunset Orange", "#FF4500");
            put("<color:#7851A9><italic:false>Royal Purple", "#7851A9");
            put("<color:#87CEEB><italic:false>Sky Blue", "#87CEEB");
            put("<color:#228B22><italic:false>Forest Green", "#228B22");
            put("<color:#FFD700><italic:false>Golden Yellow", "#FFD700");
            put("<color:#8B0000><italic:false>Deep Red", "#8B0000");
            put("<color:#FF69B4><italic:false>Hot Pink", "#FF69B4");
            put("<color:#50C878><italic:false>Emerald", "#50C878");
            put("<color:#DC143C><italic:false>Crimson", "#DC143C");
            put("<color:#E6E6FA><italic:false>Lavender", "#E6E6FA");
            put("<color:#00CED1><italic:false>Dark Turquoise", "#00CED1");
            put("<color:#FF1493><italic:false>Deep Pink", "#FF1493");
            put("<color:#FF00FF><italic:false>Magenta", "#FF00FF");
            put("<color:#9400D3><italic:false>Dark Violet", "#9400D3");
            put("<color:#4B0082><italic:false>Indigo", "#4B0082");
            put("<color:#32CD32><italic:false>Lime Green", "#32CD32");
            put("<color:#FA8072><italic:false>Salmon", "#FA8072");
            put("<color:#FF8C00><italic:false>Dark Orange", "#FF8C00");
            put("<color:#48D1CC><italic:false>Medium Turquoise", "#48D1CC");
            put("<color:#800080><italic:false>Purple", "#800080");
            put("<color:#DAA520><italic:false>Goldenrod", "#DAA520");
            put("<color:#FF6347><italic:false>Tomato", "#FF6347");
            put("<color:#4682B4><italic:false>Steel Blue", "#4682B4");
            put("<color:#800000><italic:false>Maroon", "#800000");
            put("<color:#808000><italic:false>Olive", "#808000");
            put("<color:#C71585><italic:false>Medium Violet Red", "#C71585");
            put("<color:#BDB76B><italic:false>Dark Khaki", "#BDB76B");
            put("<color:#8FBC8F><italic:false>Dark Sea Green", "#8FBC8F");
            put("<color:#DB7093><italic:false>Pale Violet Red", "#DB7093");
        }};
    }
}
