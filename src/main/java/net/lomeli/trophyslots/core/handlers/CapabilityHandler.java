package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class CapabilityHandler {

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(IPlayerSlots.class);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            event.addCapability(PlayerSlotProvider.ID, new PlayerSlotProvider());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            IPlayerSlots newSlots = PlayerSlotHelper.getPlayerSlots(event.getPlayer());
            if (newSlots != null)
                PlayerSlotHelper.readFromPersist(event.getOriginal(), newSlots);
        }
    }
}
