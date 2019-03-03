package net.lomeli.trophyslots.core.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.lomeli.knit.network.AbstractMessage;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.ModConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MessageServerConfig extends AbstractMessage<MessageServerConfig> {
    private static final Identifier UPDATE_CLIENT_CONFIG = new Identifier(TrophySlots.MOD_ID, "update_client_config");

    private boolean advancementUnlock;
    private boolean useTrophies;
    private boolean buyTrophies;
    private boolean reverseOrder;
    private boolean loseSlots;
    private int losingSlots;
    private int startingSlots;

    public MessageServerConfig() {
        advancementUnlock = ModConfig.unlockViaAdvancements;
        useTrophies = ModConfig.canUseTrophy;
        buyTrophies = ModConfig.canBuyTrophy;
        reverseOrder = ModConfig.reverseOrder;
        loseSlots = ModConfig.loseSlots;
        losingSlots = ModConfig.loseSlotNum;
        startingSlots = ModConfig.startingSlots;
    }

    @Override
    public MessageServerConfig createMessage() {
        return new MessageServerConfig();
    }

    @Override
    public void toBytes(PacketByteBuf byteBuf) {
        byteBuf.writeBoolean(advancementUnlock);
        byteBuf.writeBoolean(useTrophies);
        byteBuf.writeBoolean(buyTrophies);
        byteBuf.writeBoolean(reverseOrder);
        byteBuf.writeBoolean(loseSlots);
        byteBuf.writeInt(losingSlots);
        byteBuf.writeInt(startingSlots);
    }

    @Override
    public void fromBytes(PacketByteBuf byteBuf) {
        advancementUnlock = byteBuf.readBoolean();
        useTrophies = byteBuf.readBoolean();
        buyTrophies = byteBuf.readBoolean();
        reverseOrder = byteBuf.readBoolean();
        loseSlots = byteBuf.readBoolean();
        losingSlots = byteBuf.readInt();
        startingSlots = byteBuf.readInt();
    }

    @Override
    public Identifier getMessageID() {
        return UPDATE_CLIENT_CONFIG;
    }

    @Override
    public void handle(PacketContext context, MessageServerConfig message) {
        if (message == null)
            return;

        ModConfig.unlockViaAdvancements = message.advancementUnlock;
        ModConfig.canUseTrophy = message.useTrophies;
        ModConfig.canBuyTrophy = message.buyTrophies;
        ModConfig.reverseOrder = message.reverseOrder;
        ModConfig.loseSlots = message.loseSlots;
        ModConfig.loseSlotNum = message.losingSlots;
        ModConfig.startingSlots = message.startingSlots;
    }
}
