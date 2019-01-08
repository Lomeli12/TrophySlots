package net.lomeli.trophyslots.items;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static Trophy trophy;
    public static Trophy masterTrophy;

    public static void init() {
        trophy = new Trophy(Trophy.TrophyType.NORMAL);
        masterTrophy = new Trophy(Trophy.TrophyType.MASTER);

        Registry.ITEM.register(new Identifier(TrophySlots.MOD_ID, "trophy"), trophy);
        Registry.ITEM.register(new Identifier(TrophySlots.MOD_ID, "master_trophy"), masterTrophy);
    }
}
