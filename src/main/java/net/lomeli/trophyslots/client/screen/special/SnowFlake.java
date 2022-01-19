package net.lomeli.trophyslots.client.screen.special;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Random;

public class SnowFlake {
    private static final Random rand = new Random();
    private final float weight;
    private final float alpha;
    private int xPos;
    private int yPos;

    SnowFlake(int screenWidth) {
        this((int) (Math.random() * screenWidth), -(int) Math.floor(16 * rand.nextFloat()), (float) (Math.random() / 2) + 1f);
    }

    private SnowFlake(int x, int y, float weight) {
        this.xPos = x;
        this.yPos = y;
        this.alpha = 0.0001f + rand.nextFloat();
        this.weight = weight;
    }

    public void render() {
        PoseStack matrix = new PoseStack();
        matrix.pushPose();
        GlStateManager._enableBlend();

        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
        TextureAtlasSprite snowflake = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(SpriteHandler.SNOWFLAKE);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        GuiComponent.blit(matrix, this.xPos, this.yPos, 300, 6, 6, snowflake);

        GlStateManager._disableBlend();
        matrix.popPose();
    }

    public void update(Wind wind, float gravity) {
        float downwardMotion = this.weight * gravity;
        float windMotion = wind.getSpeed() / this.weight;

        this.yPos += downwardMotion + (rand.nextFloat() * (rand.nextBoolean() ? -1 : 1));
        this.xPos += windMotion + (rand.nextFloat() * (rand.nextBoolean() ? -1 : 1));
    }

    int getXPos() {
        return xPos;
    }

    void setXPos(int pos) {
        this.xPos = pos;
    }

    int getYPos() {
        return yPos;
    }
}
