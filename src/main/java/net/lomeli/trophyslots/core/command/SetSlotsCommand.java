package net.lomeli.trophyslots.core.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.lomeli.knit.command.ICommand;
import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.command.arguments.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

import java.util.Collection;

public class SetSlotsCommand implements ICommand {
    private static final SimpleCommandExceptionType SET_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslatableTextComponent("command.trophyslots.set_slots.error"));

    @Override
    public void setupCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(ServerCommandManager.literal(getName()).requires(
                (commandSource) -> commandSource.hasPermissionLevel(2))
                .then(ServerCommandManager.argument("targets", GameProfileArgumentType.create())
                        .then(ServerCommandManager.argument("amount", IntegerArgumentType.integer(0, TrophySlots.MAX_SLOTS))
                                .executes((commandContext) -> setPlayerSlots(commandContext.getSource(),
                                        GameProfileArgumentType.getProfilesArgument(commandContext, "targets"),
                                        IntegerArgumentType.getInteger(commandContext, "amount"))
                                )
                        )
                )
        );
    }

    private int setPlayerSlots(ServerCommandSource commandSource, Collection<GameProfile> profiles, int amount) throws CommandSyntaxException {
        int i = 0;
        PlayerManager playerManager = commandSource.getMinecraftServer().getPlayerManager();

        for (GameProfile profile : profiles) {
            ServerPlayerEntity player = playerManager.getPlayer(profile.getId());
            if (player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                slotManager.setSlotsUnlocked(amount);
                MessageUtil.sendToClient(new MessageSlotClient(amount, ModConfig.reverseOrder), player);
                commandSource.sendFeedback(new TranslatableTextComponent("command.trophyslots.set_slots.success",
                        profile.getName(), slotManager.getSlotsUnlocked()), false);
                i++;
            }
        }
        if (i == 0)
            throw SET_SLOTS_ERROR.create();
        return i;
    }

    @Override
    public String getName() {
        return "set_slots";
    }
}
