package net.lomeli.trophyslots.client.handler;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.screen.LockedSlotScreen;
import net.lomeli.trophyslots.client.screen.special.SpecialScreenRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, value = Dist.CLIENT)
public class EventHandlerClient {
    private static SpecialScreenRenderer specialScreenRenderer = new SpecialScreenRenderer();

    @SubscribeEvent
    public static void onPreScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof ContainerScreen)
            event.addWidget(new LockedSlotScreen((ContainerScreen<?>) screen));
    }


    @SubscribeEvent
    public static void onScreenOpen(GuiOpenEvent event) {
        TrophySlots.log.info("Screen: {}", event.getGui());
        if (!(event.getGui() instanceof ContainerScreen))
            specialScreenRenderer.clearFlakes();
    }

    @SubscribeEvent
    public static void onPostScreenDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (ClientConfig.special && event.getGui() instanceof ContainerScreen && specialScreenRenderer.isSpecialDay()) {
            specialScreenRenderer.tick(event.getGui());
        }
    }
}
