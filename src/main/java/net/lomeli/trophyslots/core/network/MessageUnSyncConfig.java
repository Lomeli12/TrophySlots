package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.core.CommonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUnSyncConfig implements IMessage {
    public static MessageUnSyncConfig fromBytes(FriendlyByteBuf buffer) {
        return new MessageUnSyncConfig();
    }

    public static void toBytes(MessageUnSyncConfig message, FriendlyByteBuf buffer) {
    }

    public static void handle(MessageUnSyncConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(CommonConfig::reloadConfig);
        context.get().setPacketHandled(true);
    }
}
