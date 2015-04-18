package net.lomeli.trophyslots.client.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        return null;
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b() {
        return false;
    }
}
