package net.lomeli.trophyslots.core.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class SetSlotsCommand implements ISubCommand {
    private static final SimpleCommandExceptionType SET_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslationTextComponent("command.trophyslots.set_slots.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName()).requires(source -> source.hasPermissionLevel(2))
                .then(Commands.argument("target", EntityArgument.players())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, InventoryUtils.getMaxUnlockableSlots()))
                                .executes(context -> setPlayersSlots(context.getSource(),
                                        EntityArgument.getPlayers(context, "targets"),
                                        IntegerArgumentType.getInteger(context, "amount")))))
                .then(Commands.argument("amount",
                        IntegerArgumentType.integer(0, InventoryUtils.getMaxUnlockableSlots()))
                        .executes(context -> setPlayersSlots(context.getSource(), null,
                                IntegerArgumentType.getInteger(context, "amount")))));
    }

    private int setPlayersSlots(CommandSource source, Collection<ServerPlayerEntity> players, int amount) throws CommandSyntaxException {
        AtomicInteger result = new AtomicInteger(0);

        if (players != null && !players.isEmpty()) {
            players.forEach(player -> {
                if (setPlayerSlot(source, player, amount))
                    result.incrementAndGet();
            });
        } else if (setPlayerSlot(source, source.asPlayer(), amount))
            result.incrementAndGet();

        if (result.intValue() == 0)
            throw SET_SLOTS_ERROR.create();

        return result.intValue();
    }

    private boolean setPlayerSlot(CommandSource source, ServerPlayerEntity player, int amount) {
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null)
            return false;
        playerSlots.setSlotsUnlocked(amount);
        if (playerSlots.getSlotsUnlocked() > 0)
            ModCriteria.UNLOCK_SLOT.trigger(player);
        PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
        source.sendFeedback(new TranslationTextComponent("command.trophyslots.set_slots.success",
                player.getGameProfile().getName(), playerSlots.getSlotsUnlocked()), false);
        return true;
    }

    @Override
    public String getName() {
        return "set_slots";
    }
}