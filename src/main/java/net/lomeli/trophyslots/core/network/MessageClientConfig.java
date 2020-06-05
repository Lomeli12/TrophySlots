package net.lomeli.trophyslots.core.network;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.client.ClientConfig;

public class MessageClientConfig implements IMessage {

    private final boolean special;
    private final int renderType;

    public MessageClientConfig() {
        this(true, 0);
    }

    public MessageClientConfig(boolean special, int renderType) {
        this.special = special;
        this.renderType = renderType;
    }

    public static MessageClientConfig fromBytes(PacketBuffer buffer) {
        return new MessageClientConfig(buffer.readBoolean(), buffer.readInt());
    }

    public static void toBytes(MessageClientConfig message, PacketBuffer buffer) {
        buffer.writeBoolean(message.special);
        buffer.writeInt(message.renderType);
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
