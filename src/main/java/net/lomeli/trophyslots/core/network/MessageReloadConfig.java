package net.lomeli.trophyslots.core.network;

import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.client.ClientConfig;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageReloadConfig implements IMessage {

    public MessageReloadConfig() {
    }

    public static MessageReloadConfig fromBytes(PacketBuffer buffer) {
        return new MessageReloadConfig();
    }

    public static void toBytes(MessageReloadConfig message, PacketBuffer buffer) {
    }

    public static void handle(MessageReloadConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (ClientConfig.clientConfig != null)
                ClientConfig.bakeConfig(ClientConfig.clientConfig);
        });
    }
}
