package net.lomeli.trophyslots.client.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpriteHandler {

    public static final Identifier CROSS_SPRITE = new Identifier(TrophySlots.MOD_ID, "gui/cross");

    public static void stitchSprites() {
        ClientSpriteRegistryCallback.EVENT.register((atlasTexture, registry) -> registry.register(CROSS_SPRITE));
    }
}
