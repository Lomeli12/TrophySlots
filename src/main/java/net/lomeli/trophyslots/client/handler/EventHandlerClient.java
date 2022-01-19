package net.lomeli.trophyslots.client.handler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.screen.LockedSlotScreen;
import net.lomeli.trophyslots.client.screen.special.SpecialScreenRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, value = Dist.CLIENT)
public class EventHandlerClient {
    private static final SpecialScreenRenderer specialScreenRenderer = new SpecialScreenRenderer();

    @SubscribeEvent
    public static void onPreScreenInit(ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof AbstractContainerScreen)
            event.addListener(new LockedSlotScreen((AbstractContainerScreen<?>) screen));
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenOpenEvent event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen))
            specialScreenRenderer.clearFlakes();
    }

    @SubscribeEvent
    public static void onPostScreenDraw(ScreenEvent.DrawScreenEvent.Post event) {
        if (ClientConfig.special && event.getScreen() instanceof ContainerScreen && specialScreenRenderer.isSpecialDay())
            specialScreenRenderer.tick(event.getScreen());
    }
}
