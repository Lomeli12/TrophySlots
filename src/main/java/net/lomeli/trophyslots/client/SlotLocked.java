package net.lomeli.trophyslots.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A slot that cannot be edited in anyway via GUIs. {@link net.lomeli.trophyslots.core.handler.EventHandler} will
 * handle any items placed into locked slots.
 */
public class SlotLocked extends Slot {
    public SlotLocked(IInventory iInventory, int slot, int x, int y) {
        super(iInventory, slot, x, y);
    }

    @Override
    public void putStack(ItemStack stack) {
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
    public boolean canBeHovered() {
        return false;
    }
}
