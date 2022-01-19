package net.lomeli.trophyslots.core;

import com.google.common.collect.Lists;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class CommonConfig {
    private static ModConfig commonConfig;
    public static int loseSlotNum = 1;
    public static int startingSlots = 9;
    public static boolean unlockViaAdvancements = true;
    public static boolean canUseTrophy = true;
    public static boolean canBuyTrophy = false;
    public static boolean loseSlots = false;
    public static boolean reverseOrder = false;
    public static List<ResourceLocation> advancementList = Lists.newArrayList();
    public static AdvancementHandler.ListMode listMode;

    final ForgeConfigSpec.IntValue loseSlotNumSpec;
    final ForgeConfigSpec.IntValue startingSlotspec;
    final ForgeConfigSpec.BooleanValue unlockViaAdvancementsSpec;
    final ForgeConfigSpec.BooleanValue canUseTrophySpec;
    final ForgeConfigSpec.BooleanValue canBuyTrophySpec;
    final ForgeConfigSpec.BooleanValue loseSlotsSpec;
    final ForgeConfigSpec.BooleanValue reverseOrderSpec;
    final ForgeConfigSpec.ConfigValue<String> advancementListSpec;
    final ForgeConfigSpec.EnumValue<AdvancementHandler.ListMode> listModeSpec;

    public CommonConfig(final ForgeConfigSpec.Builder builder) {
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
        advancementListSpec = builder
                .comment("List of advancements to either white or black listed. Each advancement should be " +
                        "separated by a ';'. Example:\"minecraft:story/smelt_iron;minecraft:story/shiny_gear\"")
                .translation("config.trophyslots.advancement_list")
                .define("advancmentList", "");
        listModeSpec = builder
                .comment("Use a white, black, or no list of advancements to filter.")
                .translation("config.trophyslots.list_mode")
                .defineEnum("listMode", AdvancementHandler.ListMode.NONE);

        builder.pop();
    }

    public static void reloadConfig() {
        if (commonConfig != null) {
            commonConfig.save();
            bakeConfig(commonConfig);
        }
    }

    public static void setBoolValue(String specName, boolean flag) {
        ForgeConfigSpec.BooleanValue booleanSpec = switch (specName.toLowerCase()) {
            case "unlockviaadvancements" -> TrophySlots.COMMON.unlockViaAdvancementsSpec;
            case "canusetrophy" -> TrophySlots.COMMON.canUseTrophySpec;
            case "canbuytrophy" -> TrophySlots.COMMON.canBuyTrophySpec;
            case "loseslots" -> TrophySlots.COMMON.loseSlotsSpec;
            case "reverseorder" -> TrophySlots.COMMON.reverseOrderSpec;
            default -> null;
        };

        if (booleanSpec == null)
            return;

        booleanSpec.set(flag);
        booleanSpec.save();
    }

    public static void setIntValue(String specName, int value) {
        ForgeConfigSpec.IntValue intSpec = switch (specName.toLowerCase()) {
            case "startingslots" -> TrophySlots.COMMON.startingSlotspec;
            case "loseslotnum" -> TrophySlots.COMMON.loseSlotNumSpec;
            default -> null;
        };
        if (intSpec == null)
            return;

        intSpec.set(value);
        intSpec.save();
    }

    public static void setListMode(AdvancementHandler.ListMode mode) {
        TrophySlots.COMMON.listModeSpec.set(mode);
        TrophySlots.COMMON.listModeSpec.save();
    }

    public static void bakeConfig(final ModConfig config) {
        commonConfig = config;

        loseSlotNum = TrophySlots.COMMON.loseSlotNumSpec.get();
        startingSlots = TrophySlots.COMMON.startingSlotspec.get();

        unlockViaAdvancements = TrophySlots.COMMON.unlockViaAdvancementsSpec.get();
        canUseTrophy = TrophySlots.COMMON.canUseTrophySpec.get();
        canBuyTrophy = TrophySlots.COMMON.canBuyTrophySpec.get();
        loseSlots = TrophySlots.COMMON.loseSlotsSpec.get();
        reverseOrder = TrophySlots.COMMON.reverseOrderSpec.get();
        listMode = TrophySlots.COMMON.listModeSpec.get();

        advancementList.clear();
        for (String s : TrophySlots.COMMON.advancementListSpec.get().split(";"))
            advancementList.add(new ResourceLocation(s));
    }
}
