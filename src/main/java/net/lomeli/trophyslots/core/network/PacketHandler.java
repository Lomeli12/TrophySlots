package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        HANDLER.registerMessage(packetID++, MessageServerConfig.class, MessageServerConfig::toBytes,
                MessageServerConfig::fromBytes, MessageServerConfig::handle);
        HANDLER.registerMessage(packetID++, MessageSlotClient.class, MessageSlotClient::toBytes,
                MessageSlotClient::fromBytes, MessageSlotClient::handle);
        HANDLER.registerMessage(packetID, MessageClientConfig.class, MessageClientConfig::toBytes,
                MessageClientConfig::fromBytes, MessageClientConfig::handle);
    }

    public static void sendToServer(IMessage message) {
        HANDLER.sendToServer(message);
    }

    public static void sendToClient(IMessage message, ServerPlayerEntity player) {
        HANDLER.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
}
