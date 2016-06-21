package net.lomeli.trophyslots.client.models

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.ModItems
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.model.ModelLoader

object ModelHandler {
    fun registerModels() {
        ModelLoader.setCustomModelResourceLocation(ModItems.trophy, 0, ModelResourceLocation("${TrophySlots.MOD_ID}:trophy", "inventory"))
        ModelLoader.setCustomModelResourceLocation(ModItems.trophy, 1, ModelResourceLocation("${TrophySlots.MOD_ID}:trophy", "inventory"))
    }
}