package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.fml.client.FMLClientHandler
import java.util.*

public object GuiEffectRenderer {
    val rand = Random()
    val mc = FMLClientHandler.instance().client;
    val snowMax = 750
    var renderTick = 0
    val snowFlakeList = ArrayList<SnowFlake>()

    public fun snowFlakeRenderer(gui: GuiScreen) {
        if (snowFlakeList.isEmpty()) {
            var i = 0
            while (i < snowMax) {
                snowFlakeList.add(SnowFlake(6 + rand.nextInt(mc.displayHeight), 1))
                i++
            }
        }
        for (i in snowFlakeList.indices) {
            val snowFlake = snowFlakeList.get(i)
            if (snowFlake.y < 0) {
                if (rand.nextInt(50) < 1)
                    snowFlake.draw(gui)
            } else
                snowFlake.draw(gui)
        }
        renderTick++
    }

    public fun validDate(): Boolean = TrophySlots.xmas && Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25

    public fun clearPrevList() {
        if (snowFlakeList.size() > 0) {
            renderTick = 0
            snowFlakeList.clear()
        }
    }

    public class SnowFlake(var x: Int, val speed: Int) {
        public var y = 0
        private var startX = 0
        private var alpha = 0f

        init {
            this.startX = x
            this.y = -20 + rand.nextInt(15)
            this.alpha = 0.0001f + rand.nextFloat()
        }

        public fun draw(gui: GuiScreen) {
            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()
            GlStateManager.color(1f, 1f, 1f, alpha)
            mc.renderEngine.bindTexture(EventHandlerClient.resourceFile)
            gui.drawTexturedModalRect(x, y, 16, 0, 6, 6)
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()

            if (renderTick % 2 == 0) {
                y += speed
                if (rand.nextBoolean()) {
                    if (rand.nextBoolean())
                        x += 1
                    else
                        x -= 1
                }
            }
            if (y > gui.height) {
                x = startX + (rand.nextInt(11) - 5)
                y = -20 + rand.nextInt(15)
            }
        }
    }
}