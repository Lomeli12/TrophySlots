package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSlotClient implements IMessage {
    private final int slots;

    public MessageSlotClient(int numSlots) {
        this.slots = numSlots;
    }

    public static MessageSlotClient fromBytes(FriendlyByteBuf buffer) {
        return new MessageSlotClient(buffer.readInt());
    }

    public static void toBytes(MessageSlotClient message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.slots);
    }

    public static void handle(MessageSlotClient message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (message == null)
                return;
            TrophySlots.proxy.updateSlots(message.slots);
        });
        context.get().setPacketHandled(true);
    }
}
