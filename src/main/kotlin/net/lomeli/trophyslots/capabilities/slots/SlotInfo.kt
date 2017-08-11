package net.lomeli.trophyslots.capabilities.slots

import net.lomeli.trophyslots.TrophySlots

class SlotInfo : ISlotInfo {
    private var slotsUnlocked = 0

    override fun getSlotsUnlocked(): Int = slotsUnlocked

    override fun unlockSlot(num: Int): Boolean {
        if (!isAtMaxSlots()) {
            slotsUnlocked += num
            if (isAtMaxSlots()) slotsUnlocked = getMaxSlots()
            return true
        }
        return false
    }

    override fun slotUnlocked(slotNum: Int): Boolean {
        if (TrophySlots.proxy!!.unlockReverse() && slotNum >= 9) {
            if (slotNum < 36)
                return slotNum > 44 - (TrophySlots.proxy!!.startingSlots + slotsUnlocked)
            return true
        }
        if (slotNum < 36)
            return slotNum < TrophySlots.proxy!!.startingSlots + slotsUnlocked
        return true
    }

    override fun setSlots(slots: Int) {
        slotsUnlocked = slots
    }

    override fun isAtMaxSlots(): Boolean = slotsUnlocked >= getMaxSlots()

    override fun getMaxSlots(): Int = 36 - TrophySlots.proxy!!.startingSlots
}