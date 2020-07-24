package net.lomeli.trophyslots.core.handlers;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class AdvancementHandler {

    @SubscribeEvent
    public static void advancementEvent(AdvancementEvent event) {
        if (!ServerConfig.unlockViaAdvancements) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (player instanceof FakePlayer || player.abilities.isCreativeMode) return;
        Advancement advancement = event.getAdvancement();
        if (advancement.getId().getNamespace().equalsIgnoreCase(TrophySlots.MOD_ID) || advancement.getDisplay() == null
                || !advancement.getDisplay().shouldAnnounceToChat()) return;
        if (ServerConfig.listMode == ListMode.WHITE && !ServerConfig.advancementList.contains(advancement.getId()))
            return;
        if (ServerConfig.listMode == ListMode.BLACK && ServerConfig.advancementList.contains(advancement.getId()))
            return;
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;
        if (playerSlots.unlockSlot(1)) {
            player.sendMessage(new TranslationTextComponent("msg.trophyslots.unlock"), ChatType.CHAT);
            TrophySlots.log.debug("Sending slot update packet to player {}", player.getName().getFormattedText());
            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
            ModCriteria.UNLOCK_SLOT.trigger(player);
        }
    }

    public enum ListMode {
        WHITE, BLACK, NONE;
    }
}
