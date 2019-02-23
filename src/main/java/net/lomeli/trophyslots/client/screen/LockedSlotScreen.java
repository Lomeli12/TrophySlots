package net.lomeli.trophyslots.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.lomeli.trophyslots.client.accessors.IContainerScreenAccessor;
import net.lomeli.trophyslots.client.accessors.ISlotAccessor;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class LockedSlotScreen extends ButtonWidget {
    private static final int GREY_COLOR = 2130706433;
    private final ContainerScreen parentScreen;

    public LockedSlotScreen(ContainerScreen parentScreen) {
        super(0, 0, 0, 0, "");
        this.enabled = false;
        this.parentScreen = parentScreen;
    }

    @Override
    public void draw(int mouseX, int mouseY, float renderTick) {
        if (!visible) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        //TODO: Make this not an eyesore.
        for (Slot slot : parentScreen.getContainer().slotList) {
            if (slot.inventory instanceof PlayerInventory) {
                PlayerEntity player = ((PlayerInventory) slot.inventory).player;
                if (!player.abilities.creativeMode && player instanceof ISlotHolder) {
                    PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                    if (slot instanceof ISlotAccessor) {
                        int index = ((ISlotAccessor) slot).getSlotIndex();
                        if (!slotManager.slotUnlocked(index))
                            drawLockedSlot(mc, slot.xPosition, slot.yPosition);
                    }
                }
            }
        }
    }

    private void drawLockedSlot(MinecraftClient mc, int xPos, int yPos) {
        if (parentScreen instanceof IContainerScreenAccessor) {
            int left = ((IContainerScreenAccessor) parentScreen).getLeft();
            int top = ((IContainerScreenAccessor) parentScreen).getTop();
            int x = left + xPos;
            int y = top + yPos;

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (ModConfig.slotRenderType == 1 || ModConfig.slotRenderType == 2) {
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(x, y, x + 16, y + 16, GREY_COLOR, GREY_COLOR);
                GlStateManager.colorMask(true, true, true, true);
            }
            if (ModConfig.slotRenderType == 0 || ModConfig.slotRenderType == 2) {
                Sprite crossSprite = mc.getSpriteAtlas().getSprite(SpriteHandler.CROSS_SPRITE);
                GlStateManager.disableLighting();
                GlStateManager.color4f(1f, 1f, 1f, 1f);
                mc.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
                this.drawTexturedRect(x, y, crossSprite, 16, 16);
                GlStateManager.enableLighting();
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
