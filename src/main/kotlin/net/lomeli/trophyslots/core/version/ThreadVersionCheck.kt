package net.lomeli.trophyslots.core.version

import com.google.gson.Gson
import net.lomeli.trophyslots.TrophySlots
import java.io.InputStreamReader
import java.net.URL

class ThreadVersionCheck(var versionChecker: VersionChecker) : Runnable {
    override fun run() {
        try {
            TrophySlots.log?.logInfo(versionChecker.translate("update.trophyslots.checking"))
            val url = URL(versionChecker.jsonURL)
            val gson = Gson()
            val update = gson.fromJson(InputStreamReader(url.openStream()), UpdateJson::class.java)
            if (update != null) {
                versionChecker.needsUpdate = versionChecker.compareVersion(update)
                if (versionChecker.needsUpdate) {
                    versionChecker.downloadURL = update.downloadURL
                    versionChecker.isDirect = update.isDirect()
                    versionChecker.changeList = update.getChangeLog()
                    versionChecker.version = update.getVersion()
                    versionChecker.doneTelling = false
                    versionChecker.sendMessage()
                } else
                    TrophySlots.log?.logInfo(versionChecker.translate("update.trophyslots.none"))
            }
        } catch (e: Exception) {
            TrophySlots.log?.logError(versionChecker.translate("update.trophyslots.failed"))
        }
    }
}