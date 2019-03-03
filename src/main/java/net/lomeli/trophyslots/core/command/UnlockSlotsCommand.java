package net.lomeli.trophyslots.core.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.knit.command.ICommand;
import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.command.arguments.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

import java.util.Collection;

public class UnlockSlotsCommand implements ICommand {
    private static final SimpleCommandExceptionType REMOVE_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslatableTextComponent("command.trophyslots.unlock_slots.error"));

    @Override
    public void setupCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(ServerCommandManager.literal(getName()).requires(
                (commandSource) -> commandSource.hasPermissionLevel(2))
                .then(ServerCommandManager.literal("all")
                        .executes((commandContext) -> unlockPlayersSlots(commandContext.getSource(), null,
                                InventoryUtils.getMaxUnlockableSlots()))
                        .then(ServerCommandManager.argument("targets", GameProfileArgumentType.create())
                                .executes((commandContext) -> unlockPlayersSlots(commandContext.getSource(),
                                        GameProfileArgumentType.getProfilesArgument(commandContext, "targets"),
                                        InventoryUtils.getMaxUnlockableSlots()))))
                .then(ServerCommandManager.argument("amount", IntegerArgumentType.integer(1,
                        InventoryUtils.getMaxUnlockableSlots()))
                        .executes((commandContext) -> unlockPlayersSlots(commandContext.getSource(), null,
                                IntegerArgumentType.getInteger(commandContext, "amount")))
                        .then(ServerCommandManager.argument("targets", GameProfileArgumentType.create())
                                .executes((commandContext) -> unlockPlayersSlots(commandContext.getSource(),
                                        GameProfileArgumentType.getProfilesArgument(commandContext, "targets"),
                                        IntegerArgumentType.getInteger(commandContext, "amount")))))
        );
    }

    private int unlockPlayersSlots(ServerCommandSource commandSource, Collection<GameProfile> profiles, int amount) throws CommandSyntaxException {
        int i = 0;

        if (profiles != null && !profiles.isEmpty()) {
            PlayerManager playerManager = commandSource.getMinecraftServer().getPlayerManager();
            for (GameProfile profile : profiles) {
                ServerPlayerEntity player = playerManager.getPlayer(profile.getId());
                if (unlockSlots(commandSource, player, amount))
                    i++;
            }
        } else {
            ServerPlayerEntity player = commandSource.getPlayer();
            if (unlockSlots(commandSource, player, amount))
                i++;
        }
        if (i == 0)
            throw REMOVE_SLOTS_ERROR.create();
        return i;
    }

    private boolean unlockSlots(ServerCommandSource commandSource, ServerPlayerEntity player, int amount) {
        if (player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            slotManager.unlockSlot(amount);
            MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked()), player);
            commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.unlock_slots.success",
                    amount, player.getGameProfile().getName()), false);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "unlock_slots";
    }
}
