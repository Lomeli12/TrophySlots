package net.lomeli.trophyslots;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.trophyslots.core.Config;
import net.lomeli.trophyslots.core.Proxy;

@Mod(modid = TrophySlots.MOD_ID, name = TrophySlots.MOD_NAME, version = TrophySlots.VERSION, guiFactory = TrophySlots.FACTORY)
public class TrophySlots {
    public static final String FACTORY = "net.lomeli.trophyslots.client.config.TrophySlotsFactory";
    public static final String MOD_ID = "trophyslots";
    public static final String MOD_NAME = "Trophy Slots";

    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;

    public static final String slotsUnlocked = MOD_ID + "_slotsUnlocked";
    public static final String unlockMessage = "msg.trophyslots.unlock";

    @SidedProxy(clientSide = "net.lomeli.trophyslots.client.ClientProxy", serverSide = "net.lomeli.trophyslots.core.Proxy")
    public static Proxy proxy;

    public static Config modConfig;

    public static int startingSlots = 9;
    public static boolean unlockViaAchievements = true;
    public static boolean canUseTrophy = true;
    public static boolean canBuyTrophy = false;

    public static void log(int type, String log) {
        FMLLog.log(type == 0 ? Level.INFO : Level.WARN, log);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modConfig = new Config(event.getSuggestedConfigurationFile());
        modConfig.loadConfig();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
