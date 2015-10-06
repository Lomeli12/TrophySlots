package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.ModItems
import net.lomeli.trophyslots.core.Proxy
import net.minecraft.client.Minecraft

public class ClientProxy : Proxy() {
    override fun preInit() {
        super.preInit()
    }

    override fun init() {
        super.init()
        Minecraft.getMinecraft().renderItem.itemModelMesher.register(ModItems.trophy, BasicItemMesh("${TrophySlots.MOD_ID}:trophy"));
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