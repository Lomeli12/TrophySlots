package net.lomeli.trophyslots.core;

import net.lomeli.knit.config.Config;

public class ModConfig {
    @Config(configName = "slotsLost", categoryComment = "Configs that affects both singleplayer and multiplayer.",
            comment = "The number of slots one loses upon death. If set to -1, they'll lose ALL earned slots.")
    public static int loseSlotNum = 1;
    @Config(comment = "The number of slots a player starts with.")
    public static int startingSlots = 9;
    @Config(comment = "Allows you to unlock slots by completing advancements.")
    public static boolean unlockViaAdvancements = true;
    @Config(comment = "Allows you to unlock slots using trophies.")
    public static boolean canUseTrophy = true;
    @Config(configName = "canBuyTrophies",
            comment = "Allows you to buy trophies from villagers. Disabling this will disable any trophies bought from villagers!")
    public static boolean canBuyTrophy = false;
    @Config(comment = "Enable checking for updates")
    public static boolean checkForUpdates = true;
    @Config(configName = "loseSlotsOnDeath",
            comment = "Allows you to set whether or not you lose slots on death.")
    public static boolean loseSlots = false;
    @Config(configName = "reverseUnlock",
            comment = "Unlocks slots in reverse order (bottom right instead of top left).")
    public static boolean reverseOrder = false;

}
