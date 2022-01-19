package net.lomeli.trophyslots.core.network;

import com.google.common.collect.Lists;
import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.handlers.AdvancementHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MessageSyncConfig implements IMessage {
    private final int loseSlotNum;
    private final int startingSlots;
    private final boolean unlockViaAdvancements;
    private final boolean canUseTrophy;
    private final boolean canBuyTrophy;
    private final boolean loseSlots;
    private final boolean reverseOrder;
    private final List<ResourceLocation> advancementList;
    private final AdvancementHandler.ListMode listMode;

    public MessageSyncConfig() {
        this(CommonConfig.loseSlotNum, CommonConfig.startingSlots, CommonConfig.unlockViaAdvancements,
                CommonConfig.canUseTrophy, CommonConfig.canBuyTrophy, CommonConfig.loseSlots,
                CommonConfig.reverseOrder, CommonConfig.advancementList, CommonConfig.listMode);
    }

    public MessageSyncConfig(int loseSlotNum, int startingSlots, boolean unlockViaAdvancements, boolean canUseTrophy,
                             boolean canBuyTrophy, boolean loseSlots, boolean reverseOrder,
                             List<ResourceLocation> advancementList, AdvancementHandler.ListMode listMode) {
        this.loseSlotNum = loseSlotNum;
        this.startingSlots = startingSlots;
        this.unlockViaAdvancements = unlockViaAdvancements;
        this.canUseTrophy = canUseTrophy;
        this.canBuyTrophy = canBuyTrophy;
        this.loseSlots = loseSlots;
        this.reverseOrder = reverseOrder;
        this.advancementList = Lists.newArrayList();
        if (advancementList != null)
            this.advancementList.addAll(advancementList);
        this.listMode = listMode;
    }

    public static MessageSyncConfig fromBytes(FriendlyByteBuf buffer) {
        return new MessageSyncConfig(buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(),
                buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(),
                buffer.readList(FriendlyByteBuf::readResourceLocation),
                buffer.readEnum(AdvancementHandler.ListMode.class));
    }

    public static void toBytes(MessageSyncConfig message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.loseSlotNum);
        buffer.writeInt(message.startingSlots);
        buffer.writeBoolean(message.unlockViaAdvancements);
        buffer.writeBoolean(message.canUseTrophy);
        buffer.writeBoolean(message.canBuyTrophy);
        buffer.writeBoolean(message.loseSlots);
        buffer.writeBoolean(message.reverseOrder);
        buffer.writeCollection(message.advancementList, FriendlyByteBuf::writeResourceLocation);
        buffer.writeEnum(message.listMode);
    }

    public static void handle(MessageSyncConfig message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (message == null)
                return;

            CommonConfig.loseSlotNum = message.loseSlotNum;
            CommonConfig.startingSlots = message.startingSlots;
            CommonConfig.unlockViaAdvancements = message.unlockViaAdvancements;
            CommonConfig.canUseTrophy = message.canUseTrophy;
            CommonConfig.canBuyTrophy = message.canBuyTrophy;
            CommonConfig.loseSlots = message.loseSlots;
            CommonConfig.reverseOrder = message.reverseOrder;
            CommonConfig.advancementList.clear();
            if (message.advancementList != null)
                CommonConfig.advancementList.addAll(message.advancementList);
            CommonConfig.listMode = message.listMode;

        });
        context.get().setPacketHandled(true);
    }
}
