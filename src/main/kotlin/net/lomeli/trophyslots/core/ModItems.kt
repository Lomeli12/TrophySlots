package net.lomeli.trophyslots.core

import net.minecraft.item.Item
import net.minecraftforge.fml.common.registry.GameRegistry

public object ModItems {
    public var trophy: Item? = null

    public fun registerItems() {
        trophy = ItemTrophy()
        GameRegistry.registerItem(trophy, "trophy")
    }
}