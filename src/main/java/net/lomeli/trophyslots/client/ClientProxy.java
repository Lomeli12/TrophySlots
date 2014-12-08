package net.lomeli.trophyslots.client;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.Proxy;

public class ClientProxy extends Proxy {

    @Override
    public void preInit() {
        super.preInit();
        FMLCommonHandler.instance().bus().register(TrophySlots.modConfig);
    }

    @Override
    public void init() {
        super.init();
        EventHandlerClient handlerClient = new EventHandlerClient();
        FMLCommonHandler.instance().bus().register(handlerClient);
        MinecraftForge.EVENT_BUS.register(handlerClient);
    }
}
