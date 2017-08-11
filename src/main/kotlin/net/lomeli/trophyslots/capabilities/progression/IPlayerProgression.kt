package net.lomeli.trophyslots.capabilities.progression

interface IPlayerProgression {
    fun givePlayerProgression(progression: Progression): Boolean

    fun givePlayerProgression(id: String): Boolean

    fun forceAddProgression(id: String)

    fun hasProgression(progression: Progression): Boolean

    fun hasProgression(id: String): Boolean

    fun getTotalProgress(): Int

    fun getUnlockList(): List<String>
}