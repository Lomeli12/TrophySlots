package net.lomeli.trophyslots.client.handler;

import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpriteHandler {

    public static final ResourceLocation CROSS_SPRITE = new ResourceLocation(TrophySlots.MOD_ID, "gui/cross");
    public static final ResourceLocation SNOWFLAKE = new ResourceLocation(TrophySlots.MOD_ID, "gui/snowflake");

    @SubscribeEvent
    public static void stitchSprite(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation() != PlayerContainer.LOCATION_BLOCKS_TEXTURE)
            return;
        event.addSprite(CROSS_SPRITE);
        event.addSprite(SNOWFLAKE);
    }
}
