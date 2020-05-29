package net.lomeli.trophyslots.core.criterion;

import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteria {
    public static final UnlockSlotTrigger UNLOCK_SLOT = new UnlockSlotTrigger();

    public static void initTriggers() {
        CriteriaTriggers.register(UNLOCK_SLOT);
    }
}
