package net.lomeli.trophyslots.client.screen.special;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class SnowFlake {
    private static final Random rand = new Random();
    private float weight;
    private int xPos;
    private int yPos;
    private float alpha;

    SnowFlake(int screenWidth) {
        this((int) (Math.random() * screenWidth), 0 - (int) Math.floor(16 * rand.nextFloat()), (float) (Math.random() / 2) + 1f);
    }

    private SnowFlake(int x, int y, float weight) {
        this.xPos = x;
        this.yPos = y;
        this.alpha = 0.0001f + rand.nextFloat();
        this.weight = weight;
    }

    public void render(Screen screen) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();


        GlStateManager.color4f(1f, 1f, 1f, alpha);
        Sprite snowflake = MinecraftClient.getInstance().getSpriteAtlas().getSprite(SpriteHandler.SNOWFLAKE);
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        //WTF does "blit" mean?!!
        screen.blit(this.xPos, this.yPos, snowflake, 6, 6);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
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

    int getYPos() {
        return yPos;
    }

    void setXPos(int pos) {
        this.xPos = pos;
    }
}
