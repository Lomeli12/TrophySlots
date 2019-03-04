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
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageServerConfig;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

import java.util.Arrays;
import java.util.List;

public class TSConfigCommand implements ICommand {
    private static final SimpleCommandExceptionType CONFIG_ERROR =
            new SimpleCommandExceptionType(new TranslatableTextComponent("command.trophyslots.config.error"));

    private static final List<String> CONFIG_OPTIONS = Arrays.asList("advancementUnlock", "useTrophies", "buyTrophies",
            "reverseUnlockOrder", "loseSlotsOnDeath", "loseSlotOnDeathAmount", "startingSlots");

    private static final int integerArgIndex = 5;

    @Override
    public void setupCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> parentCommand = ServerCommandManager.literal(getName())
                .requires((commandSource) -> commandSource.hasPermissionLevel(2));

        for (int i = 0; i < CONFIG_OPTIONS.size(); i++) {
            String config = CONFIG_OPTIONS.get(i);
            if (i < integerArgIndex) {
                parentCommand.then(ServerCommandManager.literal(config)
                        .then(ServerCommandManager.argument("value", BoolArgumentType.bool()).executes(
                                (commandContext) -> setConfigValue(commandContext.getSource(), config,
                                        BoolArgumentType.getBool(commandContext, "value")))));
            } else {
                IntegerArgumentType argumentType = IntegerArgumentType.integer();
                if (config.equals("loseSlotOnDeathAmount"))
                    argumentType = IntegerArgumentType.integer(-1, InventoryUtils.MAX_SLOTS);
                if (config.equals("startingSlots"))
                    argumentType = IntegerArgumentType.integer(9, InventoryUtils.MAX_SLOTS);
                parentCommand.then(ServerCommandManager.literal(config)
                        .then(ServerCommandManager.argument("amount", argumentType).executes(
                                (commandContext) -> setConfigValue(commandContext.getSource(), config,
                                        IntegerArgumentType.getInteger(commandContext, "amount")))));
            }
        }
        commandDispatcher.register(parentCommand);
    }

    private int setConfigValue(ServerCommandSource commandSource, String config, Object value) throws CommandSyntaxException {
        switch (config) {
            case "advancementUnlock":
                ModConfig.unlockViaAdvancements = (boolean) value;
                break;
            case "useTrophies":
                ModConfig.canUseTrophy = (boolean) value;
                break;
            case "buyTrophies":
                ModConfig.canBuyTrophy = (boolean) value;
                break;
            case "reverseUnlockOrder":
                ModConfig.reverseOrder = (boolean) value;
                break;
            case "loseSlotsOnDeath":
                ModConfig.loseSlots = (boolean) value;
                break;
            case "loseSlotOnDeathAmount":
                ModConfig.loseSlotNum = (int) value;
                break;
            case "startingSlots":
                ModConfig.startingSlots = (int) value;
                break;
            default:
                TrophySlots.log.error("How the hell did you get here?!!");
                throw CONFIG_ERROR.create();
        }
        TrophySlots.config.saveConfig();
        for (ServerPlayerEntity player : commandSource.getMinecraftServer().getPlayerManager().getPlayerList()) {
            MessageUtil.sendToClient(new MessageServerConfig(), player);
        }
        commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.config.success", config,
                value.toString()), false);
        return 0;
    }

    @Override
    public String getName() {
        return TrophySlots.MOD_ID + "_config";
    }

}
