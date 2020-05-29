package net.lomeli.trophyslots.core.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.utils.InventoryUtils;
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
        if (ServerConfig.loseSlots && (ServerConfig.loseSlotNum == -1 || ServerConfig.loseSlotNum > 0)) {
            if (event.getEntityLiving() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
                TrophySlots.log.info("{} died. Losing {} slot(s).", player.getName().getFormattedText(), ServerConfig.loseSlotNum);

                if (player.world.isRemote) return;
                IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                if (playerSlots == null) return;

                int slots = playerSlots.getSlotsUnlocked();
                if (ServerConfig.loseSlotNum == -1) slots = 0;
                else slots -= ServerConfig.loseSlotNum;
                playerSlots.setSlotsUnlocked(slots);

                String msg = ServerConfig.loseSlotNum == 1 ? "msg.trophyslots.lost_slot" :
                        ServerConfig.loseSlotNum == -1 ? "msg.trophyslots.lost_all" : "msg.trophyslots.lost_slot.multiple";
                if (ServerConfig.loseSlotNum > 1)
                    player.sendMessage(new TranslationTextComponent(msg, ServerConfig.loseSlotNum), ChatType.CHAT);
                else player.sendMessage(new TranslationTextComponent(msg), ChatType.CHAT);
                PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        if (player.world.isRemote || player.abilities.isCreativeMode) return;

        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            if (playerSlots.slotUnlocked(i)) continue;
            ItemStack stack = player.inventory.mainInventory.get(i);
            if (stack.isEmpty()) continue;
            int slot = InventoryUtils.getNextEmptySlot(playerSlots, player.inventory);
            if (slot <= -1) {
                player.entityDropItem(stack, 0f);
                player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
            } else player.inventory.setInventorySlotContents(slot, player.inventory.removeStackFromSlot(i));
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote || player.abilities.isCreativeMode) return;
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;

        ItemStack stack = event.getItem().getItem();
        if (stack.isEmpty()) return;

        int slot = InventoryUtils.searchForPossibleSlots(playerSlots, player.inventory, stack);
        if (slot == -1 || !playerSlots.slotUnlocked(slot))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity)
            updateClientSlots((ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedInt(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity)
            updateClientSlots((ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    private static void updateClientSlots(ServerPlayerEntity player) {
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null) return;
        //Don't need this anymore because Forge now syncs server configs to the client
        //if (updateConfig)
        //    PacketHandler.sendToClient(new MessageServerConfig(), player);
        PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
    }
}
