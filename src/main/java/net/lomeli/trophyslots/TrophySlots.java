package net.lomeli.trophyslots;

import net.fabricmc.api.ModInitializer;
import net.lomeli.knit.config.ConfigFile;
import net.lomeli.knit.utils.Logger;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.command.ModCommands;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.lomeli.trophyslots.core.handlers.PlayerHandler;
import net.lomeli.trophyslots.items.ModItems;

public class TrophySlots implements ModInitializer {

    public static Logger log;
    public static ConfigFile config;

    public static final String MOD_ID = "trophyslots";
    static final String MOD_NAME = "Trophy Slots";

    @Override
    public void onInitialize() {
        log = new Logger(MOD_NAME);
        log.info("Reading config!");
        config = new ConfigFile(MOD_ID, ModConfig.class);
        config.loadConfig();

        log.info("Registering custom criteria");
        ModCriteria.initTriggers();

        log.info("Registering items");
        ModItems.init();

        if (ModConfig.canBuyTrophy) {
            log.info("Buying trophies enabled, adding trades");
            ModItems.addTrades();
        }

        log.info("Registering events");
        AdvancementHandler.initAdvancmentEvent();
        PlayerHandler.initPlayerEvents();

        log.info("Registering commands");
        ModCommands.registerCommands();
    }
}
