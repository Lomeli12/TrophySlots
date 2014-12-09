package net.lomeli.trophyslots.core;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

import net.lomeli.trophyslots.TrophySlots;

public class SimpleUtil {
    /**
     * Gets the number of slots the player has unlocked.
     *
     * @return
     */
    public static int getSlotsUnlocked(EntityPlayer player) {
        int i = 0;
        EntityPlayer pl = getPlayerMP(player);
        if (pl != null) {
            if (pl.getEntityData().hasKey(TrophySlots.slotsUnlocked))
                i = pl.getEntityData().getInteger(TrophySlots.slotsUnlocked);
        }
        return i;
    }

    public static boolean doStackMatch(ItemStack stack1, ItemStack stack2) {
        boolean flag = false;
        if (stack1 != null && stack1.getItem() != null && stack2 != null && stack2.getItem() != null)
            flag = stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
        return flag;
    }

    public static int searchForPossibleSlot(ItemStack stack, InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < inventoryPlayer.getSizeInventory() - 4; i++) {
            ItemStack item = inventoryPlayer.getStackInSlot(i);
            if (item == null || item.getItem() == null)
                return i;
            else if (doStackMatch(stack, item) && (item.stackSize + stack.stackSize) < item.getMaxStackSize())
                return i;
        }
        return -1;
    }

    /**
     * Check if slot index is unlocked
     *
     * @param i - inventory index corresponding with the slot
     * @return slot's unlock status
     */
    public static boolean slotUnlocked(int i, EntityPlayer player) {
        return !(i >= TrophySlots.startingSlots + getSlotsUnlocked(player) && i < player.inventory.getSizeInventory() - 4);
    }

    /**
     * Unlocks slot, if player hasn't reached max number of slots
     *
     * @return unlocked a slot for the player
     */
    public static boolean unlockSlot(EntityPlayer player) {
        int i = player.getEntityData().getInteger(TrophySlots.slotsUnlocked);
        if (i + TrophySlots.startingSlots < player.inventory.getSizeInventory() - 4) {
            TrophySlots.log(0, "Awarding slot to " + player.getCommandSenderName());
            i++;
            player.getEntityData().setInteger(TrophySlots.slotsUnlocked, i);

            EntityPlayerMP playerMP = getPlayerMP(player);
            if (i >= 1 && !playerMP.func_147099_x().hasAchievementUnlocked(TrophySlots.firstSlot))
                playerMP.addStat(TrophySlots.firstSlot, 1);
            if (i + TrophySlots.startingSlots >= (player.inventory.getSizeInventory() - 4) && playerMP.func_147099_x().canUnlockAchievement(TrophySlots.maxCapcity)) {
                if (!playerMP.func_147099_x().hasAchievementUnlocked(TrophySlots.maxCapcity))
                    playerMP.addStat(TrophySlots.maxCapcity, 1);
            }
            TrophySlots.proxy.markContainerUpdate();
            displayMessage(player);
            return true;
        }
        return false;
    }

    public static String translate(String unlocalizedText) {
        return StatCollector.translateToLocal(unlocalizedText);
    }

    /**
     * Diplay simple "you unlocked a slot" message
     */
    public static void displayMessage(EntityPlayer player) {
        String msg = translate(TrophySlots.unlockMessage);
        if (player.getUniqueID().toString().equals("0b7509f0-2458-4160-9ce1-2772b9a45ac2"))
            msg += " §dOink!§r";
        player.addChatComponentMessage(new ChatComponentText(msg));
    }

    public static EntityPlayerMP getPlayerMP(EntityPlayer player) {
        return (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID());
    }

    public static String nameFromStack(ItemStack stack) {
        try {
            ModContainer mod = GameData.findModOwner(GameData.getItemRegistry().getNameForObject(stack.getItem()));
            String modname = mod == null ? "Minecraft" : mod.getName();
            return modname;
        } catch (NullPointerException var3) {
            return "";
        }
    }

    public static boolean safeKeyDown(int keyCode) {
        try {
            return Keyboard.isKeyDown(keyCode);
        } catch (IndexOutOfBoundsException var2) {
            return false;
        }
    }
}
