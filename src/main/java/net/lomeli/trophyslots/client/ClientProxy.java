package net.lomeli.trophyslots.client;

import net.lomeli.trophyslots.core.IProxy;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientProxy implements IProxy {
    @Override
    public void updateSlots(int slotAmount) {
        PlayerEntity player = Minecraft.getInstance().player;
        IPlayerSlots slots = PlayerSlotHelper.getPlayerSlots(player);
        if (slots != null)
            slots.setSlotsUnlocked(slotAmount);
    }
}
