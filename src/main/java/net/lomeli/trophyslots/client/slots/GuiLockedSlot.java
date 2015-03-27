package net.lomeli.trophyslots.client.slots;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.EventHandlerClient;

public class GuiLockedSlot extends GuiButton {

    public GuiLockedSlot(int id, int x, int y) {
        super(id, x, y, 16, 16, "");
        this.enabled = false;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (TrophySlots.slotRenderType == 0 || TrophySlots.slotRenderType == 2) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
                mc.renderEngine.bindTexture(EventHandlerClient.resourceFile);
                drawTexturedModalRect(xPosition, yPosition, 0, 0, 16, 16);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
            }
            if (TrophySlots.slotRenderType == 1 || TrophySlots.slotRenderType == 2) {
                GL11.glColorMask(true, true, true, false);
                drawGradientRect(xPosition, yPosition, 300f, xPosition + 16, yPosition + 16, 2130706433, 2130706433);
                GL11.glColorMask(true, true, true, true);
            }
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private void drawGradientRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
        float var7 = (par5 >> 24 & 255) / 255F;
        float var8 = (par5 >> 16 & 255) / 255F;
        float var9 = (par5 >> 8 & 255) / 255F;
        float var10 = (par5 & 255) / 255F;
        float var11 = (par6 >> 24 & 255) / 255F;
        float var12 = (par6 >> 16 & 255) / 255F;
        float var13 = (par6 >> 8 & 255) / 255F;
        float var14 = (par6 & 255) / 255F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3, par2, z);
        var15.addVertex(par1, par2, z);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4, z);
        var15.addVertex(par3, par4, z);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
