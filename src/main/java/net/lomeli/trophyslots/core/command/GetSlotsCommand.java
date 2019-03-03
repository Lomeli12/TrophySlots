package net.lomeli.trophyslots.core.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.knit.command.ICommand;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.command.arguments.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

import java.util.Collection;

public class GetSlotsCommand implements ICommand {
    private static final SimpleCommandExceptionType GET_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslatableTextComponent("command.trophyslots.get_slots.error"));

    @Override
    public void setupCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(ServerCommandManager.literal(getName())
                .executes((commandContext) -> givePlayerSlots(commandContext.getSource(), null))
                .then(ServerCommandManager.argument("targets", GameProfileArgumentType.create())
                        .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                        .suggests((commandContext, suggestionBuilder) -> {
                            PlayerManager playerManager = commandContext.getSource().getMinecraftServer().getPlayerManager();
                            return CommandSource.suggestMatching(playerManager.getPlayerList().stream()
                                    .map((player) -> player.getGameProfile().getName()), suggestionBuilder);
                        }).executes((commandContext) -> givePlayerSlots(commandContext.getSource(),
                                GameProfileArgumentType.getProfilesArgument(commandContext, "targets")))));
    }

    private int givePlayerSlots(ServerCommandSource commandSource, Collection<GameProfile> profiles) throws CommandSyntaxException {
        int i = 0;

        if (profiles != null) {
            PlayerManager playerManager = commandSource.getMinecraftServer().getPlayerManager();
            for (GameProfile profile : profiles) {
                ServerPlayerEntity player = playerManager.getPlayer(profile.getId());
                if (getPlayerSlots(commandSource, player))
                    i++;
            }
        } else {
            ServerPlayerEntity player = commandSource.getPlayer();
            if (getPlayerSlots(commandSource, player))
                i++;
        }
        if (i == 0)
            throw GET_SLOTS_ERROR.create();
        return i;
    }

    private boolean getPlayerSlots(ServerCommandSource commandSource, ServerPlayerEntity player) {
        if (player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.get_slots.success",
                    player.getGameProfile().getName(), slotManager.getSlotsUnlocked()), false);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "get_slots";
    }
}
