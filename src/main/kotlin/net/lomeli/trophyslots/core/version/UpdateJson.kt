package net.lomeli.trophyslots.core.version

import com.google.common.collect.Lists
import java.util.*

class UpdateJson(val major: Int, val minor: Int, val revision: Int, val downloadURL: String, val direct: Boolean, vararg changes: String) {
    val changeLog: ArrayList<String> = Lists.newArrayList()

    init {
        if (changes.size > 0) {
            for (i in changes.indices)
                changeLog.add(changes[i])
        }
    }

    fun isDirect(): Boolean = direct

    fun getChangeLog(): List<String> = changeLog

    fun getVersion(): String = "$major.$minor.$revision"
}