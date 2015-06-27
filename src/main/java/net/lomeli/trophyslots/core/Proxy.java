package net.lomeli.trophyslots.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.EventHandlerClient;
import net.lomeli.trophyslots.core.handler.EventHandlerServer;
import net.lomeli.trophyslots.core.handler.VillagerHandler;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;

public class Proxy {
    public EventHandlerClient eventHandlerClient;
    public EventHandlerServer eventHandlerServer;
    protected boolean reverseOrder;
    protected int startingSlots;
    protected List<String> achievementWhiteList;

    public void preInit() {
        ModItems.registerItems();
        VillagerRegistry.instance().registerVillageTradeHandler(1, new VillagerHandler());
    }

    public void init() {
        eventHandlerServer = new EventHandlerServer();
        registerFMLEvent(eventHandlerServer);
        registerForgeEvent(eventHandlerServer);
    }

    public void postInit() {
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
            player.addChatComponentMessage(new ChatComponentTranslation(i >= SlotUtil.getMaxSlots() ? "msg.trophyslots.unlockAll" : "msg.trophyslots.unlock"));
            EntityPlayerMP mp = (EntityPlayerMP) (player instanceof EntityPlayerMP ? player : FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID()));
            if (mp != null) {
                if (!mp.func_147099_x().hasAchievementUnlocked(TrophySlots.firstSlot) && mp.func_147099_x().canUnlockAchievement(TrophySlots.firstSlot))
                    mp.addStat(TrophySlots.firstSlot, 1);
                if (i >= SlotUtil.getMaxSlots() && !mp.func_147099_x().hasAchievementUnlocked(TrophySlots.maxCapcity) && mp.func_147099_x().canUnlockAchievement(TrophySlots.maxCapcity))
                    mp.addStat(TrophySlots.maxCapcity, 1);
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(i), (EntityPlayerMP) player);
            }
            return true;
        }
        return false;
    }

    public boolean unlockAllSlots(EntityPlayer player) {
        if (player != null && !SlotUtil.hasUnlockedAllSlots(player)) {
            SlotUtil.setSlotsUnlocked(player, SlotUtil.getMaxSlots());
            player.addChatComponentMessage(new ChatComponentTranslation("msg.trophyslots.unlockAll"));
            EntityPlayerMP mp = (EntityPlayerMP) (player instanceof EntityPlayerMP ? player : FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID()));
            if (mp != null) {
                if (!mp.func_147099_x().hasAchievementUnlocked(TrophySlots.maxCapcity) && mp.func_147099_x().canUnlockAchievement(TrophySlots.maxCapcity))
                    mp.addStat(TrophySlots.maxCapcity, 1);
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(SlotUtil.getMaxSlots()), mp);
            }
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

    public boolean unlockReverse() {
        return reverseOrder;
    }

    public void setReverse(boolean bool) {
        reverseOrder = bool;
    }

    public int getStartingSlots() {
        return startingSlots;
    }

    public void setStartingSlots(int val) {
        startingSlots = val;
    }

    public void resetConfig() {
    }

    public void openWhitelistGui() {
    }

    public List<String> getWhiteList() {
        if (achievementWhiteList == null)
            achievementWhiteList = new ArrayList<String>();
        return achievementWhiteList;
    }

    public void setWhiteList(List<String> list) {
        this.achievementWhiteList = list;
    }
}
