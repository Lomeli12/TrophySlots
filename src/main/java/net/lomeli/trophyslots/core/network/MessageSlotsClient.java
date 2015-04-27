package net.lomeli.trophyslots.core.network;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.trophyslots.TrophySlots;

public class MessageSlotsClient implements IMessage, IMessageHandler<MessageSlotsClient, IMessage> {
    private int slots, startingSlots;
    private boolean setOrder, reverse;

    public MessageSlotsClient() {
    }

    public MessageSlotsClient(int slots) {
        this.slots = slots;
        this.setOrder = false;
    }

    public MessageSlotsClient(int slots, boolean reverse, int startingSlots) {
        this.slots = slots;
        this.setOrder = true;
        this.reverse = reverse;
        this.startingSlots = startingSlots;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slots = buf.readInt();
        this.setOrder = buf.readBoolean();
        if (this.setOrder) {
            this.reverse = buf.readBoolean();
            this.startingSlots = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slots);
        buf.writeBoolean(this.setOrder);
        if (this.setOrder) {
            buf.writeBoolean(this.reverse);
            buf.writeInt(this.startingSlots);
        }
    }

    @Override
    public IMessage onMessage(MessageSlotsClient message, MessageContext ctx) {
        TrophySlots.proxy.setSlotsUnlocked(message.slots);
        if (message.setOrder) {
            TrophySlots.proxy.setReverse(message.reverse);
            TrophySlots.proxy.setStartingSlots(message.startingSlots);
        }
        return null;
    }
}
