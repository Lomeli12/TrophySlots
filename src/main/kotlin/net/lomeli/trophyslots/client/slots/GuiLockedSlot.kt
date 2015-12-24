package net.lomeli.trophyslots.client.slots

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.EventHandlerClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11

class GuiLockedSlot(val gui: GuiContainer) : GuiButton(-111, 0, 0, 16, 16, "") {
    init {
        this.enabled = false
    }

    fun drawLockedSlot(mc: Minecraft, slotX: Int, slotY: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        if (TrophySlots.slotRenderType == 1 || TrophySlots.slotRenderType == 2) {
            GlStateManager.colorMask(true, true, true, false)
            gui.drawGradientRect(gui.guiLeft + slotX, gui.guiTop + slotY, gui.guiLeft + slotX + 16, gui.guiTop + slotY + 16, 2130706433, 2130706433)
            GlStateManager.colorMask(true, true, true, true)
        }
        if (TrophySlots.slotRenderType == 0 || TrophySlots.slotRenderType == 2) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            mc.renderEngine.bindTexture(EventHandlerClient.resourceFile)
            gui.drawTexturedModalRect(gui.guiLeft + slotX, gui.guiTop + slotY, 0, 0, 16, 16)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        }
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        if (this.visible) {
            var slotList = gui.inventorySlots.inventorySlots;
            if (slotList != null && slotList.size() > 0) {
                for (i in slotList.indices) {
                    var slot = gui.inventorySlots.getSlot(i)
                    if (slot != null && slot.isHere(mc.thePlayer.inventory, slot.slotIndex) && !TrophySlots.proxy!!.slotUnlocked(slot.slotIndex))
                        drawLockedSlot(mc, slot.xDisplayPosition, slot.yDisplayPosition)
                }
            }
        }
    }
}