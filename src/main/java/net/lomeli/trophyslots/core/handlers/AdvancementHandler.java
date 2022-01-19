package net.lomeli.trophyslots.core.handlers;

import com.google.common.collect.Lists;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.items.ItemTrophy;
import net.lomeli.trophyslots.items.ModItems;
import net.lomeli.trophyslots.utils.NBTUtils;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID)
public class AdvancementHandler {

    @SubscribeEvent
    public static void advancementEvent(AdvancementEvent event) {
        if (!CommonConfig.unlockViaAdvancements) return;
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        if (player instanceof FakePlayer || player.isCreative()) return;
        Advancement advancement = event.getAdvancement();
        if (advancement.getId().getNamespace().equalsIgnoreCase(TrophySlots.MOD_ID) || advancement.getDisplay() == null
                || !advancement.getDisplay().shouldAnnounceChat()) return;
        if (CommonConfig.listMode == ListMode.WHITE && !CommonConfig.advancementList.contains(advancement.getId()))
            return;
        if (CommonConfig.listMode == ListMode.BLACK && CommonConfig.advancementList.contains(advancement.getId()))
            return;
        IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
        if (playerSlots == null || playerSlots.maxSlotsUnlocked()) return;
        if (playerSlots.unlockSlot(1)) {
            player.sendMessage(new TranslatableComponent("msg.trophyslots.unlock"), ChatType.CHAT,
                    Util.NIL_UUID);
            TrophySlots.log.debug("Sending slot update packet to player {}", player.getName().getString());
            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
            ModCriteria.UNLOCK_SLOT.trigger(player);
        }
    }

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (!CommonConfig.canBuyTrophy) return;
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            ItemStack villagerTrophy = new ItemStack(ModItems.trophy);
            NBTUtils.setBoolean(villagerTrophy, ItemTrophy.VILLAGER_TROPHY, true);
            List<VillagerTrades.ItemListing> customTrades = Lists.newArrayList();
            customTrades.add(new BasicItemListing(new ItemStack(Items.EMERALD, 5), new ItemStack(Items.DIAMOND),
                    villagerTrophy, 3, 10, 0.2f));
            event.getTrades().put(event.getTrades().size(), customTrades);
        }
    }

    public enum ListMode {
        WHITE, BLACK, NONE
    }
}
