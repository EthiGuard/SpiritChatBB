package games.negative.spiritchat.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import org.jetbrains.annotations.NotNull;
import games.negative.spiritchat.SpiritChatPlugin;
import games.negative.spiritchat.permission.Perm;
import org.bukkit.entity.Player;

public class CommandSpiritChat extends Command {

    public CommandSpiritChat() {
        super(CommandBuilder.builder().name("spiritchat")
                .description("The administrative command for SpiritChat.")
                .permission(Perm.ADMIN)
                .smartTabComplete(true)
        );

        injectSubCommand(CommandBuilder.builder().name("reload"), context -> {
            SpiritChatPlugin.instance().reload();

            SpiritChatPlugin.messages().getReloaded().create().send(context.sender());
        });

        injectSubCommand(CommandBuilder.builder().name("reset"), context -> {
            if (context.sender() instanceof Player player) {
                SpiritChatPlugin.colors().removeCustomColor(player.getUniqueId());
                SpiritChatPlugin.colors().setColor(player.getUniqueId(), "<white>");
                SpiritChatPlugin.instance().getColorConfiguration().save();
                SpiritChatPlugin.messages().getReset().create().send(context.sender());
            }
        });
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        SpiritChatPlugin.messages().getAdminCommandHelp().create().send(context.sender());
    }
}
