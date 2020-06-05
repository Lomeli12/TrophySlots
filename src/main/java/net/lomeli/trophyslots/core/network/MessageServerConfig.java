package net.lomeli.trophyslots.core.network;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.core.ServerConfig;

public class MessageServerConfig implements IMessage {

    private final boolean advancementUnlock;
    private final boolean useTrophies;
    private final boolean buyTrophies;
    private final boolean reverseOrder;
    private final boolean loseSlots;
    private final int losingSlots;
    private final int startingSlots;

    public MessageServerConfig() {
        advancementUnlock = ServerConfig.unlockViaAdvancements;
        useTrophies = ServerConfig.canUseTrophy;
        buyTrophies = ServerConfig.canBuyTrophy;
        reverseOrder = ServerConfig.reverseOrder;
        loseSlots = ServerConfig.loseSlots;
        losingSlots = ServerConfig.loseSlotNum;
        startingSlots = ServerConfig.startingSlots;
    }

    public MessageServerConfig(boolean advancementUnlock, boolean useTrophies, boolean buyTrophies, boolean reverseOrder,
                               boolean loseSlots, int losingSlots, int startingSlots) {
        this.advancementUnlock = advancementUnlock;
        this.useTrophies = useTrophies;
        this.buyTrophies = buyTrophies;
        this.reverseOrder = reverseOrder;
        this.loseSlots = loseSlots;
        this.losingSlots = losingSlots;
        this.startingSlots = startingSlots;
    }

    public static MessageServerConfig fromBytes(PacketBuffer buffer) {
        return new MessageServerConfig(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(),
                buffer.readBoolean(), buffer.readBoolean(), buffer.readInt(), buffer.readInt());
    }

    public static void toBytes(MessageServerConfig message, PacketBuffer buffer) {
        buffer.writeBoolean(message.advancementUnlock);
        buffer.writeBoolean(message.useTrophies);
        buffer.writeBoolean(message.buyTrophies);
        buffer.writeBoolean(message.reverseOrder);
        buffer.writeBoolean(message.loseSlots);
        buffer.writeInt(message.losingSlots);
        buffer.writeInt(message.startingSlots);
    }

    public static void handle(MessageServerConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (message == null)
                return;

            ServerConfig.setBoolValue("unlockViaAdvancements", message.advancementUnlock);
            ServerConfig.setBoolValue("canUseTrophy", message.useTrophies);
            ServerConfig.setBoolValue("canBuyTrophy", message.buyTrophies);
            ServerConfig.setBoolValue("reverseOrder", message.reverseOrder);
            ServerConfig.setBoolValue("loseSlots", message.loseSlots);
            ServerConfig.setIntValue("loseSlotNum", message.losingSlots);
            ServerConfig.setIntValue("startingSlots", message.startingSlots);
            ServerConfig.reloadConfig();
        });
        context.get().setPacketHandled(true);
    }
}
