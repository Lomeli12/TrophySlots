package net.lomeli.trophyslots.client.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.TrophySlots;

/**
 * A slot that cannot be edited in anyway via GUIs. {@link net.lomeli.trophyslots.core.handler.EventHandlerServer} will
 * handle any items placed into locked slots.
 */
public class SlotLocked extends Slot {
    public SlotLocked(IInventory iInventory, int slot, int x, int y) {
        super(iInventory, slot, x, y);
    }

    public static Slot getSlot(EntityPlayer player, Slot old) {
        return new SlotLocked(player.inventory, old.getSlotIndex(), old.xDisplayPosition, old.yDisplayPosition);
    }

    @Override
    public ItemStack decrStackSize(int par1) {
        return TrophySlots.proxy.slotUnlocked(this.getSlotIndex()) ? super.decrStackSize(par1) : null;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TrophySlots.proxy.slotUnlocked(this.getSlotIndex()) ? super.isItemValid(stack) : false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return TrophySlots.proxy.slotUnlocked(this.getSlotIndex()) ? super.canTakeStack(player) : false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b() {
        return TrophySlots.proxy.slotUnlocked(this.getSlotIndex()) ? super.func_111238_b() : false;
    }
}
