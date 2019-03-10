package net.lomeli.trophyslots.client;

import net.lomeli.knit.config.Config;

public class ClientConfig {

    // Client
    @Config(categoryComment = "Client-side only configs. Ignore on server-side.",
            comment = "Render settings for locked slots. 0 = Crossed out; 1 = Grayed out; 2 = Grayed and crossed out; 3 = no special rendering.")
    public static int slotRenderType = 0;
    @Config(configName = "special")
    public static boolean special = true;
}
