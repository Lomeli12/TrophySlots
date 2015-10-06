package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.core.Proxy

public class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
    }

    override fun init() {
        super.init()
    }

    override fun postInit() {
        super.postInit()
    }

    override fun getSlotsUnlocked(): Int {
        return super.getSlotsUnlocked()
    }

    override fun setSlotsUnlocked(i0: Int) {
        super.setSlotsUnlocked(i0)
    }

    override fun slotUnlocked(slot: Int): Boolean {
        return super.slotUnlocked(slot)
    }

    override fun hasUnlockedAllSlots(): Boolean {
        return super.hasUnlockedAllSlots()
    }

    override fun reset() {
        super.reset()
    }

    override fun resetConfig() {
        super.resetConfig()
    }

    override fun openWhitelistGui() {
        super.openWhitelistGui()
    }
}