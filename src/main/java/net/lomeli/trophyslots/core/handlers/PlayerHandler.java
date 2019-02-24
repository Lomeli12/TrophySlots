package net.lomeli.trophyslots.core.handlers;

import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

public class PlayerHandler {

    public static void onPlayerDeath(ServerPlayerEntity player) {
        if (ModConfig.loseSlots && (ModConfig.loseSlotNum == -1 || ModConfig.loseSlotNum > 0)) {
            TrophySlots.log.info("%s died. Losing %s slot(s).", player.getName().getText(), ModConfig.loseSlotNum);
            if (player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                int slots = slotManager.getSlotsUnlocked();
                if (ModConfig.loseSlotNum == -1)
                    slots = 0;
                else
                    slots -= ModConfig.loseSlotNum;

                if (slots < 0)
                    slots = 0;
                slotManager.setSlotsUnlocked(slots);
                String msg = ModConfig.loseSlotNum == 1 ? "msg.trophyslots.lost_slot" :
                        ModConfig.loseSlotNum == -1 ? "msg.trophyslots.lost_all" : "msg.trophyslots.lost_slot.multiple";
                if (ModConfig.loseSlotNum > 1)
                    player.addChatMessage(new TranslatableTextComponent(msg, ModConfig.loseSlotNum), false);
                else
                    player.addChatMessage(new TranslatableTextComponent(msg), false);
                MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked(),
                        ModConfig.reverseOrder), player);
            }
        }
    }

    public static void clonePlayer(ServerPlayerEntity player, ServerPlayerEntity oldPlayer, boolean switchingDim) {
        if (!player.world.isClient() && player instanceof ISlotHolder && oldPlayer instanceof ISlotHolder) {
            PlayerSlotManager oldSlotManager = ((ISlotHolder) oldPlayer).getSlotManager();
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            slotManager.setSlotsUnlocked(oldSlotManager.getSlotsUnlocked());
        }
    }
}
