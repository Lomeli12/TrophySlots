package net.lomeli.trophyslots.core.handlers;

import net.lomeli.knit.event.AdvancementCallback;
import net.lomeli.knit.utils.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.advancement.SimpleAdvancement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

public class AdvancementHandler {

    public static void initAdvancmentEvent() {
        AdvancementCallback.EVENT.register(AdvancementHandler::unlockedAdvancment);
    }

    private static void unlockedAdvancment(ServerPlayerEntity player, SimpleAdvancement advancement) {
        if (!ModConfig.unlockViaAdvancements) return;
        if (advancement.getId().getNamespace().equals(TrophySlots.MOD_ID)) return;
        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat()) return;
        if (!(player instanceof ISlotHolder) || player.world.isClient || player.abilities.creativeMode) return;
        PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
        if (slotManager.maxSlotsUnlocked()) return;
        if (slotManager.unlockSlot(1)) {
            player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.unlock"), false);
            TrophySlots.log.info("Sending slot update packet to player {}.", player.getName().getText());
            MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked()), player);
            ModCriteria.UNLOCK_SLOT.trigger(player);
        }
    }
}
