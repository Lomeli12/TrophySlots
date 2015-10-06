package net.lomeli.trophyslots.core.version

import com.google.common.collect.Lists
import java.util.*

public class UpdateJson {
    private val major: Int
    private val minor: Int
    private val revision: Int
    private val downloadURL: String
    private val direct: Boolean
    private val changeLog: ArrayList<String>

    constructor(major: Int, minor: Int, revision: Int, downloadURL: String, direct: Boolean, vararg changes: String) {
        this.major = major
        this.minor = minor
        this.revision = revision
        this.downloadURL = downloadURL
        this.direct = direct
        this.changeLog = Lists.newArrayList()
        if (changes != null && changes.size() > 0) {
            for (st: String in changes)
                changeLog.add(st)
        }
    }

    fun getMajor(): Int = major

    fun getMinor(): Int = minor

    fun getRevision(): Int = revision

    fun getDownloadURL(): String = downloadURL

    fun isDirect(): Boolean = direct

    fun getChangeLog(): List<String> = changeLog

    fun getVersion(): String = "$major.$minor.$revision"
}