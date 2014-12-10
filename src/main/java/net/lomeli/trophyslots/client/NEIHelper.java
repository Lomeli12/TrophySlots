package net.lomeli.trophyslots.client;

import codechicken.nei.NEIClientConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NEIHelper {
    public static String itemInfoString(ItemStack stack) {
        String out =  " " + Item.getIdFromItem(stack.getItem()) + (stack.getItemDamage() != 0 ? "/" + stack.getItemDamage() : "");
        return !NEIClientConfig.isHidden() ? out : "";
    }
}
