package net.lomeli.trophyslots.core.criterion;

import net.lomeli.knit.utils.CriteriaRegistry;

public class ModCriteria {
    public static final UnlockSlotTrigger UNLOCK_SLOT = new UnlockSlotTrigger();

    public static void initTriggers() {
        CriteriaRegistry.registerCriterion(UNLOCK_SLOT);
    }

}
