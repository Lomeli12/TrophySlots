package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;

public class RemoveSlotsCommand implements ISubCommand {
    private static final SimpleCommandExceptionType REMOVE_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslationTextComponent("command.trophyslots.remove_slots.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName()).requires(source -> source.hasPermissionLevel(3))
                .then(Commands.literal("all")
                        .executes(context -> removePlayersSlots(context.getSource(), null,
                                InventoryUtils.getMaxUnlockableSlots()))
                ).then(Commands.argument("amount", IntegerArgumentType.integer(1, InventoryUtils.getMaxUnlockableSlots()))
                        .executes(context -> removePlayersSlots(context.getSource(), null,
                                IntegerArgumentType.getInteger(context, "amount")))
                ).then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.literal("all")
                                .executes(context -> removePlayersSlots(context.getSource(),
                                        EntityArgument.getPlayers(context, "target"),
                                        InventoryUtils.getMaxUnlockableSlots()))
                        ).then(Commands.argument("amount", IntegerArgumentType.integer(1, InventoryUtils.getMaxUnlockableSlots()))
                                .executes(context -> removePlayersSlots(context.getSource(),
                                        EntityArgument.getPlayers(context, "target"),
                                        IntegerArgumentType.getInteger(context, "amount")))
                        )
                )
        );
    }

    private int removePlayersSlots(CommandSource source, Collection<ServerPlayerEntity> targets, int amount) throws CommandSyntaxException {
        AtomicInteger result = new AtomicInteger(0);

        List<ServerPlayerEntity> players = Lists.newArrayList();
        if (targets != null && !targets.isEmpty())
            players.addAll(targets);
        else
            players.add(source.asPlayer());

        players.forEach(profile -> {
            if (removePlayerSlots(source, profile, amount))
                result.incrementAndGet();
        });

        if (result.intValue() == 0)
            throw REMOVE_SLOTS_ERROR.create();

        return result.intValue();
    }

    private boolean removePlayerSlots(CommandSource source, ServerPlayerEntity player, int amount) {
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null)
            return false;
        playerSlots.unlockSlot(-amount);
        PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
        source.sendFeedback(new TranslationTextComponent("command.trophyslots.remove_slots.success",
                amount, player.getGameProfile().getName()), false);
        return true;
    }

    @Override
    public String getName() {
        return "lock_slots";
    }
}
