package net.lomeli.trophyslots.core.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerSlotHelper {
    @SuppressWarnings("all")
    public static IPlayerSlots getPlayerSlots(PlayerEntity player) {
        if (player == null || player instanceof FakePlayer)
            return null;
        return player.getCapability(PlayerSlotProvider.SLOTS, null)
                .orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
    }

    public static boolean isSlotUnlocked(Slot slot) {
        return isSlotUnlocked(slot.inventory, slot.getSlotIndex());
    }

    private static boolean isSlotUnlocked(IInventory inventory, int index) {
        if (inventory instanceof PlayerInventory) {
            PlayerEntity player = ((PlayerInventory) inventory).player;
            if (!player.abilities.isCreativeMode) {
                IPlayerSlots playerSlots = getPlayerSlots(player);
                return playerSlots.slotUnlocked(index);
            }
        }
        return true;
    }
}
