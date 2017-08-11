package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.entity.IMerchant
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.village.MerchantRecipe
import net.minecraft.village.MerchantRecipeList
import java.util.*

class TrophyTrade : EntityVillager.ITradeList {
    override fun addMerchantRecipe(merchant: IMerchant?, recipeList: MerchantRecipeList?, random: Random?) {
        val stack = ItemStack(Items.EMERALD, 3 + random!!.nextInt(5))
        val item = ItemStack(Items.DIAMOND, 1 + random!!.nextInt(5))
        val out = ItemStack(TrophySlots.TROPHY)
        out.tagCompound = NBTTagCompound()
        out.tagCompound?.setBoolean("fromVillager", true)
        recipeList?.add(MerchantRecipe(stack, item, out))
    }
}