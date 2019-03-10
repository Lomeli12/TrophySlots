package net.lomeli.trophyslots.core;

import net.lomeli.knit.config.Config;

public class ModConfig {
    private static final String CLIENT = "client";
    private static final String COMMON = "common";
    // Client
    @Config(category = CLIENT, categoryComment = "Client-side only configs. Ignore on server-side.",
            comment = "Render settings for locked slots. 0 = Crossed out; 1 = Grayed out; 2 = Grayed and crossed out; 3 = no special rendering.")
    public static int slotRenderType = 0;
    @Config(configName = "special", category = "client")
    public static boolean special = true;

    // Common
    @Config(configName = "slotsLost", category = COMMON, categoryComment = "Configs that affects both singleplayer and multiplayer.",
            comment = "The number of slots one loses upon death. If set to -1, they'll lose ALL earned slots.")
    public static int loseSlotNum = 1;
    @Config(category = COMMON, comment = "The number of slots a player starts with.")
    public static int startingSlots = 9;
    @Config(category = COMMON, comment = "Allows you to unlock slots by completing advancements.")
    public static boolean unlockViaAdvancements = true;
    @Config(category = COMMON, comment = "Allows you to unlock slots using trophies.")
    public static boolean canUseTrophy = true;
    @Config(configName = "canBuyTrophies", category = COMMON,
            comment = "Allows you to buy trophies from villagers. Disabling this will disable any trophies bought from villagers!")
    public static boolean canBuyTrophy = false;
    @Config(category = COMMON, comment = "Enable checking for updates")
    public static boolean checkForUpdates = true;
    @Config(configName = "loseSlotsOnDeath", category = COMMON,
            comment = "Allows you to set whether or not you lose slots on death.")
    public static boolean loseSlots = false;
    @Config(configName = "reverseUnlock", category = COMMON,
            comment = "Unlocks slots in reverse order (bottom right instead of top left).")
    public static boolean reverseOrder = false;

}
