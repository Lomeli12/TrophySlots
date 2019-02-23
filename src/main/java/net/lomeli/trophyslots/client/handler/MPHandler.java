package net.lomeli.trophyslots.client.handler;

import net.lomeli.trophyslots.TrophySlots;

public class MPHandler {

    public static void onPlayerDisconnect() {
        TrophySlots.config.loadConfig();
    }
}
