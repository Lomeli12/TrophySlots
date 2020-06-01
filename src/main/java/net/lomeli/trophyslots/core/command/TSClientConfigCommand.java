package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.core.network.MessageClientConfig;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.minecraftforge.common.util.FakePlayer;

public class TSClientConfigCommand implements ISubCommand {
    private static final SimpleCommandExceptionType CONFIG_ERROR =
            new SimpleCommandExceptionType(new TranslationTextComponent("command.trophyslots.config.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName())
                .then(Commands.literal("slotRenderType")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0, 3))
                                .executes(context -> setConfigValue(context.getSource(), "slotRenderType",
                                        IntegerArgumentType.getInteger(context, "value")))))
                .then(Commands.literal("enableSecret")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> setConfigValue(context.getSource(), "enableSecret",
                                        BoolArgumentType.getBool(context, "value")))))
        );
    }

    private int setConfigValue(CommandSource source, String config, Object value) throws CommandSyntaxException {
        boolean special = ClientConfig.special;
        int renderType = ClientConfig.slotRenderType;

        switch (config.toLowerCase()) {
            case "slotrendertype":
                renderType = (int) value;
                break;
            case "enablesecret":
                boolean enabled = (boolean) value;
                if (enabled)
                    source.sendFeedback(new TranslationTextComponent(
                            "command.trophyslots.config.special"), false);
                special = enabled;
                break;
            default:
                TrophySlots.log.error("How the hell did you get here?!!");
                throw CONFIG_ERROR.create();
        }

        if (!(source.asPlayer() instanceof FakePlayer)) {
            PacketHandler.sendToClient(new MessageClientConfig(special, renderType), source.asPlayer());
            source.sendFeedback(new TranslationTextComponent("command.trophyslots.config.success", config,
                    value.toString()), false);
        } else
            TrophySlots.log.warn("This command only affects the client side. Nothing happens server side.");

        return 0;
    }

    @Override
    public String getName() {
        return "client_config";
    }
}