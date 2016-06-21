package net.lomeli.trophyslots.core

import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.GameRegistry

object ModItems {
    @JvmField
    var trophy: Item? = null

    fun registerItems() {
        trophy = ItemTrophy()
        registerItem(trophy!!, "trophy")
    }

    fun registerItem(item: Item, name: String) {
        item.setRegistryName(name)
        GameRegistry.register(item)
    }
}