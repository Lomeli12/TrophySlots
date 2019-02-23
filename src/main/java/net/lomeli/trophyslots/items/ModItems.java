package net.lomeli.trophyslots.items;

import net.lomeli.knit.utils.RegistryUtil;
import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.util.Identifier;

public class ModItems {
    private static Trophy trophy;
    private static Trophy masterTrophy;

    public static void init() {
        trophy = new Trophy(Trophy.TrophyType.NORMAL);
        masterTrophy = new Trophy(Trophy.TrophyType.MASTER);

        RegistryUtil.registerItem(new Identifier(TrophySlots.MOD_ID, "trophy"), trophy);
        RegistryUtil.registerItem(new Identifier(TrophySlots.MOD_ID, "master_trophy"), masterTrophy);
    }
}
