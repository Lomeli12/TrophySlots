package net.lomeli.trophyslots.core.handlers;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotProvider;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class CapabilityHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity)
            event.addCapability(PlayerSlotProvider.ID, new PlayerSlotProvider());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerEntity newPlayer = event.getPlayer();
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(newPlayer);
            IPlayerSlots oldSlots = PlayerSlotHelper.getPlayerSlots(event.getOriginal());
            if (playerSlots != null && oldSlots != null)
                playerSlots.setSlotsUnlocked(oldSlots.getSlotsUnlocked());
        }
    }
}
