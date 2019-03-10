package net.lomeli.trophyslots.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.utils.command.ICommand;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableTextComponent;

@Environment(EnvType.CLIENT)
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
        switch (config) {
            case "slotRenderType":
                ModConfig.slotRenderType = (int) value;
                break;
            case "enableSecret":
                if ((boolean) value)
                    commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.config.special"), false);
                ModConfig.special = (boolean) value;
                break;
            default:
                TrophySlots.log.error("How the hell did you get here?!!");
                throw CONFIG_ERROR.create();
        }
        TrophySlots.config.saveConfig();
        commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.config.success", config,
                value.toString()), false);
        return 0;
    }

    @Override
    public String getName() {
        return TrophySlots.MOD_ID + "_client_config";
    }
}
