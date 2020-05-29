package net.lomeli.trophyslots.core.network;

import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

import net.lomeli.trophyslots.core.ServerConfig;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageServerConfig implements IMessage {

    private boolean advancementUnlock;
    private boolean useTrophies;
    private boolean buyTrophies;
    private boolean reverseOrder;
    private boolean loseSlots;
    private int losingSlots;
    private int startingSlots;

    public MessageServerConfig() {
        advancementUnlock = ServerConfig.unlockViaAdvancements;
        useTrophies = ServerConfig.canUseTrophy;
        buyTrophies = ServerConfig.canBuyTrophy;
        reverseOrder = ServerConfig.reverseOrder;
        loseSlots = ServerConfig.loseSlots;
        losingSlots = ServerConfig.loseSlotNum;
        startingSlots = ServerConfig.startingSlots;
    }

    private MessageServerConfig(boolean advancementUnlock, boolean useTrophies, boolean buyTrophies, boolean reverseOrder,
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

            ServerConfig.unlockViaAdvancements = message.advancementUnlock;
            ServerConfig.canUseTrophy = message.useTrophies;
            ServerConfig.canBuyTrophy = message.buyTrophies;
            ServerConfig.reverseOrder = message.reverseOrder;
            ServerConfig.loseSlots = message.loseSlots;
            ServerConfig.loseSlotNum = message.losingSlots;
            ServerConfig.startingSlots = message.startingSlots;
        });
    }
}
