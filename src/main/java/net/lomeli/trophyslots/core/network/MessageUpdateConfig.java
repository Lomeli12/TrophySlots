package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateConfig implements IMessage {
    private final String configKey;
    private Object value;

    public MessageUpdateConfig(String configKey, Object value) {
        this.configKey = configKey;
        this.value = value;
    }

    public static MessageUpdateConfig fromBytes(FriendlyByteBuf buffer) {
        var packet = new MessageUpdateConfig(buffer.readUtf(), null);
        switch (buffer.readEnum(DataType.class)) {
            case BOOLEAN -> packet.value = buffer.readBoolean();
            case INT -> packet.value = buffer.readInt();
            default -> TrophySlots.log.error("Sent null config packet. How did we get here? Config Key: {}",
                    packet.configKey);
        }
        return packet;
    }

    public static void toBytes(MessageUpdateConfig message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.configKey);
        if (message.value instanceof Boolean) {
            buffer.writeEnum(DataType.BOOLEAN);
            buffer.writeBoolean((Boolean) message.value);
        } else if (message.value instanceof Integer) {
            buffer.writeEnum(DataType.INT);
            buffer.writeInt((Integer) message.value);
        } else
            buffer.writeEnum(DataType.NULL); // Should never reach this point, but just in case...
    }

    public static void handle(MessageUpdateConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (message == null || message.value == null)
                return;

            switch (message.configKey) {
                case "listmode":
                    CommonConfig.setListMode(AdvancementHandler.ListMode.values()[(Integer) message.value]);
                case "startingslots":
                case "loseslotnum":
                    CommonConfig.setIntValue(message.configKey, (Integer) message.value);
                    break;
                default:
                    CommonConfig.setBoolValue(message.configKey, (Boolean) message.value);
            }
        });
        context.get().setPacketHandled(true);
    }

    private enum DataType {
        NULL, BOOLEAN, INT
    }
}
