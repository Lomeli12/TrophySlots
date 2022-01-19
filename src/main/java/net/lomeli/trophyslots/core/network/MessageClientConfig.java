package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.screen.SlotRenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MessageClientConfig(boolean special,
                                  SlotRenderType renderType) implements IMessage {

    public static MessageClientConfig fromBytes(FriendlyByteBuf buffer) {
        return new MessageClientConfig(buffer.readBoolean(), SlotRenderType.values()[buffer.readInt()]);
    }

    public static void toBytes(MessageClientConfig message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.special);
        buffer.writeInt(message.renderType.ordinal());
    }

    public static void handle(MessageClientConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (ClientConfig.clientConfig != null) {
                ClientConfig.setSpecial(message.special);
                ClientConfig.setSlotRenderType(message.renderType);
                ClientConfig.reloadConfig();
            }
        });
        context.get().setPacketHandled(true);
    }
}
