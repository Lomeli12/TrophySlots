package net.lomeli.trophyslots;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.trophyslots.client.handler.SpriteHandler;

@Environment(EnvType.CLIENT)
public class TrophySlotsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpriteHandler.stitchSprites();
    }
}
