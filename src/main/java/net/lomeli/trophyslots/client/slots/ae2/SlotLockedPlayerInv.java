package net.lomeli.trophyslots.client.slots.ae2;

import appeng.container.slot.SlotPlayerInv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotLockedPlayerInv extends SlotPlayerInv {
    public SlotLockedPlayerInv(IInventory iInventory, int slot, int x, int y) {
        super(iInventory, slot, x, y);
    }

    public static Slot getSlot(EntityPlayer player, Slot old) {
        return new SlotLockedPlayerInv(player.inventory, old.getSlotIndex(), old.xDisplayPosition, old.yDisplayPosition);
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