package net.lomeli.trophyslots.core.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.lomeli.trophyslots.core.network.MessageUpdateConfig;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.server.command.EnumArgument;

public class TSConfigCommand implements ISubCommand {

    private static final String[] CONFIG_OPTIONS = {"loseSlotOnDeathAmount", "startingSlots", "listmode", "advancementUnlock",
            "useTrophies", "buyTrophies", "reverseUnlockOrder", "loseSlotsOnDeath"};

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        LiteralArgumentBuilder<CommandSourceStack> base = Commands.literal(getName());

        for (int i = 0; i < CONFIG_OPTIONS.length; i++) {
            String config = CONFIG_OPTIONS[i];
            if (i < 2) {
                IntegerArgumentType intArg = IntegerArgumentType.integer(
                        config.equalsIgnoreCase("loseSlotOnDeathAmount") ? -1 : 9,
                        InventoryUtils.getMaxUnlockableSlots()
                );
                base.then(Commands.literal(config).requires((source -> source.hasPermission(2)))
                        .then(Commands.argument("amount", intArg)
                                .executes(context -> setConfigValue(context.getSource(), config,
                                        IntegerArgumentType.getInteger(context, "amount")))));
            } else if (i == 2) {
                base.then(Commands.literal(config).requires((source -> source.hasPermission(2)))
                        .then(Commands.argument("mode", EnumArgument.enumArgument(AdvancementHandler.ListMode.class))
                                .executes(context -> setConfigValue(context.getSource(), config,
                                        context.getArgument("mode", AdvancementHandler.ListMode.class)))));
            } else {
                base.then(Commands.literal(config).requires((source -> source.hasPermission(2)))
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> setConfigValue(context.getSource(), config,
                                        BoolArgumentType.getBool(context, "value")))));
            }
        }
        argumentBuilder.then(base);
    }

    private int setConfigValue(CommandSourceStack source, String config, Object value) {
        var trueValue = value;
        if (value instanceof AdvancementHandler.ListMode)
            trueValue = ((AdvancementHandler.ListMode) value).ordinal();
        PacketHandler.sendToServer(new MessageUpdateConfig(config.toLowerCase(), trueValue));
        source.sendSuccess(new TranslatableComponent("command.trophyslots.config.success", config,
                value.toString()), false);
        return 0;
    }

    @Override
    public String getName() {
        return "server_config";
    }
}
