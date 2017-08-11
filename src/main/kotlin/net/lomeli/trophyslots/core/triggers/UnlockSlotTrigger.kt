package net.lomeli.trophyslots.core.triggers

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.minecraft.advancements.ICriterionTrigger
import net.minecraft.advancements.PlayerAdvancements
import net.minecraft.advancements.critereon.AbstractCriterionInstance
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation

class UnlockSlotTrigger : ICriterionTrigger<UnlockSlotTrigger.Instance> {
    private val listeners = Maps.newHashMap<PlayerAdvancements, UnlockSlotTrigger.Listeners>()

    override fun getId(): ResourceLocation = ResourceLocation(TrophySlots.MOD_ID, "unlock_slot")

    override fun removeAllListeners(playerAdvancementsIn: PlayerAdvancements?) {
        this.listeners.remove(playerAdvancementsIn)
    }

    override fun deserializeInstance(json: JsonObject?, context: JsonDeserializationContext?): Instance {
        return Instance(json?.get("slots_unlocked")?.asInt!!)
    }

    override fun removeListener(playerAdvancementsIn: PlayerAdvancements?, listener: ICriterionTrigger.Listener<Instance>?) {
        var trigger: UnlockSlotTrigger.Listeners? = this.listeners[playerAdvancementsIn] ?: return
        trigger!!.remove(listener!!)
        if (trigger.isEmpty) this.listeners.remove(playerAdvancementsIn)
    }

    override fun addListener(playerAdvancementsIn: PlayerAdvancements?, listener: ICriterionTrigger.Listener<Instance>?) {
        var trigger: UnlockSlotTrigger.Listeners? = this.listeners[playerAdvancementsIn]

        if (trigger == null) {
            trigger = UnlockSlotTrigger.Listeners(playerAdvancementsIn!!)
            this.listeners.put(playerAdvancementsIn, trigger)
        }

        trigger.add(listener!!)
    }

    fun trigger(player: EntityPlayerMP) {
        var listenter = this.listeners[player.advancements]
        if (listenter != null) listenter.trigger(player)
    }

    class Instance(private val minSlot: Int) : AbstractCriterionInstance(ResourceLocation(TrophySlots.MOD_ID, "unlock_slot")) {
        fun test(player: EntityPlayerMP): Boolean {
            val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
            return if (minSlot == -1) slotInfo.isAtMaxSlots() else slotInfo.getSlotsUnlocked() >= minSlot
        }
    }

    internal class Listeners(private val playerAdvancements: PlayerAdvancements) {
        private val listeners = Sets.newHashSet<ICriterionTrigger.Listener<UnlockSlotTrigger.Instance>>()

        val isEmpty: Boolean
            get() = this.listeners.isEmpty()

        fun add(listener: ICriterionTrigger.Listener<UnlockSlotTrigger.Instance>) {
            this.listeners.add(listener)
        }

        fun remove(listener: ICriterionTrigger.Listener<UnlockSlotTrigger.Instance>) {
            this.listeners.remove(listener)
        }

        fun trigger(player: EntityPlayerMP) {
            var list: MutableList<ICriterionTrigger.Listener<UnlockSlotTrigger.Instance>>? = null

            for (listener in this.listeners) {
                if ((listener.criterionInstance as UnlockSlotTrigger.Instance).test(player)) {
                    if (list == null)
                        list = Lists.newArrayList<ICriterionTrigger.Listener<UnlockSlotTrigger.Instance>>()

                    list!!.add(listener)
                }
            }

            if (list != null) {
                for (listener1 in list)
                    listener1.grantCriterion(this.playerAdvancements)
            }
        }
    }
}