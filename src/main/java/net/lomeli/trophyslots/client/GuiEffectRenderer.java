package net.lomeli.trophyslots.client;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.client.event.GuiScreenEvent;

import net.lomeli.trophyslots.TrophySlots;

/**
 * Really cheesy poorly done snow effect =P
 */
public class GuiEffectRenderer {
    private static Random rand = new Random();
    private static Minecraft mc = Minecraft.getMinecraft();
    private static int snowMax = 750;
    private static int renderTick;
    private static List<SnowFlake> snowFlakeList;

    public static void snowFlakeRenderer(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (snowFlakeList.isEmpty()) {
            for (int i = 0; i < snowMax; i++)
                snowFlakeList.add(new SnowFlake(6 + rand.nextInt(mc.displayWidth), 1));
        }
        for (int i = 0; i < snowFlakeList.size(); i++) {
            SnowFlake snowFlake = snowFlakeList.get(i);
            if (snowFlake != null) {
                if (snowFlake.y < 0) {
                    if (rand.nextInt(50) < 1)
                        snowFlake.draw(event.gui);
                } else
                    snowFlake.draw(event.gui);
            }
        }
        renderTick++;
    }

    public static boolean validDate() {
        return TrophySlots.xmas && Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25;
    }

    public static void clearPrevList() {
        renderTick = 0;
        if (snowFlakeList == null)
            snowFlakeList = new ArrayList<SnowFlake>();
        else
            snowFlakeList.clear();
    }

    public static class SnowFlake {
        private int startX, x;
        public int y;
        private int speed;
        private float alpha;

        public SnowFlake(int x, int speed) {
            this.startX = x;
            this.x = x;
            this.y = -20 + rand.nextInt(15);
            this.speed = speed;
            this.alpha = 0.0001f + rand.nextFloat();
        }

        public void draw(GuiScreen gui) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, alpha);
            mc.renderEngine.bindTexture(EventHandlerClient.resourceFile);
            gui.drawTexturedModalRect(x, y, 16, 0, 6, 6);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();



            if (renderTick % 2 == 0) {
                y += speed;
                if (rand.nextBoolean())
                    x += rand.nextBoolean() ? 1 : -1;
            }
            if (y > gui.height) {
                x = startX + (rand.nextInt(11) - 5);
                y = -20 + rand.nextInt(15);
            }
        }
    }
}
