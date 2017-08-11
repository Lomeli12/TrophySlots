package net.lomeli.trophyslots.capabilities.progression

import net.minecraft.item.ItemStack

class Progression(val progressionID: String, val progressionName: String, itemIcon: ItemStack, private var parentID: String?) {
    fun getParentID(): String? = parentID

    fun getDescription(): String = progressionName + ".desc"
}