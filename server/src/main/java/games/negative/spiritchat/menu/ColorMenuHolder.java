package games.negative.spiritchat.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ColorMenuHolder implements InventoryHolder {

    private final String menuType;

    public ColorMenuHolder(String menuType) {
        this.menuType = menuType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException("This method should not be called directly");
    }

    public boolean isMainMenu() {
        return "main".equals(menuType);
    }

    public boolean isCustomMenu() {
        return "custom".equals(menuType);
    }

    public String getMenuType() {
        return menuType;
    }
}
