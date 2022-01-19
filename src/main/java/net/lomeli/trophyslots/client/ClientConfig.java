package net.lomeli.trophyslots.client;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.screen.SlotRenderType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig {
    public static ModConfig clientConfig;
    public static SlotRenderType renderType = SlotRenderType.CROSS_ONLY;
    public static boolean special = true;

    final ForgeConfigSpec.EnumValue<SlotRenderType> renderTypeSpec;
    final ForgeConfigSpec.BooleanValue specialSpec;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("client");

        renderTypeSpec = builder
                .comment("Render settings for locked slots. 0 = Crossed out; 1 = Grayed out; " +
                        "2 = Grayed and crossed out; 3 = no special rendering.")
                .translation("config.trophyslots.render_locked_slots")
                .defineEnum("slotRenderType", SlotRenderType.CROSS_ONLY);
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

    public static void setSlotRenderType(SlotRenderType type) {
        TrophySlots.CLIENT.renderTypeSpec.set(type);
        TrophySlots.CLIENT.renderTypeSpec.save();
    }

    public static void setSpecial(boolean flag) {
        TrophySlots.CLIENT.specialSpec.set(flag);
        TrophySlots.CLIENT.specialSpec.save();
    }

    public static void bakeConfig(final ModConfig config) {
        clientConfig = config;

        renderType = TrophySlots.CLIENT.renderTypeSpec.get();
        special = TrophySlots.CLIENT.specialSpec.get();
    }
}
