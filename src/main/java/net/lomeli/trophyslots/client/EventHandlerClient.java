package net.lomeli.trophyslots.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.GuiScreenEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SimpleUtil;

public class EventHandlerClient {
    // Renders cross over locked slots
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen != null && mc.currentScreen instanceof GuiContainer && !mc.thePlayer.capabilities.isCreativeMode) {
                GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
                int xBase = guiContainer.guiLeft;
                int yBase = guiContainer.guiTop;
                List slotList = guiContainer.inventorySlots.inventorySlots;
                if (slotList != null) {
                    for (int i = 0; i < slotList.size(); i++) {
                        Slot slot = guiContainer.inventorySlots.getSlot(i);
                        if (slot != null && slot.isSlotInInventory(mc.thePlayer.inventory, slot.getSlotIndex())) {
                            if (!SimpleUtil.slotUnlocked(slot.getSlotIndex(), mc.thePlayer)) {
                                mc.renderEngine.bindTexture(new ResourceLocation(TrophySlots.MOD_ID.toLowerCase() + ":textures/cross.png"));
                                guiContainer.drawTexturedModalRect(xBase + slot.xDisplayPosition, yBase + slot.yDisplayPosition, 0, 0, 16, 16);
                            }
                        }
                    }
                }
            }
        }
    }

    // Disables slots in guis
    @SubscribeEvent
    public void preDrawGuiEvent(GuiScreenEvent.DrawScreenEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.gui instanceof GuiContainer) {
            GuiContainer guiContainer = (GuiContainer) event.gui;
            List slotList = guiContainer.inventorySlots.inventorySlots;
            if (slotList != null) {
                for (int i = 0; i < slotList.size(); i++) {
                    Slot slot = guiContainer.inventorySlots.getSlot(i);
                    if (slot != null && slot.isSlotInInventory(mc.thePlayer.inventory, slot.getSlotIndex())) {
                        if (!SimpleUtil.slotUnlocked(slot.getSlotIndex(), mc.thePlayer))
                            ((GuiContainer) event.gui).inventorySlots.inventorySlots.set(i, new SlotLocked(mc.thePlayer.inventory, slot.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition));
                    }
                }
            }
        }
    }
}
