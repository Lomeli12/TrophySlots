package net.lomeli.trophyslots.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import net.lomeli.trophyslots.TrophySlots;

public class ClientConfig {
    public static ModConfig clientConfig;
    public static int slotRenderType = 0;
    public static boolean special = true;

    final ForgeConfigSpec.IntValue slotRenderTypeSpec;
    final ForgeConfigSpec.BooleanValue specialSpec;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("client");

        slotRenderTypeSpec = builder
                .comment("Render settings for locked slots. 0 = Crossed out; 1 = Grayed out; " +
                        "2 = Grayed and crossed out; 3 = no special rendering.")
                .translation("config.trophyslots.render_locked_slots")
                .defineInRange("slotRenderType", 0, 0, 3);
        specialSpec = builder
                .comment("Walking in a Winter GUIland")
                .translation("config.trophyslots.special")
                .define("special", true);

        builder.pop();
    }

    public static void reloadConfig() {
        if (clientConfig != null) {
            clientConfig.save();
            bakeConfig(clientConfig);
        }
    }

    public static void setSlotRenderType(int type) {
        TrophySlots.CLIENT.slotRenderTypeSpec.set(type);
        TrophySlots.CLIENT.slotRenderTypeSpec.save();
    }

    public static void setSpecial(boolean flag) {
        TrophySlots.CLIENT.specialSpec.set(flag);
        TrophySlots.CLIENT.specialSpec.save();
    }

    public static void bakeConfig(final ModConfig config) {
        clientConfig = config;

        slotRenderType = TrophySlots.CLIENT.slotRenderTypeSpec.get();
        special = TrophySlots.CLIENT.specialSpec.get();
    }
}
