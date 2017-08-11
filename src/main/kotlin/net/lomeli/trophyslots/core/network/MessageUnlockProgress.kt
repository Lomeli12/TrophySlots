package net.lomeli.trophyslots.core.network

import io.netty.buffer.ByteBuf
import net.lomeli.trophyslots.capabilities.progression.ProgressionManager
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessageUnlockProgress : IMessage, IMessageHandler<MessageUnlockProgress, IMessage> {
    private var id = ""

    constructor()

    constructor(id: String) {
        this.id = id
    }

    override fun fromBytes(buf: ByteBuf?) {
        id = ByteBufUtils.readUTF8String(buf)
    }

    override fun toBytes(buf: ByteBuf?) {
        ByteBufUtils.writeUTF8String(buf, id)
    }

    override fun onMessage(message: MessageUnlockProgress?, ctx: MessageContext?): IMessage? {
        if (ctx == null) return null
        val player = ctx.serverHandler.player
        val progression = ProgressionManager.getPlayerProgressionData(player)
        if (progression != null && progression.givePlayerProgression(message!!.id))
            ProgressionManager.progressionMessage(player, message.id)
        return null
    }
}