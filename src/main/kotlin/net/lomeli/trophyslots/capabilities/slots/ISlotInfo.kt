package net.lomeli.trophyslots.capabilities.slots

interface ISlotInfo {
    fun getSlotsUnlocked(): Int
    fun unlockSlot(num: Int): Boolean
    fun slotUnlocked(slotNum: Int): Boolean
    fun setSlots(slots: Int)
    fun isAtMaxSlots(): Boolean
    fun getMaxSlots(): Int
}