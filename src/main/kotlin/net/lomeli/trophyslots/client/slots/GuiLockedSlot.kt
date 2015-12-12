package net.lomeli.trophyslots.client.slots

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.EventHandlerClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11

class GuiLockedSlot(x: Int, y: Int, private val gui: GuiContainer, private val slot: Int) : GuiButton(0, x, y, 16, 16, "") {
    init {
        this.enabled = false
    }

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        if (this.visible && !TrophySlots.proxy!!.slotUnlocked(this.slot)) {
            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()
            OpenGlHelper.glBlendFunc(770, 771, 1, 0)
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            if (TrophySlots.slotRenderType == 1 || TrophySlots.slotRenderType == 2) {
                GlStateManager.colorMask(true, true, true, false)
                this.drawGradientRect(gui.guiLeft + xPosition, gui.guiTop + yPosition, gui.guiLeft + xPosition + 16, gui.guiTop + yPosition + 16, 2130706433, 2130706433)
                GlStateManager.colorMask(true, true, true, true)
            }
            if (TrophySlots.slotRenderType == 0 || TrophySlots.slotRenderType == 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
                mc.renderEngine.bindTexture(EventHandlerClient.resourceFile)
                drawTexturedModalRect(gui.guiLeft + xPosition, gui.guiTop + yPosition, 0, 0, 16, 16)
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            }
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}
