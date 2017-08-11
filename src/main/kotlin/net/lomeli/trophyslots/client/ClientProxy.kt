package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.models.ModelHandler
import net.lomeli.trophyslots.compat.CompatManager
import net.lomeli.trophyslots.core.Proxy

class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
        ModelHandler.registerModels()
    }

    override fun init() {
        super.init()
        registerForgeEvent(EventHandlerClient)
        registerForgeEvent(TrophySlots.modConfig!!)
    }

    override fun postInit() {
        super.postInit()
        CompatManager.initCompatModules()
    }

    override fun resetConfig() {
        TrophySlots.modConfig?.loadConfig()
    }
}