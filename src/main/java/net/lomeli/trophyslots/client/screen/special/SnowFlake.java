package net.lomeli.trophyslots.client.screen.special;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.lomeli.trophyslots.client.handler.SpriteHandler;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
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
        TextureAtlasSprite snowflake = Minecraft.getInstance()
                .getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(SpriteHandler.SNOWFLAKE);
        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        //WTF does "blit" mean?!!
        screen.blit(this.xPos, this.yPos, 300, 6, 6, snowflake);

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

    void setXPos(int pos) {
        this.xPos = pos;
    }

    int getYPos() {
        return yPos;
    }
}
