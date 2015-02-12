package net.lomeli.trophyslots.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.handler.EventHandlerServer;
import net.lomeli.trophyslots.core.handler.VillagerHandler;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;

public class Proxy {

    public void preInit() {
        ModItems.registerItems();
        VillagerRegistry.instance().registerVillageTradeHandler(1, new VillagerHandler());
    }

    public void init() {
        EventHandlerServer eventHandlerServer = new EventHandlerServer();
        registerFMLEvent(eventHandlerServer);
        registerForgeEvent(eventHandlerServer);
    }

    protected void registerFMLEvent(Object obj) {
        FMLCommonHandler.instance().bus().register(obj);
    }

    protected void registerForgeEvent(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    public boolean unlockSlot(EntityPlayer player) {
        if (player != null && !SlotUtil.hasUnlockedAllSlots(player)) {
            int i = SlotUtil.getSlotsUnlocked(player) + 1;
            SlotUtil.setSlotsUnlocked(player, i);
            player.addChatComponentMessage(new ChatComponentTranslation(i >= 36 ? "msg.trophyslots.unlockAll" : "msg.trophyslots.unlock"));
            EntityPlayer mp = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID());
            if (mp != null)
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(i), (EntityPlayerMP) player);
            return true;
        }
        return false;
    }

    public boolean unlockAllSlots(EntityPlayer player) {
        if (player != null && !SlotUtil.hasUnlockedAllSlots(player)) {
            SlotUtil.setSlotsUnlocked(player, 36);
            player.addChatComponentMessage(new ChatComponentTranslation("msg.trophyslots.unlockAll"));
            EntityPlayer mp = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID());
            if (mp != null)
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(36), (EntityPlayerMP) player);
            return true;
        }
        return false;
    }

    public int getSlotsUnlocked() {
        return 0;
    }

    public void setSlotsUnlocked(int var) {
    }

    public boolean slotUnlocked(int slot) {
        return false;
    }

    public boolean hasUnlockedAllSlots() {
        return false;
    }

    public void reset() {
    }
}
