package net.lomeli.trophyslots;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.common.AchievementPage;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.trophyslots.compat.CompatManager;
import net.lomeli.trophyslots.core.Config;
import net.lomeli.trophyslots.core.Logger;
import net.lomeli.trophyslots.core.ModItems;
import net.lomeli.trophyslots.core.Proxy;
import net.lomeli.trophyslots.core.handler.VersionHandler;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;

@Mod(modid = TrophySlots.MOD_ID, name = TrophySlots.MOD_NAME, version = TrophySlots.VERSION, guiFactory = TrophySlots.FACTORY)
public class TrophySlots {
    public static final String FACTORY = "net.lomeli.trophyslots.client.config.TrophySlotsFactory";
    public static final String MOD_ID = "trophyslots";
    public static final String MOD_NAME = "Trophy Slots";
    public static final String slotsUnlocked = MOD_ID + "_slotsUnlocked";

    public static final int MAJOR = 1, MINOR = 2, REV = 3;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;

    public static final String updateUrl = "https://raw.githubusercontent.com/Lomeli12/TrophySlots/master/update.json";

    public static SimpleNetworkWrapper packetHandler;

    @SidedProxy(clientSide = "net.lomeli.trophyslots.client.ClientProxy", serverSide = "net.lomeli.trophyslots.core.Proxy")
    public static Proxy proxy;

    public static Config modConfig;
    public static VersionHandler versionHandler;

    public static int startingSlots = 9;
    public static int slotRenderType = 0;
    public static boolean unlockViaAchievements = true;
    public static boolean canUseTrophy = true;
    public static boolean canBuyTrophy = false;
    public static boolean disable3 = false;
    public static boolean checkForUpdates = true;
    public static boolean xmas = true;
    public static boolean useWhiteList = false;
    public static boolean reverse = false;
    public static boolean useBlackList;

    public static List<String> achievementWhiteList, achievementBlackList;
    public static Achievement firstSlot, maxCapcity;
    public static AchievementPage achievementPage;
    public static boolean debug;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            EntityPlayer.class.getMethod("addChatComponentMessage", IChatComponent.class);
            debug = true;
            Logger.logInfo("Dev environment, enabled logging!");
        } catch (Exception e) {
            debug = false;
        }

        modConfig = new Config(event.getSuggestedConfigurationFile());
        modConfig.loadConfig();
        versionHandler = new VersionHandler(updateUrl, MOD_NAME, MAJOR, MINOR, REV);
        if (checkForUpdates)
            versionHandler.checkForUpdates();

        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID.toLowerCase());
        packetHandler.registerMessage(MessageSlotsClient.class, MessageSlotsClient.class, 0, Side.CLIENT);

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

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CompatManager.initCompatModules();
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.reset();
    }
}
