package net.lomeli.trophyslots.core.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.trophyslots.TrophySlots;

public class MessageUpdateWhitelist implements IMessage, IMessageHandler<MessageUpdateWhitelist, IMessage> {
    private List<String> whiteList;

    public MessageUpdateWhitelist() {
    }

    public MessageUpdateWhitelist(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        whiteList = new ArrayList<String>();
        NBTTagCompound packetData = ByteBufUtils.readTag(buf);
        NBTTagList tagList = packetData.getTagList("data", 8);
        for (int i = 0; i < tagList.tagCount(); i++) {
            whiteList.add(tagList.getStringTagAt(i));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound packetData = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (String string : whiteList) {
            list.appendTag(new NBTTagString(string));
        }
        packetData.setTag("data", list);
        ByteBufUtils.writeTag(buf, packetData);
    }

    @Override
    public IMessage onMessage(MessageUpdateWhitelist message, MessageContext ctx) {
        TrophySlots.proxy.setWhiteList(message.whiteList);
        return null;
    }
}
