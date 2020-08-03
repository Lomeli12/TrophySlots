package net.lomeli.trophyslots.core.network;

import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageServerConfig implements IMessage {

    private final boolean advancementUnlock;
    private final boolean useTrophies;
    private final boolean buyTrophies;
    private final boolean reverseOrder;
    private final boolean loseSlots;
    private final int losingSlots;
    private final int startingSlots;
    private final AdvancementHandler.ListMode listMode;

    public MessageServerConfig() {
        advancementUnlock = ServerConfig.unlockViaAdvancements;
        useTrophies = ServerConfig.canUseTrophy;
        buyTrophies = ServerConfig.canBuyTrophy;
        reverseOrder = ServerConfig.reverseOrder;
        loseSlots = ServerConfig.loseSlots;
        losingSlots = ServerConfig.loseSlotNum;
        startingSlots = ServerConfig.startingSlots;
        listMode = ServerConfig.listMode;
    }

    public MessageServerConfig(boolean advancementUnlock, boolean useTrophies, boolean buyTrophies, boolean reverseOrder,
                               boolean loseSlots, int losingSlots, int startingSlots, AdvancementHandler.ListMode listMode) {
        this.advancementUnlock = advancementUnlock;
        this.useTrophies = useTrophies;
        this.buyTrophies = buyTrophies;
        this.reverseOrder = reverseOrder;
        this.loseSlots = loseSlots;
        this.losingSlots = losingSlots;
        this.startingSlots = startingSlots;
        this.listMode = listMode;
    }

    public static MessageServerConfig fromBytes(PacketBuffer buffer) {
        return new MessageServerConfig(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(),
                buffer.readBoolean(), buffer.readBoolean(), buffer.readInt(), buffer.readInt(),
                AdvancementHandler.ListMode.values()[buffer.readInt()]);
    }

    public static void toBytes(MessageServerConfig message, PacketBuffer buffer) {
        buffer.writeBoolean(message.advancementUnlock);
        buffer.writeBoolean(message.useTrophies);
        buffer.writeBoolean(message.buyTrophies);
        buffer.writeBoolean(message.reverseOrder);
        buffer.writeBoolean(message.loseSlots);
        buffer.writeInt(message.losingSlots);
        buffer.writeInt(message.startingSlots);
        buffer.writeInt(message.listMode.ordinal());
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
            ServerConfig.setListMode(message.listMode);
            ServerConfig.reloadConfig();
        });
        context.get().setPacketHandled(true);
    }
}
