package net.lomeli.trophyslots.core.handlers;

import net.lomeli.knit.event.player.*;
import net.lomeli.knit.network.MessageUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

public class PlayerHandler {

    public static void initPlayerEvents() {
        PlayerLoginCallback.EVENT.register((connection, player) -> updateClientSlots(player));
        PlayerRespawnCallback.EVENT.register((oldPlayer, newPlayer, dimType, keepEverything) -> updateClientSlots(newPlayer));
        PlayerDeathCallback.EVENT.register(PlayerHandler::onPlayerDeath);
        ClonePlayerCallback.EVENT.register(PlayerHandler::clonePlayer);
        PlayerTickCallback.EVENT.register(PlayerHandler::playerUpdate);
        ItemPickupCallback.EVENT.register(PlayerHandler::onItemPickup);
    }

    private static void playerUpdate(PlayerEntity player) {
        if (player.world.isClient || player.abilities.creativeMode) return;
        if (player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            if (slotManager.maxSlotsUnlocked()) return;
            for (int i = 0; i < player.inventory.main.size(); i++) {
                if (slotManager.slotUnlocked(i)) continue;
                ItemStack stack = player.inventory.main.get(i);
                if (stack.isEmpty()) continue;
                int slot = InventoryUtils.getNextEmptySlot(slotManager, player.inventory);
                if (slot <= -1) {
                    player.dropStack(stack, 0f);
                    player.inventory.setInvStack(i, ItemStack.EMPTY);
                } else
                    player.inventory.setInvStack(slot, player.inventory.removeInvStack(i));
            }
        }
    }

    private static boolean onItemPickup(ItemEntity itemEntity, PlayerEntity player) {
        if (!player.world.isClient && !player.abilities.creativeMode && player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            if (!slotManager.maxSlotsUnlocked()) {
                ItemStack stack = itemEntity.getStack();
                if (!stack.isEmpty()) {
                    int slot = InventoryUtils.searchForPossibleSlots(slotManager, player.inventory, stack);
                    return !(slot == -1 || !slotManager.slotUnlocked(slot));
                }
            }
        }
        return true;
    }

    private static void updateClientSlots(ServerPlayerEntity player) {
        if (player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            MessageUtil.sendToClient(new MessageSlotClient(slotManager.getSlotsUnlocked(), ModConfig.reverseOrder), player);
        }
    }

    private static void onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
        if (ModConfig.loseSlots && (ModConfig.loseSlotNum == -1 || ModConfig.loseSlotNum > 0)) {
            TrophySlots.log.info("%s died. Losing %s slot(s).", player.getName().getText(), ModConfig.loseSlotNum);
            if (!player.world.isClient() && player instanceof ISlotHolder) {
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

    private static void clonePlayer(ServerPlayerEntity player, ServerPlayerEntity oldPlayer, boolean switichingDim) {
        if (!player.world.isClient() && player instanceof ISlotHolder && oldPlayer instanceof ISlotHolder) {
            PlayerSlotManager oldSlotManager = ((ISlotHolder) oldPlayer).getSlotManager();
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            slotManager.setSlotsUnlocked(oldSlotManager.getSlotsUnlocked());
        }
    }
}
