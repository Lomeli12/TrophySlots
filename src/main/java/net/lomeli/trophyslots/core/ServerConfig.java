package net.lomeli.trophyslots.core;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ServerConfig {
    public static ModConfig serverConfig;
    public static int loseSlotNum = 1;
    public static int startingSlots = 9;
    public static boolean unlockViaAdvancements = true;
    public static boolean canUseTrophy = true;
    public static boolean canBuyTrophy = false;
    //TODO: Use Forge's update thing instead of repeatedly failing at making your own
    //public static boolean checkForUpdates = true;
    public static boolean loseSlots = false;
    public static boolean reverseOrder = false;

    final ForgeConfigSpec.IntValue loseSlotNumSpec;
    final ForgeConfigSpec.IntValue startingSlotspec;
    final ForgeConfigSpec.BooleanValue unlockViaAdvancementsSpec;
    final ForgeConfigSpec.BooleanValue canUseTrophySpec;
    final ForgeConfigSpec.BooleanValue canBuyTrophySpec;
    final ForgeConfigSpec.BooleanValue loseSlotsSpec;
    final ForgeConfigSpec.BooleanValue reverseOrderSpec;

    public ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");

        loseSlotNumSpec = builder
                .comment("The number of slots one loses upon death. If set to -1, they'll lose ALL earned slots.")
                .translation("config.trophyslots.lose_slots.num")
                .defineInRange("loseSlotNum", 1, -1, 27);
        startingSlotspec = builder
                .comment("The number of slots a player starts with.")
                .translation("config.trophyslots.starting_slota")
                .defineInRange("startingSlots", 9, 9, 36);

        unlockViaAdvancementsSpec = builder
                .comment("Allows you to unlock slots by completing advancements.")
                .translation("config.trophyslots.unlock_via_advancements")
                .define("unlockViaAdvancements", true);
        canUseTrophySpec = builder
                .comment("Allows you to unlock slots using trophies.")
                .translation("config.trophyslots.can_use_trophy")
                .define("canUseTrophy", true);
        canBuyTrophySpec = builder.comment("Allows you to buy trophies from villagers. " +
                "Disabling this will disable any trophies bought from villagers!")
                .translation("config.trophyslots.can_buy_trophy")
                .define("canBuyTrophy", false);
        loseSlotsSpec = builder
                .comment("Allows you to set whether or not you lose slots on death.")
                .translation("config.trophyslots.lose_slots")
                .define("loseSlots", false);
        reverseOrderSpec = builder
                .comment("Render settings for locked slots. 0 = Crossed out; 1 = Grayed out; " +
                        "2 = Grayed and crossed out; 3 = no special rendering.")
                .translation("config.trophyslots.render_locked_slots")
                .define("reverseOrder", false);

        builder.pop();
    }

    public static void bakeConfig(final ModConfig config) {
        serverConfig = config;

        loseSlotNum = TrophySlots.SERVER.loseSlotNumSpec.get();
        startingSlots = TrophySlots.SERVER.startingSlotspec.get();

        unlockViaAdvancements = TrophySlots.SERVER.unlockViaAdvancementsSpec.get();
        canUseTrophy = TrophySlots.SERVER.canUseTrophySpec.get();
        canBuyTrophy = TrophySlots.SERVER.canBuyTrophySpec.get();
        loseSlots = TrophySlots.SERVER.loseSlotsSpec.get();
        reverseOrder = TrophySlots.SERVER.reverseOrderSpec.get();
    }

}
