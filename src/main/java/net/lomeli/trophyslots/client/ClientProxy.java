package net.lomeli.trophyslots.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModItems;
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

        registerModel(ModItems.trophy, 0, "trophyslots:trophy");
        registerModel(ModItems.trophy, 1, "trophyslots:trophy");
        registerMetadataModel(ModItems.trophy, "trophyslots:trophy", "trophyslots:trophy");
    }

    @Override
    public void markContainerUpdate() {
        handlerClient.markContainerUpdate = true;
    }

    private void registerMetadataModel(Item item, String... files) {
        ModelBakery.addVariantName(item, files);
    }

    private void registerModel(Item item, int metaData, String name) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, metaData, new ModelResourceLocation(name, "inventory"));
    }
}
