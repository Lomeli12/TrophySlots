package net.lomeli.trophyslots.items;

import net.lomeli.knit.utils.ItemNBTUtils;
import net.lomeli.knit.utils.RegistryUtil;
import net.lomeli.knit.utils.trades.CurrencyItem;
import net.lomeli.knit.utils.trades.CustomTradeFactory;
import net.lomeli.knit.utils.trades.TradeUtils;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.village.Trades;
import net.minecraft.village.VillagerProfession;

public class ModItems {
    private static Trophy trophy;
    private static Trophy masterTrophy;

    public static void init() {
        trophy = new Trophy(Trophy.TrophyType.NORMAL);
        masterTrophy = new Trophy(Trophy.TrophyType.MASTER);

        RegistryUtil.registerItem(new Identifier(TrophySlots.MOD_ID, "trophy"), trophy);
        RegistryUtil.registerItem(new Identifier(TrophySlots.MOD_ID, "master_trophy"), masterTrophy);
    }

    public static void addTrades() {
        ItemStack villagerTrophy = new ItemStack(trophy);
        ItemNBTUtils.setBoolean(villagerTrophy, Trophy.VILLAGER_TROPHY, true);
        TradeUtils.addTradeToProfession(VillagerProfession.LIBRARIAN, 3, new Trades.Factory[]{
                new CustomTradeFactory(villagerTrophy,
                        new CurrencyItem(Items.EMERALD, 3, 5),
                        new CurrencyItem(Items.DIAMOND, 1, 5))
        });
    }
}
