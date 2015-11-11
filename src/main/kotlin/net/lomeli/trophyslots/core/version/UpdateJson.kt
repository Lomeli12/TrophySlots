package net.lomeli.trophyslots.core.version

import com.google.common.collect.Lists
import java.util.*

public class UpdateJson(val major: Int, val minor: Int, val revision: Int, val downloadURL: String, val direct: Boolean, vararg changes: String) {
    private val changeLog: ArrayList<String>

    init {
        this.changeLog = Lists.newArrayList()
        if (changes.size() > 0) {
            for (st: String in changes)
                changeLog.add(st)
        }
    }

    fun isDirect(): Boolean = direct

    fun getChangeLog(): List<String> = changeLog

    fun getVersion(): String = "$major.$minor.$revision"
}