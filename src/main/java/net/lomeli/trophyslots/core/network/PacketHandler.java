package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(TrophySlots.MOD_ID, "main_channel");
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void registerPackets() {
        int packetID = 0;
        HANDLER.registerMessage(packetID++, MessageUpdateConfig.class, MessageUpdateConfig::toBytes,
                MessageUpdateConfig::fromBytes, MessageUpdateConfig::handle);
        HANDLER.registerMessage(packetID++, MessageSlotClient.class, MessageSlotClient::toBytes,
                MessageSlotClient::fromBytes, MessageSlotClient::handle);
        HANDLER.registerMessage(packetID++, MessageClientConfig.class, MessageClientConfig::toBytes,
                MessageClientConfig::fromBytes, MessageClientConfig::handle);
        HANDLER.registerMessage(packetID++, MessageSyncConfig.class, MessageSyncConfig::toBytes,
                MessageSyncConfig::fromBytes, MessageSyncConfig::handle);
        HANDLER.registerMessage(packetID, MessageUnSyncConfig.class, MessageUnSyncConfig::toBytes,
                MessageUnSyncConfig::fromBytes, MessageUnSyncConfig::handle);
    }

    public static void sendToServer(IMessage message) {
        HANDLER.sendToServer(message);
    }

    public static void sendToClient(IMessage message, ServerPlayer player) {
        HANDLER.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
