package net.lomeli.trophyslots.core.network

import io.netty.buffer.ByteBuf
import net.lomeli.trophyslots.TrophySlots
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import java.util.*

public class MessageUpdateWhitelist : IMessage, IMessageHandler<MessageUpdateWhitelist, IMessage> {
    private val whiteList: ArrayList<String>

    constructor() {
        whiteList = ArrayList<String>()
    }

    constructor(whiteList: ArrayList<String>) {
        this.whiteList = whiteList
    }

    override fun fromBytes(buf: ByteBuf?) {
        val packetData = ByteBufUtils.readTag(buf)
        val tagList = packetData.getTagList("data", 8)
        var i = 0
        while (i < tagList.tagCount()) {
            whiteList.add(tagList.getStringTagAt(i))
            ++i;
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        val packetData = NBTTagCompound()
        val list = NBTTagList()
        for (i in whiteList.indices)
            list.appendTag(NBTTagString(whiteList[i]))
        packetData.setTag("data", list)
        ByteBufUtils.writeTag(buf, packetData)
    }

    override fun onMessage(message: MessageUpdateWhitelist?, ctx: MessageContext?): IMessage? {
        if (message != null)
            TrophySlots.proxy?.setWhiteList(message.whiteList)
        return message
    }
}