package net.lomeli.trophyslots.items;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
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
}