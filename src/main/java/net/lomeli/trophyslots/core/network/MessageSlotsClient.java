package net.lomeli.trophyslots.core.network;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.trophyslots.TrophySlots;

public class MessageSlotsClient implements IMessage, IMessageHandler<MessageSlotsClient, IMessage> {
    private int slots;
    private boolean setOrder, reverse;

    public MessageSlotsClient() {
    }

    public MessageSlotsClient(int slots) {
        this.slots = slots;
        this.setOrder = false;
    }

    public MessageSlotsClient(int slots, boolean reverse) {
        this.slots = slots;
        this.setOrder = true;
        this.reverse = reverse;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slots = buf.readInt();
        this.setOrder = buf.readBoolean();
        if (this.setOrder)
            this.reverse = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slots);
        buf.writeBoolean(this.setOrder);
        if (this.setOrder)
            buf.writeBoolean(this.reverse);
    }

    @Override
    public IMessage onMessage(MessageSlotsClient message, MessageContext ctx) {
        TrophySlots.proxy.setSlotsUnlocked(message.slots);
        if (message.setOrder)
            TrophySlots.proxy.setReverse(message.reverse);
        return null;
    }
}
