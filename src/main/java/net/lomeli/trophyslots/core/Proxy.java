package net.lomeli.trophyslots.core;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.common.FMLCommonHandler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.handler.EventHandler;
import net.lomeli.trophyslots.core.handler.VillagerHandler;

public class Proxy {
    public void preInit() {
        ModItems.loadItems();
        FMLCommonHandler.instance().bus().register(TrophySlots.modConfig);

        VillagerHandler.addVillagerTrades();
    }

    public void init() {
        EventHandler handler = new EventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    public void markContainerUpdate() {}
}
