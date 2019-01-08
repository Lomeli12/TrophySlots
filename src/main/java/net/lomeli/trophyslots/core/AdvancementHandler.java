package net.lomeli.trophyslots.core;

import net.minecraft.advancement.SimpleAdvancement;
import net.minecraft.server.network.ServerPlayerEntity;

public class AdvancementHandler {

    public static void unlockedAdvancment(ServerPlayerEntity player, SimpleAdvancement advancement) {
        if (advancement.getDisplay() == null || !advancement.getDisplay().shouldAnnounceToChat()) return;
        if (player.world.isClient || player.abilities.creativeMode) return;
        System.out.println("[Trophy Slots]: " + player.getDisplayName().getText() + " unlocked advancement " +
                advancement.getDisplay().getTitle().getText());
    }
}
