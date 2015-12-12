package net.lomeli.trophyslots.core.network

import io.netty.buffer.ByteBuf

import net.lomeli.trophyslots.TrophySlots
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

public class MessageOpenWhitelist() : IMessage, IMessageHandler<MessageOpenWhitelist, IMessage> {

    override fun toBytes(buf: ByteBuf?) {
    }

    override fun fromBytes(buf: ByteBuf?) {
    }

    override fun onMessage(message: MessageOpenWhitelist?, ctx: MessageContext?): IMessage? {
        TrophySlots.proxy?.openWhitelistGui()
        return message
    }
}