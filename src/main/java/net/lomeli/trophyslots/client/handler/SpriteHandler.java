package net.lomeli.trophyslots.client.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpriteHandler {

    public static final Identifier CROSS_SPRITE = new Identifier(TrophySlots.MOD_ID, "gui/cross");
    public static final Identifier SNOWFLAKE = new Identifier(TrophySlots.MOD_ID, "gui/snowflake");

    public static void stitchSprites() {
        ClientSpriteRegistryCallback.EVENT.register((atlasTexture, registry) -> {
            if (atlasTexture == MinecraftClient.getInstance().getSpriteAtlas()) {
                registry.register(CROSS_SPRITE);
                registry.register(SNOWFLAKE);
            }
        });
    }
}
