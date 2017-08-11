package net.lomeli.trophyslots.capabilities.slots

import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagInt
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

class SlotCapability : Capability.IStorage<ISlotInfo> {
    override fun readNBT(capability: Capability<ISlotInfo>?, instance: ISlotInfo?, side: EnumFacing?, nbt: NBTBase?) {
        if (instance == null) return
        if (nbt is NBTTagInt) instance.setSlots(nbt.int)
    }

    override fun writeNBT(capability: Capability<ISlotInfo>?, instance: ISlotInfo?, side: EnumFacing?): NBTBase? {
        if (instance == null) return null
        return NBTTagInt(instance.getSlotsUnlocked())
    }
}