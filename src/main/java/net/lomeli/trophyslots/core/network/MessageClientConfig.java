package net.lomeli.trophyslots.core.network;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.screen.SlotRenderType;

public class MessageClientConfig implements IMessage {

    private final boolean special;
    private final SlotRenderType renderType;

    public MessageClientConfig() {
        this(true, SlotRenderType.NONE);
    }

    public MessageClientConfig(boolean special, SlotRenderType renderType) {
        this.special = special;
        this.renderType = renderType;
    }

    public static MessageClientConfig fromBytes(PacketBuffer buffer) {
        return new MessageClientConfig(buffer.readBoolean(), SlotRenderType.values()[buffer.readInt()]);
    }

    public static void toBytes(MessageClientConfig message, PacketBuffer buffer) {
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
