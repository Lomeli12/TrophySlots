package net.lomeli.trophyslots.core.handler;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModItems;

public class VillagerHandler implements VillagerRegistry.IVillageTradeHandler {
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        if (villager.getProfession() == 1 && TrophySlots.canBuyTrophy) {
            if (random.nextInt(100) < 10) {
                ItemStack stack = new ItemStack(Items.emerald, 3 + random.nextInt(5));
                ItemStack item = new ItemStack(Items.diamond, 1 + random.nextInt(5));
                ItemStack out = new ItemStack(ModItems.trophy);
                out.stackTagCompound = new NBTTagCompound();
                out.stackTagCompound.setBoolean("fromVillager", true);
                recipeList.addToListWithCheck(new MerchantRecipe(stack, item, out));
            }
        }
    }
}
