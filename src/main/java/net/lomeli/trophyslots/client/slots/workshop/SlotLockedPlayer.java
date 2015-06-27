package net.lomeli.trophyslots.client.slots.workshop;

import vswe.production.gui.GuiTable;
import vswe.production.gui.container.slot.SlotPlayer;
import vswe.production.tileentity.TileEntityTable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.TrophySlots;

public class SlotLockedPlayer extends SlotPlayer {
    public SlotLockedPlayer(IInventory inventory, TileEntityTable table, int id, int x, int y) {
        super(inventory, table, id, x, y);
    }

    public static SlotLockedPlayer getSlot(EntityPlayer player, GuiContainer gui, Slot oldSlot) {
        SlotPlayer playerSlot = (SlotPlayer) oldSlot;
        GuiTable tableGui = (GuiTable) gui;
        return new SlotLockedPlayer(player.inventory, tableGui.getTable(), playerSlot.getSlotIndex(), playerSlot.xDisplayPosition, playerSlot.yDisplayPosition);
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
