package net.lomeli.trophyslots.compat;

import appeng.api.config.Settings;
import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.container.slot.SlotPlayerHotBar;
import appeng.container.slot.SlotPlayerInv;
import appeng.core.AEConfig;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import net.minecraftforge.client.event.GuiScreenEvent;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.slots.ae2.SlotLockedPlayerHotbar;
import net.lomeli.trophyslots.client.slots.ae2.SlotLockedPlayerInv;

public class AE2Mod implements ICompatModule {
    private Object aeStyle, prevStyle, searchMode, prevSearchMode;

    public AE2Mod() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void tickHandler(TickEvent.ClientTickEvent event) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (event.phase == TickEvent.Phase.END && mc.currentScreen != null && mc.currentScreen instanceof GuiContainer) {
            GuiContainer gui = (GuiContainer) mc.currentScreen;
            if (isCompatibleGui(gui)) {
                GuiMEMonitorable me = (GuiMEMonitorable) gui;
                if (aeStyle == null) {
                    aeStyle = AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
                    prevStyle = AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
                }
                if (searchMode == null) {
                    searchMode = AEConfig.instance.getConfigManager().getSetting(Settings.SEARCH_MODE);
                    prevSearchMode = AEConfig.instance.getConfigManager().getSetting(Settings.SEARCH_MODE);
                }
                prevStyle = aeStyle;
                prevSearchMode = searchMode;
                aeStyle = AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
                searchMode = AEConfig.instance.getConfigManager().getSetting(Settings.SEARCH_MODE);
                if (aeStyle != prevStyle || searchMode != prevSearchMode)
                    TrophySlots.proxy.eventHandlerClient.guiPostInit(new GuiScreenEvent.InitGuiEvent.Post(me, me.buttonList));
            }
        }
    }

    public void replaceSlots(GuiContainer container, EntityPlayer player) {
        GuiMEMonitorable gui = (GuiMEMonitorable) container;
        List slotList = gui.inventorySlots.inventorySlots;
        if (slotList != null) {
            for (int i = 0; i < slotList.size(); i++) {
                Slot slot = gui.inventorySlots.getSlot(i);
                if (slot != null && slot.isSlotInInventory(player.inventory, slot.getSlotIndex())) {
                    if (!TrophySlots.proxy.slotUnlocked(slot.getSlotIndex())) {
                        Slot replacementSlot = getSlot(player, slot);
                        gui.inventorySlots.inventorySlots.set(i, replacementSlot);
                    }
                }
            }
        }
    }

    public Slot getSlot(EntityPlayer player, Slot slot) {
        return slot instanceof SlotPlayerInv ? SlotLockedPlayerInv.getSlot(player, slot) : slot instanceof SlotPlayerHotBar ? SlotLockedPlayerHotbar.getSlot(player, slot) : null;
    }

    public boolean isCompatibleGui(GuiContainer gui) {
        return gui instanceof GuiMEMonitorable;
    }
}