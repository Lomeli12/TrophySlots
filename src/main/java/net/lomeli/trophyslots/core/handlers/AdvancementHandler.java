package net.lomeli.trophyslots.core.handlers;

import com.google.common.collect.Lists;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.MessageSlotClient;
import net.lomeli.trophyslots.core.network.PacketHandler;
import net.lomeli.trophyslots.items.ItemTrophy;
import net.lomeli.trophyslots.items.ModItems;
import net.lomeli.trophyslots.utils.NBTUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.BasicTrade;
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
            player.func_241151_a_(new TranslationTextComponent("msg.trophyslots.unlock"), ChatType.CHAT,
                    Util.field_240973_b_);
            TrophySlots.log.debug("Sending slot update packet to player {}", player.getName().func_230531_f_());
            PacketHandler.sendToClient(new MessageSlotClient(playerSlots.getSlotsUnlocked()), player);
            ModCriteria.UNLOCK_SLOT.trigger(player);
        }
    }

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (!ServerConfig.canBuyTrophy) return;
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            ItemStack villagerTrophy = new ItemStack(ModItems.trophy);
            NBTUtils.setBoolean(villagerTrophy, ItemTrophy.VILLAGER_TROPHY, true);
            List<VillagerTrades.ITrade> customTrades = Lists.newArrayList();
            customTrades.add(new BasicTrade(new ItemStack(Items.EMERALD, 5), new ItemStack(Items.DIAMOND),
                    villagerTrophy, 3, 10, 0.2f));
            event.getTrades().put(event.getTrades().size(), customTrades);
        }
    }

    public enum ListMode {
        WHITE, BLACK, NONE
    }
}
