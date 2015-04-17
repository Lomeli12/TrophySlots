package net.lomeli.trophyslots.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.slots.GuiLockedSlot;
import net.lomeli.trophyslots.client.slots.SlotLocked;
import net.lomeli.trophyslots.compat.CompatManager;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {
    public static final ResourceLocation resourceFile = new ResourceLocation(TrophySlots.MOD_ID + ":textures/cross.png");

    @SubscribeEvent
    public void postDrawGuiEvent(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui != null && event.gui instanceof GuiContainer) {
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.snowFlakeRenderer(event.gui);
        }
    }

    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (event.gui != null && event.gui instanceof GuiContainer) {
            if (!mc.thePlayer.capabilities.isCreativeMode && !TrophySlots.proxy.hasUnlockedAllSlots()) {
                GuiContainer gui = (GuiContainer) event.gui;
                if (gui instanceof GuiInventory) {
                    ContainerPlayer containerPlayer = new ContainerPlayer(mc.thePlayer.inventory, !mc.theWorld.isRemote, mc.thePlayer);
                    List slotList = containerPlayer.inventorySlots;
                    if (slotList != null) {
                        for (int i = 5; i < slotList.size(); i++) {
                            Slot slot = containerPlayer.getSlot(i);
                            if (slot != null && !(slot instanceof SlotCrafting)) {
                                if (slot.inventory != containerPlayer.craftMatrix && !TrophySlots.proxy.slotUnlocked(slot.getSlotIndex()))
                                    event.buttonList.add(new GuiLockedSlot(getUniqueBtnID(event.buttonList), slot.xDisplayPosition, slot.yDisplayPosition, gui));
                            }
                        }
                    }
                } else {
                    List slotList = gui.inventorySlots.inventorySlots;
                    if (slotList != null) {
                        for (int i = 0; i < slotList.size(); i++) {
                            Slot slot = gui.inventorySlots.getSlot(i);
                            if (slot != null && slot.isSlotInInventory(mc.thePlayer.inventory, slot.getSlotIndex())) {
                                if (!TrophySlots.proxy.slotUnlocked(slot.getSlotIndex()))
                                    event.buttonList.add(new GuiLockedSlot(getUniqueBtnID(event.buttonList), slot.xDisplayPosition, slot.yDisplayPosition, gui));
                            }
                        }
                    }
                }
            }
        }
    }

    public int getUniqueBtnID(List<GuiButton> buttonList) {
        int id = 0;
        boolean flag = true;
        while (flag) {
            flag = false;
            for (GuiButton btn : buttonList) {
                if (btn != null && btn.id == id) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                id++;
        }
        return id;
    }

    @SubscribeEvent
    public void openGuiEvent(GuiOpenEvent event) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (event.gui != null && event.gui instanceof GuiContainer) {
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.clearPrevList();
            if (!mc.thePlayer.capabilities.isCreativeMode && !TrophySlots.proxy.hasUnlockedAllSlots()) {
                GuiContainer gui = (GuiContainer) event.gui;
                if (gui instanceof GuiInventory) {
                    ContainerPlayer containerPlayer = new ContainerPlayer(mc.thePlayer.inventory, !mc.theWorld.isRemote, mc.thePlayer);
                    List slotList = containerPlayer.inventorySlots;
                    if (slotList != null) {
                        for (int i = 5; i < slotList.size(); i++) {
                            Slot slot = containerPlayer.getSlot(i);
                            if (slot != null && !(slot instanceof SlotCrafting)) {
                                if (slot.inventory != containerPlayer.craftMatrix && slot.isSlotInInventory(mc.thePlayer.inventory, slot.getSlotIndex()) && !TrophySlots.proxy.slotUnlocked(slot.getSlotIndex()))
                                    containerPlayer.inventorySlots.set(i, SlotLocked.getSlot(mc.thePlayer, slot));
                            }
                        }
                    }
                    gui.inventorySlots = containerPlayer;
                } else {
                    if (CompatManager.ae2Mod != null && CompatManager.ae2Mod.isCompatibleGui(gui))
                        CompatManager.ae2Mod.replaceAESlots(gui, mc.thePlayer);
                    else {
                        List slotList = gui.inventorySlots.inventorySlots;
                        if (slotList != null) {
                            for (int i = 0; i < slotList.size(); i++) {
                                Slot slot = gui.inventorySlots.getSlot(i);
                                if (slot != null && slot.isSlotInInventory(mc.thePlayer.inventory, slot.getSlotIndex())) {
                                    if (!TrophySlots.proxy.slotUnlocked(slot.getSlotIndex())) {
                                        Slot replacementSlot = CompatManager.stWorkshopMod != null ? CompatManager.stWorkshopMod.getSlotForGui(gui, slot) : SlotLocked.getSlot(mc.thePlayer, slot);
                                        gui.inventorySlots.inventorySlots.set(i, replacementSlot);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
