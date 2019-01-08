package net.lomeli.trophyslots.core.criterion;

import net.lomeli.trophyslots.core.utils.ObfUtil;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.Criterions;

public class ModCriterions {
    public static final UnlockSlotTrigger UNLOCK_SLOT = new UnlockSlotTrigger();

    public static void initTriggers() {
        registerCriterion(UNLOCK_SLOT);
    }

    private static void registerCriterion(Criterion criterion) {
        ObfUtil.invokeMethod(Criterions.class, null, new String[]{"register", "a"},
                new Class[]{Criterion.class}, criterion);
    }
}
