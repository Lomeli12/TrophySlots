package net.lomeli.trophyslots.client.handler;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpriteHandler {

    public static final ResourceLocation CROSS_SPRITE = new ResourceLocation(TrophySlots.MOD_ID, "gui/cross");
    public static final ResourceLocation SNOWFLAKE = new ResourceLocation(TrophySlots.MOD_ID, "gui/snowflake");

    @SubscribeEvent
    public static void stitchSprite(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) return;
        event.addSprite(CROSS_SPRITE);
        event.addSprite(SNOWFLAKE);
    }
}
