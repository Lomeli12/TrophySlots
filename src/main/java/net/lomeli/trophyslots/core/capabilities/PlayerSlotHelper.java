package net.lomeli.trophyslots.core.capabilities;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerSlotHelper {
    @SuppressWarnings("all")
    public static IPlayerSlots getPlayerSlots(PlayerEntity player) {
        if (player == null || player instanceof FakePlayer)
            return null;
        return player.getCapability(PlayerSlotProvider.SLOTS, null)
                .orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
    }
}
