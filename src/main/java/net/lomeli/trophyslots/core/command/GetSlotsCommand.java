package net.lomeli.trophyslots.core.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TranslationTextComponent;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;

public class GetSlotsCommand implements ISubCommand {
    private static final SimpleCommandExceptionType GET_SLOTS_ERROR =
            new SimpleCommandExceptionType(new TranslationTextComponent("command.trophyslots.get_slots.error"));

    @Override
    public void registerSubCommand(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder.then(Commands.literal(getName())
                .executes((context -> givePlayerSlots(context.getSource(), null)))
                .then(Commands.argument("target", GameProfileArgument.gameProfile())
                        .requires((source -> source.hasPermissionLevel(2)))
                        .executes((context -> givePlayerSlots(context.getSource(),
                                GameProfileArgument.getGameProfiles(context, "target"))))
                )
        );
    }

    private int givePlayerSlots(CommandSource source, Collection<GameProfile> profiles) throws CommandSyntaxException {
        AtomicInteger result = new AtomicInteger(0);

        if (profiles != null && !profiles.isEmpty()) {
            PlayerList playerList = source.getServer().getPlayerList();
            profiles.forEach(profile -> {
                IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(playerList.getPlayerByUUID(profile.getId()));
                if (playerSlots != null) {
                    result.getAndIncrement();
                    source.sendFeedback(new TranslationTextComponent("command.trophyslots.get_slots.success",
                            profile.getName(), playerSlots.getSlotsUnlocked()), false);
                }
            });
        } else {
            ServerPlayerEntity player = source.asPlayer();
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots != null) {
                result.getAndIncrement();
                source.sendFeedback(new TranslationTextComponent("command.trophyslots.get_slots.success",
                        player.getGameProfile().getName(), playerSlots.getSlotsUnlocked()), false);
            }
        }
        if (result.intValue() == 0)
            throw GET_SLOTS_ERROR.create();

        return result.intValue();
    }

    @Override
    public String getName() {
        return "get_slots";
    }
}