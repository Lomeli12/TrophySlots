package net.lomeli.trophyslots.core.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RemoveSlotsCommand implements ISubCommand {
    private static final SimpleCommandExceptionType REMOVE_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslatableComponent("command.trophyslots.remove_slots.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName()).requires(source -> source.hasPermission(3))
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

    private int removePlayersSlots(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) throws CommandSyntaxException {
        AtomicInteger result = new AtomicInteger(0);

        List<ServerPlayer> players = Lists.newArrayList();
        if (targets != null && !targets.isEmpty())
            players.addAll(targets);
        else
            players.add(source.getPlayerOrException());

        players.forEach(profile -> {
            if (removePlayerSlots(source, profile, amount))
                result.incrementAndGet();
        });

        if (result.intValue() == 0)
            throw REMOVE_SLOTS_ERROR.create();

        return result.intValue();
    }

    private boolean removePlayerSlots(CommandSourceStack source, ServerPlayer player, int amount) {
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null)
            return false;
        playerSlots.unlockSlot(-amount);
        PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
        source.sendSuccess(new TranslatableComponent("command.trophyslots.remove_slots.success",
                amount, player.getGameProfile().getName()), false);
        return true;
    }

    @Override
    public String getName() {
        return "lock_slots";
    }
}
