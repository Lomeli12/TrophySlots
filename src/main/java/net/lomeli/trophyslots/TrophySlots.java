package net.lomeli.trophyslots;

import org.apache.logging.log4j.Level;

import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;

import net.minecraftforge.common.AchievementPage;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.trophyslots.core.Config;
import net.lomeli.trophyslots.core.ModItems;
import net.lomeli.trophyslots.core.Proxy;
import net.lomeli.trophyslots.core.handler.VersionHandler;

@Mod(modid = TrophySlots.MOD_ID, name = TrophySlots.MOD_NAME, version = TrophySlots.VERSION, guiFactory = TrophySlots.FACTORY)
public class TrophySlots {
    public static final String FACTORY = "net.lomeli.trophyslots.client.config.TrophySlotsFactory";
    public static final String MOD_ID = "trophyslots";
    public static final String MOD_NAME = "Trophy Slots";

    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;

    public static final String slotsUnlocked = MOD_ID + "_slotsUnlocked";
    public static final String unlockMessage = "msg.trophyslots.unlock";
    public static final String updateUrl = "https://raw.githubusercontent.com/Lomeli12/TrophySlots/master/update.json";

    @SidedProxy(clientSide = "net.lomeli.trophyslots.client.ClientProxy", serverSide = "net.lomeli.trophyslots.core.Proxy")
    public static Proxy proxy;

    public static Config modConfig;
    public static VersionHandler versionHandler;

    public static int startingSlots = 9;
    public static boolean unlockViaAchievements = true;
    public static boolean canUseTrophy = true;
    public static boolean canBuyTrophy = false;
    public static boolean disable3 = false;
    public static boolean checkForUpdates = true;
    public static boolean xmas = true;

    public static Achievement firstSlot, maxCapcity;
    public static AchievementPage achievementPage;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modConfig = new Config(event.getSuggestedConfigurationFile());
        modConfig.loadConfig();
        versionHandler = new VersionHandler(updateUrl, MOD_NAME, MAJOR, MINOR, REV);
        if (checkForUpdates)
            versionHandler.checkForUpdates();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();

        firstSlot = new Achievement("achievement.trophyslots.firstSlot", "firstSlotAchievement", 0, 0, Blocks.chest, null).registerStat();
        maxCapcity = new Achievement("achievement.trophyslots.maximumCapacity", "maximumCapacityAchievement", 2, 0, ModItems.trophy, firstSlot).registerStat();

        achievementPage = new AchievementPage(MOD_NAME, firstSlot, maxCapcity);
        AchievementPage.registerAchievementPage(achievementPage);
    }

    public static void log(int type, String log) {
        FMLLog.log(MOD_NAME, type == 0 ? Level.INFO : Level.ERROR, log, null);
    }
}
