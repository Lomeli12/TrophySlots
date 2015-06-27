package net.lomeli.trophyslots.core.network;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.trophyslots.TrophySlots;

public class MessageOpenWhitelist implements IMessage, IMessageHandler<MessageOpenWhitelist, IMessage> {

    public MessageOpenWhitelist() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(MessageOpenWhitelist message, MessageContext ctx) {
        TrophySlots.proxy.openWhitelistGui();
        return null;
    }
}
