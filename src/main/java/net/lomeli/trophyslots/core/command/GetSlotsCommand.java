package net.lomeli.trophyslots.core.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GetSlotsCommand implements ISubCommand {
    private static final SimpleCommandExceptionType GET_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslatableComponent("command.trophyslots.get_slots.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName())
                .executes((context -> givePlayerSlots(context.getSource(), null)))
                .then(Commands.argument("target", EntityArgument.players())
                        .requires((source -> source.hasPermission(2)))
                        .executes((context -> givePlayerSlots(context.getSource(),
                                EntityArgument.getPlayers(context, "target"))))
                )
        );
    }

    private int givePlayerSlots(CommandSourceStack source, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        AtomicInteger result = new AtomicInteger(0);

        List<ServerPlayer> players = Lists.newArrayList();
        if (targets != null && !targets.isEmpty())
            players.addAll(targets);
        else
            players.add(source.getPlayerOrException());

        players.forEach(player -> {
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots != null) {
                result.getAndIncrement();
                source.sendSuccess(new TranslatableComponent("command.trophyslots.get_slots.success",
                        player.getName(), playerSlots.getSlotsUnlocked()), false);
            }
        });
        if (result.intValue() == 0)
            throw GET_SLOTS_ERROR.create();

        return result.intValue();
    }

    @Override
    public String getName() {
        return "get_slots";
    }
}