package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.slots.GuiLockedSlot
import net.lomeli.trophyslots.client.slots.SlotLocked
import net.lomeli.trophyslots.compat.CompatManager
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT) public object EventHandlerClient {
    public val resourceFile = ResourceLocation(TrophySlots.MOD_ID + ":textures/cross.png")

    @SubscribeEvent public fun postDrawGuiEvent(event: GuiScreenEvent.DrawScreenEvent.Post) {
        if (event.gui != null && event.gui is GuiContainer) {
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.snowFlakeRenderer(event.gui)
        }
    }

    @SubscribeEvent public fun guiPostInit(event: GuiScreenEvent.InitGuiEvent.Post) {
        val mc = FMLClientHandler.instance().client;
        if (event.gui != null && event.gui is GuiContainer) {
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.clearPrevList()
            if (!mc.thePlayer.capabilities.isCreativeMode && !TrophySlots.proxy!!.hasUnlockedAllSlots()) {
                val gui = event.gui
                var slotList = gui.inventorySlots.inventorySlots;
                if (slotList != null && slotList.size() > 0) {
                    for(i in slotList.indices) {
                        var slot = gui.inventorySlots.getSlot(i)
                        if (slot != null && slot.isHere(mc.thePlayer.inventory, slot.slotIndex) && !TrophySlots.proxy!!.slotUnlocked(slot.slotIndex))
                            event.buttonList.add(GuiLockedSlot(slot.xDisplayPosition, slot.yDisplayPosition, gui, slot.slotIndex))
                    }
                }
            }
        }
    }

    @SubscribeEvent public fun openGuiEvent(event: GuiOpenEvent) {
        val mc = FMLClientHandler.instance().client;
        if (event.gui != null && event.gui is GuiContainer) {
            val gui = event.gui
            if (!GuiEffectRenderer.validDate())
                GuiEffectRenderer.clearPrevList()
            if (!mc.thePlayer.capabilities.isCreativeMode && !TrophySlots.proxy!!.hasUnlockedAllSlots() && !CompatManager.useCompatReplace(gui, mc.thePlayer)) {
                var slotList = gui.inventorySlots.inventorySlots
                if (slotList != null && slotList.size() > 0) {
                    for(i in slotList.indices) {
                        var slot = gui.inventorySlots.getSlot(i)
                        if (slot != null && slot.isHere(mc.thePlayer.inventory, slot.slotIndex) && !TrophySlots.proxy!!.slotUnlocked(slot.slotIndex)) {
                            var replacementSlot = SlotLocked(mc.thePlayer.inventory, slot.slotIndex, slot.xDisplayPosition, slot.yDisplayPosition)
                            gui.inventorySlots.inventorySlots.set(i, replacementSlot)
                        }
                    }
                }
            }
        }
    }
}