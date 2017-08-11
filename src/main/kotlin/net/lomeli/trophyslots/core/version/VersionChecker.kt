package net.lomeli.trophyslots.core.version

import com.google.gson.Gson
import net.lomeli.trophyslots.TrophySlots
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.InputStreamReader
import java.net.URL

class VersionChecker(val jsonURL: String, val modname: String, val modversion: String) {
    var needsUpdate: Boolean = false
    var isDirect: Boolean = false
    var doneTelling: Boolean = false
    var version: String? = null
    var downloadURL: String? = null
    var changeList: List<String>? = null
    var mod_major: Int = 0
    var mod_minor: Int = 0
    var mod_rev: Int = 0

    init {
        getVersionFromID(modversion)
        this.needsUpdate = false
        this.isDirect = false
        this.doneTelling = true

        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun getVersionFromID(str: String) {
        val arr = str.split('.').dropLastWhile { it.isEmpty() }.toTypedArray()
        if (!str.equals("@VERSION@")) {
            for (i in 0..2) {
                if (i < arr.size) {
                    val value = parseInt(arr[i])
                    when (i) {
                        0 -> this.mod_major = value
                        1 -> this.mod_minor = value
                        2 -> this.mod_rev = value
                    }
                }
            }
        } else {
            this.mod_major = Int.MAX_VALUE
            this.mod_minor = Int.MAX_VALUE
            this.mod_rev = Int.MAX_VALUE
        }
    }

    private fun parseInt(str: String): Int {
        try {
            return Integer.parseInt(str)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun checkForUpdates() {
        try {
            TrophySlots.log?.logInfo(translate("update.trophyslots.checking"))
            val url = URL(this.jsonURL)
            val gson = Gson()
            val update = gson.fromJson(InputStreamReader(url.openStream()), UpdateJson::class.java)
            if (update != null) {
                this.needsUpdate = compareVersion(update)
                if (this.needsUpdate) {
                    this.downloadURL = update.downloadURL
                    this.isDirect = update.isDirect()
                    this.changeList = update.getChangeLog()
                    this.version = update.getVersion()
                    this.doneTelling = false
                    sendMessage()
                } else
                    TrophySlots.log?.logInfo(translate("update.trophyslots.none"))
            }
        } catch (e: Exception) {
            TrophySlots.log?.logError(translate("update.trophyslots.failed"))
        }

    }

    private fun compareVersion(update: UpdateJson): Boolean {
        if (mod_major > update.major) return false
        if (mod_major < update.major) return true
        if (mod_minor > update.minor) return false
        if (mod_minor < update.minor) return true
        if (mod_rev > update.revision) return false
        if (mod_rev < update.revision) return true
        return false
    }

    fun translate(unlocalized: String): String = I18n.translateToLocal(unlocalized)

    fun sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            var changeLog = ""
            for (i in this.changeList!!.indices)
                changeLog += "- " + this.changeList!![i]

            val tag = NBTTagCompound()
            tag.setString("modDisplayName", this.modname)
            tag.setString("oldVersion", this.modversion)
            tag.setString("newVersion", this.version)
            tag.setString("updateUrl", this.downloadURL)
            tag.setBoolean("isDirectLink", this.isDirect)
            tag.setString("changeLog", changeLog)
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag)
        }
        TrophySlots.log?.logInfo(translate("update.trophyslots").format(this.version, this.downloadURL))
        TrophySlots.log?.logInfo(translate("update.trophyslots.old").format(this.modversion))
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END && FMLClientHandler.instance().client.thePlayer != null) {
            val player = FMLClientHandler.instance().client.thePlayer
            if (!this.doneTelling) {
                player.addChatComponentMessage(TextComponentString(translate("update.trophyslots").format(this.version, this.downloadURL)))
                this.doneTelling = true
            }
        }
    }
}