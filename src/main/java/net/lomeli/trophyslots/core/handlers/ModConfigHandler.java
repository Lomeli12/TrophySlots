package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.core.ServerConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigHandler {

    @SubscribeEvent
    public static void modConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == TrophySlots.CLIENT_SPEC) {
            TrophySlots.log.debug("Loading client configs");
            ClientConfig.bakeConfig(config);
        } else if (config.getSpec() == TrophySlots.SERVER_SPEC) {
            TrophySlots.log.debug("Loading server config");
            ServerConfig.bakeConfig(config);
        }
    }

    public static void setValueAndSave(final ModConfig config, final String path, final Object value) {
        config.getConfigData().set(path, value);
        config.save();
    }
}
