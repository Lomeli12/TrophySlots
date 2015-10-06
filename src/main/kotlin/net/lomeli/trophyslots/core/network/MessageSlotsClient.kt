package net.lomeli.trophyslots.core.network

import io.netty.buffer.ByteBuf
import net.lomeli.trophyslots.TrophySlots
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

public class MessageSlotsClient : IMessage, IMessageHandler<MessageSlotsClient, IMessage> {
    private var slots = 0
    private var startingSlots = 0
    private var setOrder = false
    private var reverse = false

    constructor(){
    }

    constructor(slots: Int) {
        this.slots = slots;
        setOrder = false
    }

    constructor(slots: Int, reverse: Boolean, startingSlots: Int) {
        this.slots = slots
        this.setOrder = true
        this.reverse = reverse
        this.startingSlots = startingSlots
    }

    override fun fromBytes(buf: ByteBuf) {
        this.slots = buf.readInt()
        this.setOrder = buf.readBoolean()
        if (this.setOrder) {
            this.reverse = buf.readBoolean()
            this.startingSlots = buf.readInt()
        }
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(this.slots)
        buf.writeBoolean(this.setOrder)
        if (this.setOrder) {
            buf.writeBoolean(this.reverse)
            buf.writeInt(this.startingSlots)
        }
    }

    override fun onMessage(message: MessageSlotsClient?, ctx: MessageContext?): IMessage? {
        if (message != null) {
            TrophySlots.proxy.setSlotsUnlocked(message.slots)
            if (message.setOrder) {
                TrophySlots.proxy.setReverse(message.reverse)
                TrophySlots.proxy.startingSlots = message.startingSlots
            }
        }
        return null
    }
}