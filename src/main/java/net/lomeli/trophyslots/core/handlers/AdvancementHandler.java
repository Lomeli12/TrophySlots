package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.criterion.ModCriterions;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.advancement.SimpleAdvancement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

public class AdvancementHandler {

    public static void unlockedAdvancment(ServerPlayerEntity player, SimpleAdvancement advancement) {
        if (!ModConfig.unlockViaAdvancements) return;
        if (advancement.getId().getNamespace() == TrophySlots.MOD_ID) return;
        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat()) return;
        if (!(player instanceof ISlotHolder) || player.world.isClient || player.abilities.creativeMode) return;
        PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
        if (slotManager.maxSlotsUnlocked()) return;
        if (slotManager.unlockSlot(1)) {
            player.addChatMessage(new TranslatableTextComponent("msg.trophyslots.unlock"), false);
            //TODO: Update client
            ModCriterions.UNLOCK_SLOT.trigger(player);
            System.out.println("[Trophy Slots]: " + player.getDisplayName().getText() + " unlocked advancement " +
                    advancement.getDisplay().getTitle().getText());
        }
    }
}
