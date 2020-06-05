package net.lomeli.trophyslots.core.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;

public class MessageSlotClient implements IMessage {
    private int slots;

    public MessageSlotClient(int numSlots) {
        this.slots = numSlots;
    }

    public static MessageSlotClient fromBytes(PacketBuffer buffer) {
        return new MessageSlotClient(buffer.readInt());
    }

    public static void toBytes(MessageSlotClient message, PacketBuffer buffer) {
        buffer.writeInt(message.slots);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(MessageSlotClient message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (message == null)
                return;
            PlayerEntity player = Minecraft.getInstance().player;
            IPlayerSlots slots = PlayerSlotHelper.getPlayerSlots(player);
            if (slots != null)
                slots.setSlotsUnlocked(message.slots);
        });
        context.get().setPacketHandled(true);
    }
}
