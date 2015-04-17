package net.lomeli.trophyslots.client.slots.workshop;

import vswe.production.gui.GuiTable;
import vswe.production.gui.container.slot.SlotPlayer;
import vswe.production.tileentity.TileEntityTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotLockedPlayer extends SlotPlayer {
    public SlotLockedPlayer(IInventory inventory, TileEntityTable table, int id, int x, int y) {
        super(inventory, table, id, x, y);
    }

    public static SlotLockedPlayer getSlot(Minecraft mc, GuiContainer gui, Slot oldSlot) {
        SlotPlayer playerSlot = (SlotPlayer) oldSlot;
        GuiTable tableGui = (GuiTable) gui;
        return new SlotLockedPlayer(mc.thePlayer.inventory, tableGui.getTable(), playerSlot.getSlotIndex(), playerSlot.xDisplayPosition, playerSlot.yDisplayPosition);
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
