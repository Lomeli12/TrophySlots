package net.lomeli.trophyslots.capabilities.progression

import com.google.common.base.Strings
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

class ProgressionCapability() : Capability.IStorage<IPlayerProgression> {
    override fun readNBT(capability: Capability<IPlayerProgression>?, instance: IPlayerProgression?, side: EnumFacing?, nbt: NBTBase?) {
        if (nbt == null && nbt !is NBTTagList && instance == null) return
        val list = nbt as NBTTagList
        if (list.hasNoTags() || list.tagType != 8) return
        (0..list.tagCount()).forEach { i ->
            val id = list.getStringTagAt(i)
            if (!Strings.isNullOrEmpty(id)) instance!!.forceAddProgression(list.getStringTagAt(i))
        }
    }

    override fun writeNBT(capability: Capability<IPlayerProgression>?, instance: IPlayerProgression?, side: EnumFacing?): NBTBase? {
        val nbt = NBTTagList()
        if (instance != null) {
            val unlockList = instance.getUnlockList()
            if (unlockList.isNotEmpty()) {
                unlockList
                        .map { NBTTagString(it) }
                        .forEach { nbt.appendTag(it) }
            }
        }
        return nbt
    }
}