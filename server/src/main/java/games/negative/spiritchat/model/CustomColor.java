package games.negative.spiritchat.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class CustomColor {
    private final String name;
    private final String hex;
    private final String minimessage;

    public CustomColor(@NotNull String name, @NotNull String hex) {
        this.name = name;
        this.hex = hex;
        this.minimessage = "<color:" + hex + ">";
    }
}
