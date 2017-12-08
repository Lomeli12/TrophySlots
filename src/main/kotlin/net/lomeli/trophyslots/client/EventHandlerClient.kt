package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
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

@SideOnly(Side.CLIENT) object EventHandlerClient {
    val resourceFile = ResourceLocation(TrophySlots.MOD_ID + ":textures/cross.png")

    @SubscribeEvent fun postDrawGuiEvent(event: GuiScreenEvent.DrawScreenEvent.Post) {
        if (event.gui != null && event.gui is GuiContainer) {
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.snowFlakeRenderer(event.gui)
        }
    }

    @SubscribeEvent fun guiPostInit(event: GuiScreenEvent.InitGuiEvent.Post) {
        val mc = FMLClientHandler.instance().client
        if (event.gui is GuiContainer) {
            val gui = event.gui as GuiContainer
            if (GuiEffectRenderer.validDate())
                GuiEffectRenderer.clearPrevList()
            if (mc.player != null) {
                val slotInfo = SlotManager.getPlayerSlotInfo(mc.player)!!
                if (!mc.player.capabilities.isCreativeMode && !slotInfo.isAtMaxSlots())
                    event.buttonList.add(GuiLockedSlot(gui))
            }
        }
    }

    @SubscribeEvent fun openGuiEvent(event: GuiOpenEvent) {
        val mc = FMLClientHandler.instance().client
        if (mc.player != null && event.gui is GuiContainer) {
            val gui = event.gui as GuiContainer
            val slotInfo = SlotManager.getPlayerSlotInfo(mc.player)!!
            if (!GuiEffectRenderer.validDate())
                GuiEffectRenderer.clearPrevList()
            if (!mc.player.capabilities.isCreativeMode && !slotInfo.isAtMaxSlots() && !CompatManager.useCompatReplace(gui, mc.player)) {
                var slotList = gui.inventorySlots.inventorySlots
                if (slotList != null && slotList.size > 0) {
                    for (i in slotList.indices) {
                        var slot = gui.inventorySlots.getSlot(i)
                        if (slot != null && slot.isHere(mc.player.inventory, slot.slotIndex) && !slotInfo.slotUnlocked(slot.slotIndex)) {
                            var replacementSlot = SlotLocked(mc.player.inventory, slot.slotIndex, slot.xPos, slot.yPos)
                            gui.inventorySlots.inventorySlots.set(i, replacementSlot)
                        }
                    }
                }
            }
        }
    }
}