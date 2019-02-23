package net.lomeli.trophyslots;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.network.MessageReloadConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;

@Environment(EnvType.CLIENT)
public class TrophySlotsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpriteHandler.stitchSprites();
        MessageUtil.registerMessage(new MessageSlotClient(), EnvType.CLIENT);
        MessageUtil.registerMessage(new MessageReloadConfig(), EnvType.CLIENT);
    }
}
