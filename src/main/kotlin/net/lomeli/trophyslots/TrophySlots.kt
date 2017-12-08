package net.lomeli.trophyslots

import net.lomeli.trophyslots.capabilities.slots.ISlotInfo
import net.lomeli.trophyslots.capabilities.slots.SlotCapability
import net.lomeli.trophyslots.capabilities.slots.SlotInfo
import net.lomeli.trophyslots.core.Config
import net.lomeli.trophyslots.core.ItemTrophy
import net.lomeli.trophyslots.core.Logger
import net.lomeli.trophyslots.core.Proxy
import net.lomeli.trophyslots.core.command.CommandTrophySlots
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.lomeli.trophyslots.core.version.VersionChecker
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

@Mod(modid = TrophySlots.MOD_ID, name = TrophySlots.MOD_NAME, version = TrophySlots.VERSION,
        modLanguageAdapter = TrophySlots.KOTLIN_ADAPTER, modLanguage = TrophySlots.LANGUAGE, guiFactory = TrophySlots.FACTORY)
object TrophySlots {
    const val FACTORY = "net.lomeli.trophyslots.client.config.TrophySlotsFactory"
    const val MOD_ID = "trophyslots"
    const val MOD_NAME = "Trophy Slots"
    const val KOTLIN_ADAPTER = "net.lomeli.trophyslots.KotlinAdapter"
    const val LANGUAGE = "Kotlin"
    const val VERSION = "@VERSION@"
    const val updateUrl = "https://raw.githubusercontent.com/Lomeli12/TrophySlots/master/update.json"

    @Mod.Instance(MOD_ID)
    var instance: TrophySlots? = null

    @SidedProxy(clientSide = "net.lomeli.trophyslots.client.ClientProxy", serverSide = "net.lomeli.trophyslots.core.Proxy")
    var proxy: Proxy? = null

    @JvmField val TROPHY: Item? = ItemTrophy()

    var slotRenderType = 0
    var loseSlotNum = 1
    var unlockViaAdvancements = true
    var canUseTrophy = true
    var canBuyTrophy = false
    var checkForUpdates = true
    var xmas = true
    var loseSlots = false

    var packetHandler: SimpleNetworkWrapper? = null
    var modConfig: Config? = null
    var versionHandler: VersionChecker? = null

    var debug: Boolean = false
    var log: Logger? = null

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log = Logger()
        try {
            EntityPlayer::class.java.getMethod("getBedLocation")
            debug = true
            log?.logInfo("Dev environment, enabled logging!")
        } catch (e: Exception) {
            debug = false
            log?.logError(e)
        }

        modConfig = Config(event.suggestedConfigurationFile)
        modConfig?.loadConfig()
        versionHandler = VersionChecker(updateUrl, MOD_NAME, VERSION)
        if (checkForUpdates)
            versionHandler?.checkForUpdates()

        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID)
        packetHandler?.registerMessage<MessageSlotsClient, IMessage>(MessageSlotsClient::class.java,
                MessageSlotsClient::class.java, 0, Side.CLIENT)

        CapabilityManager.INSTANCE.register(ISlotInfo::class.java, SlotCapability(), SlotInfo::class.java)

        proxy?.preInit()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy?.init()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy?.postInit()
    }

    @Mod.EventHandler
    fun serverAboutToStart(event: FMLServerAboutToStartEvent) {
        proxy?.resetConfig()
    }

    @Mod.EventHandler
    fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandTrophySlots())
    }
}