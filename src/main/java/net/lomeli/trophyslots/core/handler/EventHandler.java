package net.lomeli.trophyslots.core.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SimpleUtil;

public class EventHandler {

    //Handles awarding slots on achievement unlocks, if that method is enabled
    @SubscribeEvent
    public void achievementGetEvent(AchievementEvent event) {
        if (event.isCanceled() || event.entityPlayer.worldObj.isRemote)
            return;
        EntityPlayerMP playerMP = (EntityPlayerMP) event.entityPlayer;
        if (!playerMP.func_147099_x().hasAchievementUnlocked(event.achievement) && TrophySlots.unlockViaAchievements)
            SimpleUtil.unlockSlot(event.entityPlayer);
    }

    // Drops items if they somehow got into a locked slot.
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.worldObj.isRemote) {
            for (int i = 0; i < event.player.inventory.getSizeInventory() - 4; i++) {
                ItemStack stack = event.player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() != null) {
                    if (!SimpleUtil.slotUnlocked(i, event.player)) {
                        event.player.entityDropItem(stack, 0);
                        event.player.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
        }
    }

    // Prevents items from being picked up if there are not empty unlocked slots
    @SubscribeEvent
    public void itemPickupEvent(EntityItemPickupEvent event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            ItemStack stack = event.item.getEntityItem();
            if (stack != null && stack.getItem() != null && stack.stackSize > 0) {
                int slot = SimpleUtil.searchForPossibleSlot(stack, event.entityPlayer.inventory);
                event.setCanceled(slot == -1 || !SimpleUtil.slotUnlocked(slot, event.entityPlayer));
            }
        }
    }
}
