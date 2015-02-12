package net.lomeli.trophyslots.client;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.Proxy;

public class ClientProxy extends Proxy {
    private int slotsUnlocked;

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        registerForgeEvent(new EventHandlerClient());
        registerFMLEvent(TrophySlots.versionHandler);
        registerFMLEvent(TrophySlots.modConfig);
    }

    @Override
    public boolean slotUnlocked(int slotNum) {
        return slotNum < 36 ? slotNum < TrophySlots.startingSlots + slotsUnlocked : true;
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
        return slotsUnlocked >= 36;
    }

    @Override
    public void reset() {
        slotsUnlocked = 0;
    }
}
