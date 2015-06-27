package net.lomeli.trophyslots.client;

import cpw.mods.fml.client.FMLClientHandler;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.config.GuiWhitelist;
import net.lomeli.trophyslots.compat.CompatManager;
import net.lomeli.trophyslots.core.Proxy;
import net.lomeli.trophyslots.core.SlotUtil;

public class ClientProxy extends Proxy {
    private int slotsUnlocked;

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        registerForgeEvent(eventHandlerClient = new EventHandlerClient());
        registerFMLEvent(TrophySlots.versionHandler);
        registerFMLEvent(TrophySlots.modConfig);
    }

    @Override
    public void postInit() {
        super.postInit();
        CompatManager.initCompatModules();
    }

    @Override
    public boolean slotUnlocked(int slotNum) {
        if (unlockReverse() && slotNum >= 9)
            return slotNum < 36 ? slotNum > 44 - (TrophySlots.proxy.getStartingSlots() + slotsUnlocked) : true;
        return slotNum < 36 ? slotNum < TrophySlots.proxy.getStartingSlots() + slotsUnlocked : true;
    }

    @Override
    public int getSlotsUnlocked() {
        return slotsUnlocked;
    }

    @Override
    public void setSlotsUnlocked(int var) {
        slotsUnlocked = var;
    }

    @Override
    public boolean hasUnlockedAllSlots() {
        return slotsUnlocked >= SlotUtil.getMaxSlots();
    }

    @Override
    public void reset() {
        slotsUnlocked = 0;
    }

    @Override
    public boolean unlockReverse() {
        return reverseOrder;
    }

    @Override
    public void setReverse(boolean bool) {
        reverseOrder = bool;
    }

    @Override
    public void resetConfig() {
        TrophySlots.modConfig.loadConfig();
    }

    @Override
    public void openWhitelistGui() {
        FMLClientHandler.instance().getClient().displayGuiScreen(new GuiWhitelist(FMLClientHandler.instance().getClient().currentScreen));
    }
}
