package net.lomeli.trophyslots.core.capabilities;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerSlotHelper {
    @SuppressWarnings("ConstantConditions")
    public static IPlayerSlots getPlayerSlots(Player player) {
        if (player == null || player instanceof FakePlayer)
            return null;
        return player.getCapability(PlayerSlotProvider.SLOTS, null).orElse(new PlayerSlotManager());
    }

    @SuppressWarnings("unused")
    public static boolean isSlotUnlocked(Slot slot) {
        return isSlotUnlocked(slot.container, slot.getSlotIndex());
    }

    public static boolean isSlotUnlocked(Container inventory, int index) {
        if (inventory instanceof Inventory) {
            Player player = ((Inventory) inventory).player;
            if (!player.isCreative()) {
                IPlayerSlots playerSlots = getPlayerSlots(player);
                return playerSlots.slotUnlocked(index);
            }
        }
        return true;
    }

    public static void backupToPersist(Player player, IPlayerSlots slots) {
        var modTag = new CompoundTag();
        slots.serialize(modTag);
        player.getPersistentData().put(TrophySlots.MOD_ID, modTag);
    }

    public static void readFromPersist(Player player, IPlayerSlots slots) {
        if (player.getPersistentData().contains(TrophySlots.MOD_ID, 10))
            slots.deserialize(player.getPersistentData().getCompound(TrophySlots.MOD_ID));
    }
}
