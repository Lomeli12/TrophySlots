package net.lomeli.trophyslots.core.triggers

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.ICriterionTrigger
import net.minecraftforge.fml.relauncher.ReflectionHelper

object AllTriggers {
    @JvmStatic val UNLOCK_SLOT = UnlockSlotTrigger()

    fun registerTriggers() {
        register(UNLOCK_SLOT)
    }

    private fun <T : ICriterionTrigger<*>> register(criterion: T) {
        var method = ReflectionHelper.findMethod(CriteriaTriggers::class.java, "register", "func_192118_a",
                ICriterionTrigger::class.java)
        if (method != null) method.invoke(null, criterion)
    }
}