package net.lomeli.trophyslots.client

import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT) class BasicItemMesh(val resource: String) : ItemMeshDefinition {
    override fun getModelLocation(stack: ItemStack?): ModelResourceLocation? = ModelResourceLocation(resource, "inventory")
}