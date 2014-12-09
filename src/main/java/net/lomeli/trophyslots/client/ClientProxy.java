package net.lomeli.trophyslots.client;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.Proxy;

public class ClientProxy extends Proxy {
    private EventHandlerClient handlerClient;

    @Override
    public void preInit() {
        super.preInit();
        FMLCommonHandler.instance().bus().register(TrophySlots.versionHandler);
        FMLCommonHandler.instance().bus().register(TrophySlots.modConfig);
    }

    @Override
    public void init() {
        super.init();
        handlerClient = new EventHandlerClient();
        FMLCommonHandler.instance().bus().register(handlerClient);
        MinecraftForge.EVENT_BUS.register(handlerClient);
    }

    @Override
    public void markContainerUpdate() {
        handlerClient.markContainerUpdate = true;
    }
}
