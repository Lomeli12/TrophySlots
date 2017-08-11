package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.models.ModelHandler
import net.lomeli.trophyslots.compat.CompatManager
import net.lomeli.trophyslots.core.Proxy
import net.minecraftforge.common.MinecraftForge

class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
        ModelHandler.registerModels()
        MinecraftForge.EVENT_BUS.register(TrophySlots.modConfig!!)
        MinecraftForge.EVENT_BUS.register(EventHandlerClient)
    }

    override fun init() {
        super.init()
    }

    override fun postInit() {
        super.postInit()
        CompatManager.initCompatModules()
    }

    override fun resetConfig() {
        TrophySlots.modConfig?.loadConfig()
    }
}