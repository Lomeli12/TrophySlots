package net.lomeli.trophyslots.capabilities.progression

import com.google.common.base.Strings
import net.lomeli.trophyslots.TrophySlots
import java.util.Collections
import kotlin.collections.ArrayList
import kotlin.collections.List

class PlayerProgression : IPlayerProgression {
    private val progress = ArrayList<String>()

    override fun givePlayerProgression(progression: Progression): Boolean {
        if (!TrophySlots.useProgressionUnlocks) return false
        if (Strings.isNullOrEmpty(progression.progressionID) || hasProgression(progression.progressionID) || !ProgressionManager.progressionRegistered(progression.progressionID))
            return false
        if (!Strings.isNullOrEmpty(progression.getParentID()) && ProgressionManager.progressionRegistered(progression.getParentID()!!)) {
            if (hasProgression(progression.getParentID()!!)) {
                progress.add(progression.progressionID)
                return true
            } else return false
        } else {
            progress.add(progression.progressionID)
            return true
        }
    }


    override fun givePlayerProgression(id: String): Boolean {
        var progression = ProgressionManager.getProgression(id)
        return if (progression != null) givePlayerProgression(progression) else false
    }

    override fun forceAddProgression(id: String) {
        if (!Strings.isNullOrEmpty(id) && !hasProgression(id)) progress.add(id)
    }

    override fun hasProgression(progression: Progression): Boolean = hasProgression(progression.progressionID)

    override fun hasProgression(id: String): Boolean = progress.contains(id)

    override fun getTotalProgress(): Int = progress.size

    override fun getUnlockList(): List<String> = Collections.unmodifiableList(progress)
}