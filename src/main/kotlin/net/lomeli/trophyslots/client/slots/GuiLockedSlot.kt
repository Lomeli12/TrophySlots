package net.lomeli.trophyslots.client.slots

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.EventHandlerClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
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
                drawGradientRect(gui.guiLeft + xPosition, gui.guiTop + yPosition, 300f, gui.guiLeft + xPosition + 16, gui.guiTop + yPosition + 16, 2130706433, 2130706433)
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

    private fun drawGradientRect(par1: Int, par2: Int, z: Float, par3: Int, par4: Int, par5: Int, par6: Int) {
        val var7 = (par5 shr 24 and 255) / 255f
        val var8 = (par5 shr 16 and 255) / 255f
        val var9 = (par5 shr 8 and 255) / 255f
        val var10 = (par5 and 255) / 255f
        val var11 = (par6 shr 24 and 255) / 255f
        val var12 = (par6 shr 16 and 255) / 255f
        val var13 = (par6 shr 8 and 255) / 255f
        val var14 = (par6 and 255) / 255f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        val var15 = Tessellator.getInstance()
        val var16 = var15.worldRenderer
        var16.startDrawingQuads()
        var16.setColorRGBA_F(var8, var9, var10, var7)
        var16.addVertex(par3.toDouble(), par2.toDouble(), z.toDouble())
        var16.addVertex(par1.toDouble(), par2.toDouble(), z.toDouble())
        var16.setColorRGBA_F(var12, var13, var14, var11)
        var16.addVertex(par1.toDouble(), par4.toDouble(), z.toDouble())
        var16.addVertex(par3.toDouble(), par4.toDouble(), z.toDouble())
        var15.draw()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.disableBlend()
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GlStateManager.enableTexture2D()
    }
}
