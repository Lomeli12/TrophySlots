package net.lomeli.trophyslots.core.command;


import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.network.MessageServerConfig;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;

public class TSConfigCommand implements ISubCommand {
    private static final SimpleCommandExceptionType CONFIG_ERROR =
            new SimpleCommandExceptionType(new TranslationTextComponent("command.trophyslots.config.error"));

    private static final String[] CONFIG_OPTIONS = { "loseSlotOnDeathAmount", "startingSlots", "advancementUnlock",
            "useTrophies", "buyTrophies", "reverseUnlockOrder", "loseSlotsOnDeath"};

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        for (int i = 0; i < CONFIG_OPTIONS.length; i++) {
            String config = CONFIG_OPTIONS[i];
            if (i < 2) {
                IntegerArgumentType intArg = IntegerArgumentType.integer(
                        config.equalsIgnoreCase("loseSlotOnDeathAmount") ? -1 : 9,
                        InventoryUtils.getMaxUnlockableSlots()
                );
                argumentBuilder.then(Commands.literal(config))
                    .then(Commands.argument("amount", intArg))
                        .executes(context -> setConfigValue(context.getSource(), config,
                            IntegerArgumentType.getInteger(context, "amount")));
            } else {
                argumentBuilder.then(Commands.literal(config))
                    .then(Commands.argument("value", BoolArgumentType.bool()))
                        .executes(context -> setConfigValue(context.getSource(), config,
                            BoolArgumentType.getBool(context, "value")));
            }
        }
    }

    private int setConfigValue(CommandSource source, String config, Object value) throws CommandSyntaxException {
        boolean advancementUnlock = ServerConfig.unlockViaAdvancements;
        boolean useTrophies = ServerConfig.canUseTrophy;
        boolean buyTrophies = ServerConfig.canBuyTrophy;
        boolean reverseOrder = ServerConfig.reverseOrder;
        boolean loseSlots = ServerConfig.loseSlots;
        int losingSlots = ServerConfig.loseSlotNum;
        int startingSlots = ServerConfig.startingSlots;

        switch (config.toLowerCase()) {
            case "advancementunlock":
                advancementUnlock = (boolean) value;
                break;
            case "usetrophies":
                useTrophies = (boolean) value;
                break;
            case "buytrophies":
                buyTrophies = (boolean) value;
                break;
            case "loseslotsondeath":
                loseSlots = (boolean) value;
                break;
            case "reverseunlockorder":
                reverseOrder = (boolean) value;
                break;
            case "startingslots":
                startingSlots = (int) value;
                break;
            case "loseslotondeathamount":
                losingSlots = (int) value;
                break;
            default:
                TrophySlots.log.error("How the hell did you get here?!!");
                throw CONFIG_ERROR.create();
        }

        PacketHandler.sendToServer(new MessageServerConfig(advancementUnlock, useTrophies, buyTrophies, reverseOrder,
                loseSlots, losingSlots, startingSlots));
        source.sendFeedback(new TranslationTextComponent("command.trophyslots.config.success", config,
                value.toString()), false);
        return 0;
    }

    @Override
    public String getName() {
        return "server_config";
    }
}
