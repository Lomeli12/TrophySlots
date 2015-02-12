package net.lomeli.trophyslots.core;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item trophy;

    public static void registerItems() {
        trophy = new ItemTrophy();
        GameRegistry.registerItem(trophy, "trophy");
    }
}
