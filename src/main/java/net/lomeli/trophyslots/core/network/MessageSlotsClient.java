package net.lomeli.trophyslots.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.trophyslots.TrophySlots;

public class MessageSlotsClient implements IMessage, IMessageHandler<MessageSlotsClient, IMessage> {
    private int slots;

    public MessageSlotsClient() {
    }

    public MessageSlotsClient(int slots) {
        this.slots = slots;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slots = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slots);
    }

    @Override
    public IMessage onMessage(MessageSlotsClient message, MessageContext ctx) {
        TrophySlots.proxy.setSlotsUnlocked(message.slots);
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation(message.slots >= 36 ? "msg.trophyslots.unlockAll" : "msg.trophyslots.unlock"));
        return null;
    }
}
