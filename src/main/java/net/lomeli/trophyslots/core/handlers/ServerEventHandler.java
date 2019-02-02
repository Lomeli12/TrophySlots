package net.lomeli.trophyslots.core.handlers;

import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageReloadConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerEventHandler {

    public static void onPlayerLoggedIn(ServerPlayerEntity player) {
        if (player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked(), ModConfig.reverseOrder), player);
        }
    }

    public static void onPlayerLoggedOut(ServerPlayerEntity player) {
        MessageUtil.sendToClient(new MessageReloadConfig(), player);
    }
}
