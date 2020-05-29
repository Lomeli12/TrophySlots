package net.lomeli.trophyslots.core.network;

import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.core.handlers.ModConfigHandler;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageClientConfig implements IMessage {

    private boolean special;
    private int renderType;

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
                ModConfigHandler.setValueAndSave(ClientConfig.clientConfig, "special", message.special);
                ModConfigHandler.setValueAndSave(ClientConfig.clientConfig, "slotRenderType", message.renderType);
                ClientConfig.bakeConfig(ClientConfig.clientConfig);
            }
        });
    }
}
