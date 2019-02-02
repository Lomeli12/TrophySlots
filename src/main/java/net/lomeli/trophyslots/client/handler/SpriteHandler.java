package net.lomeli.trophyslots.client.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.client.texture.SpriteRegistry;
import net.fabricmc.fabric.events.client.SpriteEvent;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpriteHandler {

    public static final Identifier CROSS_SPRITE = new Identifier(TrophySlots.MOD_ID, "gui/cross");

    public static void stitchSprites() {
        SpriteEvent.PROVIDE.register(new SpriteEvent.Provider() {
            @Override
            public void registerSprites(SpriteRegistry registry) {
                registry.register(CROSS_SPRITE);
            }
        });
    }
}
