package net.lomeli.trophyslots;

import net.fabricmc.api.ModInitializer;
import net.lomeli.knit.config.ConfigFile;
import net.lomeli.knit.network.MessageUtil;
import net.lomeli.knit.utils.Logger;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.criterion.ModCriterions;
import net.lomeli.trophyslots.core.network.MessageReloadConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.items.ModItems;

public class TrophySlots implements ModInitializer {

	public static Logger log;
	public static ConfigFile config;

	public static final String MOD_ID = "trophyslots";
	public static final String MOD_NAME = "Trophy Slots";

	/**
	 * Player inventory slots
	 */
	public static final int MAX_SLOTS = 36;

	/**
	 * Player Inventory slots + armor slots + crafting slots
	 */
	public static final int MAX_INV_SLOTS = 44;
	@Override
	public void onInitialize() {
		log = new Logger(MOD_NAME);
		ModCriterions.initTriggers();

		log.info("Reading config!");
		config = new ConfigFile(MOD_ID, ModConfig.class);
		config.loadConfig();

		log.info("Registering packets");
		MessageUtil.registerMessage(new MessageSlotClient());
		MessageUtil.registerMessage(new MessageReloadConfig());
		ModItems.init();
	}
}
