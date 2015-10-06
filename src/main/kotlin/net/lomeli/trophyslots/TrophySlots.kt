package net.lomeli.trophyslots

import net.lomeli.trophyslots.core.Config
import net.lomeli.trophyslots.core.Logger
import net.lomeli.trophyslots.core.ModItems
import net.lomeli.trophyslots.core.Proxy
import net.lomeli.trophyslots.core.command.CommandTrophySlots
import net.lomeli.trophyslots.core.network.MessageOpenWhitelist
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.lomeli.trophyslots.core.network.MessageUpdateWhitelist
import net.lomeli.trophyslots.core.version.VersionChecker
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.stats.Achievement
import net.minecraft.util.IChatComponent
import net.minecraftforge.common.AchievementPage
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side


@Mod(modid = TrophySlots.MOD_ID, name = TrophySlots.MOD_NAME, version = TrophySlots.VERSION, guiFactory = TrophySlots.FACTORY, modLanguageAdapter = TrophySlots.KOTLIN_ADAPTER, dependencies = "required-after:Forgelin;")
public object TrophySlots {
    const val FACTORY = "net.lomeli.trophyslots.client.config.TrophySlotsFactory"
    const val MOD_ID = "trophyslots"
    const val MOD_NAME = "Trophy Slots"
    const val KOTLIN_ADAPTER = "io.drakon.forgelin.KotlinAdapter"//Using Forgelin until srgExtra is added in FG 2.0.2
    const val MAJOR = 2
    const val MINOR = 0
    const val REV = 0
    const val VERSION = "$MAJOR.$MINOR.$REV"
    const val updateUrl = "https://raw.githubusercontent.com/Lomeli12/TrophySlots/master/update.json"
    const val slotsUnlocked = "$MOD_ID" + "_slotsUnlocked"

    @SidedProxy(clientSide = "net.lomeli.trophyslots.client.ClientProxy", serverSide = "net.lomeli.trophyslots.core.Proxy")
    public var proxy: Proxy? = null

    var slotRenderType = 0
    var loseSlotNum = 1
    var unlockViaAchievements = true
    var canUseTrophy = true
    var canBuyTrophy = false
    var disable3 = false
    var checkForUpdates = true
    var xmas = true
    var useWhiteList = false
    var loseSlots = false

    var packetHandler: SimpleNetworkWrapper? = null
    var modConfig: Config? = null
    var versionHandler: VersionChecker? = null

    var firstSlot: Achievement? = null;
    var maxCapcity: Achievement? = null
    var achievementPage: AchievementPage? = null
    var debug: Boolean = false

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        try {
            EntityPlayer::class.java.getMethod("addChatComponentMessage", IChatComponent::class.java)
            debug = true
            Logger.logInfo("Dev environment, enabled logging!")
        } catch (e: Exception) {
            debug = false
        }

        modConfig = Config(event.suggestedConfigurationFile)
        modConfig?.loadConfig()
        versionHandler = VersionChecker(updateUrl, MOD_NAME, MAJOR, MINOR, REV)
        if (checkForUpdates)
            versionHandler?.checkForUpdates()

        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID.toLowerCase())
        packetHandler?.registerMessage(MessageSlotsClient::class.java, MessageSlotsClient::class.java, 0, Side.CLIENT)
        packetHandler?.registerMessage(MessageOpenWhitelist::class.java, MessageOpenWhitelist::class.java, 1, Side.CLIENT)
        packetHandler?.registerMessage(MessageUpdateWhitelist::class.java, MessageUpdateWhitelist::class.java, 2, Side.CLIENT)

        proxy?.preInit()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy?.init()

        firstSlot = Achievement("achievement.trophyslots.firstSlot", "firstSlotAchievement", 0, 0, Blocks.chest, null).registerStat() as Achievement
        maxCapcity = Achievement("achievement.trophyslots.maximumCapacity", "maximumCapacityAchievement", 2, 0, ModItems.trophy, firstSlot).registerStat() as Achievement

        achievementPage = AchievementPage(MOD_NAME, firstSlot, maxCapcity)
        AchievementPage.registerAchievementPage(achievementPage)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy?.postInit()
    }

    @Mod.EventHandler
    fun serverStopping(event: FMLServerStoppingEvent) {
        proxy?.reset()
    }

    @Mod.EventHandler
    fun serverStarting(event: FMLServerAboutToStartEvent) {
        proxy?.resetConfig()
    }

    @Mod.EventHandler
    fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandTrophySlots())
    }
}