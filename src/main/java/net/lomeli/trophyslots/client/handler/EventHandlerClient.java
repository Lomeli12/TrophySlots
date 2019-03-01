package net.lomeli.trophyslots.client.handler;

import net.lomeli.knit.client.event.ClientDisconnectCallback;
import net.lomeli.knit.client.event.OpenScreenCallback;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.accessors.IScreenAccessor;
import net.lomeli.trophyslots.client.screen.LockedSlotScreen;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;

public class EventHandlerClient {

    public static void initClientEvents() {
        ClientDisconnectCallback.EVENT.register(() -> TrophySlots.config.loadConfig());
        OpenScreenCallback.EVENT.register(EventHandlerClient::onOpeningScreen);
    }

    private static void onOpeningScreen(Screen screen) {
        if (screen instanceof ContainerScreen && screen instanceof IScreenAccessor)
            ((IScreenAccessor) screen).addBtn(new LockedSlotScreen((ContainerScreen) screen));
    }
}
