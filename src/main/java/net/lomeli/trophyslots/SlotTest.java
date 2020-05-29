package net.lomeli.trophyslots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SlotTest {
    public final IInventory inventory;
    private final int slotIndex;
    public int slotNumber;
    public int xPos;
    public int yPos;
    protected String backgroundName = null;
    protected net.minecraft.util.ResourceLocation backgroundLocation = null;
    protected Object backgroundMap;

    public SlotTest(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        this.inventory = inventoryIn;
        this.slotIndex = index;
        this.xPos = xPosition;
        this.yPos = yPosition;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack) {
        if (!net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper.isSlotUnlocked(this.inventory, this.slotIndex)) {
            return false;
        }
        return true;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    public ItemStack decrStackSize(int amount) {
        if (!net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper.isSlotUnlocked(this.inventory, this.slotIndex)) {
            return ItemStack.EMPTY;
        }
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(PlayerEntity playerIn) {
        if (!net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper.isSlotUnlocked(this.inventory, this.slotIndex)) {
            return false;
        }
        return true;
    }

    /**
     * Actualy only call when we want to render the white square effect over the slots. Return always True, except for
     * the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isEnabled() {
        if (!net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper.isSlotUnlocked(this.inventory, this.slotIndex)) {
            return false;
        }
        return true;
    }
}