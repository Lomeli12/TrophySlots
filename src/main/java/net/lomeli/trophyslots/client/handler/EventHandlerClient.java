package net.lomeli.trophyslots.client.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.client.event.ClientDisconnectCallback;
import net.lomeli.knit.client.event.OpenScreenCallback;
import net.lomeli.knit.client.event.PostScreenDrawCallback;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.screen.special.SpecialScreenRenderer;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    private static SpecialScreenRenderer specialScreenRenderer = new SpecialScreenRenderer();

    public static void initClientEvents() {
        ClientDisconnectCallback.EVENT.register(() -> TrophySlots.config.loadConfig());
        PostScreenDrawCallback.EVENT.register((screen, mouseX, mouseY, lastFrameDuration) -> postScreenDraw(screen));
        OpenScreenCallback.EVENT.register(EventHandlerClient::openScreen);
    }

    private static boolean openScreen(Screen screen) {
        if (!(screen instanceof ContainerScreen))
            specialScreenRenderer.clearFlakes();
        return false;
    }

    private static void postScreenDraw(Screen screen) {
        if (ClientConfig.special && specialScreenRenderer.isSpecialDay() && screen instanceof ContainerScreen)
            specialScreenRenderer.tick(screen);
    }
}
