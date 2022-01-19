package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.MessageSyncConfig;
import net.lomeli.trophyslots.core.network.MessageUnSyncConfig;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class PlayerHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide) return;
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
            if (playerSlots == null) return;

            if (CommonConfig.loseSlots && (CommonConfig.loseSlotNum == -1 || CommonConfig.loseSlotNum > 0)) {
                TrophySlots.log.info("{} died. Losing {} slot(s).", player.getName().getString(), CommonConfig.loseSlotNum);

                int slots = playerSlots.getSlotsUnlocked();
                if (CommonConfig.loseSlotNum == -1) slots = 0;
                else slots -= CommonConfig.loseSlotNum;
                playerSlots.setSlotsUnlocked(slots);

                String msg = CommonConfig.loseSlotNum == 1 ? "msg.trophyslots.lost_slot" :
                        CommonConfig.loseSlotNum == -1 ? "msg.trophyslots.lost_all" : "msg.trophyslots.lost_slot.multiple";
                if (CommonConfig.loseSlotNum > 1)
                    player.sendMessage(new TranslatableComponent(msg, CommonConfig.loseSlotNum), ChatType.CHAT,
                            Util.NIL_UUID);
                else player.sendMessage(new TranslatableComponent(msg), ChatType.CHAT, Util.NIL_UUID);
                PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
            }

            // Prevent capability data being lost on death
            PlayerSlotHelper.backupToPersist(player, playerSlots);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level.isClientSide || player.isCreative() || player.isDeadOrDying()) return;

        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (playerSlots.slotUnlocked(i)) continue;
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.isEmpty()) continue;
            int slot = InventoryUtils.getNextEmptySlot(playerSlots, player.getInventory());
            if (slot <= -1) {
                player.drop(stack, false);
                player.getInventory().setItem(i, ItemStack.EMPTY);
            } else player.getInventory().setItem(slot, player.getInventory().removeItem(i, stack.getCount()));
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getPlayer();
        if (player.level.isClientSide || player.isCreative()) return;
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;

        ItemStack stack = event.getItem().getItem();
        if (stack.isEmpty()) return;

        int slot = InventoryUtils.searchForPossibleSlots(playerSlots, player.getInventory(), stack);
        if (slot == -1 || !playerSlots.slotUnlocked(slot))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getPlayer() instanceof ServerPlayer)
            updateClientSlots((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer)
            updateClientSlots((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public static void playerChangedDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer() instanceof ServerPlayer)
            updateClientSlots((ServerPlayer) event.getPlayer());
    }

    private static void updateClientSlots(ServerPlayer player) {
        // This is done so that if the player logs into a server with a different config
        // to their client, then loads into a single player world, we reload their local
        // config to avoid cross-polluting configs
        PacketHandler.sendToClient(new MessageUnSyncConfig(), player);
        if (player.level.isClientSide)
            return;
        PacketHandler.sendToClient(new MessageSyncConfig(), player);
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots != null)
            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
    }
}
