package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.core.CommonConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigHandler {

    @SubscribeEvent
    public static void modConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == TrophySlots.CLIENT_SPEC) {
            TrophySlots.log.debug("Loading client configs");
            ClientConfig.bakeConfig(config);
        } else if (config.getSpec() == TrophySlots.COMMON_SPEC) {
            TrophySlots.log.debug("Loading server config");
            CommonConfig.bakeConfig(config);
        }
    }
}
