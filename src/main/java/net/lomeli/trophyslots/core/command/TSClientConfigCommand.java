package net.lomeli.trophyslots.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.knit.utils.command.ICommand;
import net.lomeli.knit.utils.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.network.MessageClientConfig;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableTextComponent;

public class TSClientConfigCommand implements ICommand {
    private static final SimpleCommandExceptionType CONFIG_ERROR =
            new SimpleCommandExceptionType(new TranslatableTextComponent("command.trophyslots.config.error"));

    @Override
    public void setupCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> parentCommand = ServerCommandManager.literal(getName());
        parentCommand.then(ServerCommandManager.literal("slotRenderType")
                .then(ServerCommandManager.argument("value", IntegerArgumentType.integer(0, 3))
                        .executes((commandContext) -> setConfigValue(commandContext.getSource(), "slotRenderType",
                                IntegerArgumentType.getInteger(commandContext, "value")))));
        parentCommand.then(ServerCommandManager.literal("enableSecret")
                .then(ServerCommandManager.argument("value", BoolArgumentType.bool())
                        .executes((commandContext) -> setConfigValue(commandContext.getSource(), "enableSecret",
                                BoolArgumentType.getBool(commandContext, "value")))));
        commandDispatcher.register(parentCommand);
    }

    private int setConfigValue(ServerCommandSource commandSource, String config, Object value) throws CommandSyntaxException {
        boolean sendRender = false;
        boolean sendSpecial = false;
        int slotRenderType = 0;
        boolean special = true;
        switch (config) {
            case "slotRenderType":
                slotRenderType = (int) value;
                sendRender = true;
                break;
            case "enableSecret":
                if ((boolean) value)
                    commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.config.special"), false);
                special = (boolean) value;
                sendSpecial = true;
                break;
            default:
                TrophySlots.log.error("How the hell did you get here?!!");
                throw CONFIG_ERROR.create();
        }
        if (commandSource.getPlayer() != null) {
            MessageUtil.sendToClient(new MessageClientConfig(slotRenderType, sendRender, special, sendSpecial),
                    commandSource.getPlayer());
            commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.config.success", config,
                    value.toString()), false);
        } else
            TrophySlots.log.warn("This command only affects the client side. Nothing happens server side.");

        return 0;
    }

    @Override
    public String getName() {
        return TrophySlots.MOD_ID + "_client_config";
    }
}
