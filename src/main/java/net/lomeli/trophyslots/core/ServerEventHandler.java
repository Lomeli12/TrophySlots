package net.lomeli.trophyslots.core;

import net.fabricmc.fabric.events.PlayerInteractionEvent;
import net.fabricmc.loader.FabricLoader;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ServerEventHandler {

    public static void registerEvents() {
        playerInteractEvent();
    }

    private static void playerInteractEvent() {
        PlayerInteractionEvent.INTERACT_BLOCK.register((PlayerEntity player, World world, Hand hand, BlockPos pos,
                                                        Direction dir, float hitX, float hitY, float hitZ) -> {
            if (player instanceof ISlotHolder) {
                int slotsUnlocked = ((ISlotHolder) player).getSlotManager().getSlotsUnlocked();
                player.addChatMessage(new StringTextComponent("Slots unlocked: " + slotsUnlocked + " " + FabricLoader.INSTANCE.getEnvironmentHandler().getEnvironmentType()), false);
            }
            return ActionResult.PASS;
        });
    }
}
