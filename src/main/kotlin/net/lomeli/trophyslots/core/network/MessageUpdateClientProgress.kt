package net.lomeli.trophyslots.core.network

import io.netty.buffer.ByteBuf
import net.lomeli.trophyslots.capabilities.progression.ProgressionManager
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class MessageUpdateClientProgress : IMessage, IMessageHandler<MessageUpdateClientProgress, IMessage> {
    private val progress = ArrayList<String>()

    constructor()

    constructor(progress: List<String>) {
        this.progress.addAll(progress)
    }

    override fun fromBytes(buf: ByteBuf?) {
        val size = buf!!.readInt()
        if (size > 0) {
            for (i in 0..size) if (i < progress.size)
                progress.add(ByteBufUtils.readUTF8String(buf))
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf!!.writeInt(progress.size)
        if (progress.size > 0) {
            for (i in progress.indices) if (i < progress.size)
                ByteBufUtils.writeUTF8String(buf, progress[i])
        }
    }

    @SideOnly(Side.CLIENT) override fun onMessage(message: MessageUpdateClientProgress?, ctx: MessageContext?): IMessage? {
        if (ctx == null) return null
        val player = FMLClientHandler.instance().clientPlayerEntity
        val progression = ProgressionManager.getPlayerProgressionData(player)!!
        if (message!!.progress.isNotEmpty())
            message.progress.stream().forEach { id -> progression.forceAddProgression(id) }
        return null
    }
}