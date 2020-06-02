package net.lomeli.trophyslots.items;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.utils.NBTUtils;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    @ObjectHolder(TrophySlots.MOD_ID + ":trophy")
    public static ItemTrophy trophy;
    @ObjectHolder(TrophySlots.MOD_ID + ":master_trophy")
    public static ItemTrophy masterTrophy;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(trophy = new ItemTrophy(ItemTrophy.TrophyType.NORMAL),
                masterTrophy = new ItemTrophy(ItemTrophy.TrophyType.MASTER));
    }

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (!ServerConfig.canBuyTrophy) return;
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            ItemStack villagerTrophy = new ItemStack(trophy);
            NBTUtils.setBoolean(villagerTrophy, ItemTrophy.VILLAGER_TROPHY, true);
            List<VillagerTrades.ITrade> customTrades = Lists.newArrayList();
            customTrades.add(new BasicTrade(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.DIAMOND),
                    villagerTrophy, 3, 10, 0.2f));
            event.getTrades().put(event.getTrades().size(), customTrades);
        }
    }
}