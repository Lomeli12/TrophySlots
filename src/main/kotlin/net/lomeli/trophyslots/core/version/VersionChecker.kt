package net.lomeli.trophyslots.core.version

import com.google.gson.Gson
import net.lomeli.trophyslots.TrophySlots
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StatCollector
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

public class VersionChecker(val jsonURL: String, val modname: String, val mod_major: Int, val mod_minor: Int, val mod_rev: Int) {
    var needsUpdate: Boolean = false
    var isDirect: Boolean = false
    var doneTelling: Boolean = false
    var version: String? = null
    var downloadURL: String? = null
    val currentVer: String
    var changeList: List<String>? = null

    init {
        this.currentVer = "$mod_major.$mod_minor.$mod_rev"
        this.needsUpdate = false
        this.isDirect = false
        this.doneTelling = true

        MinecraftForge.EVENT_BUS.register(this)
    }

    fun checkForUpdates() {
        try {
            TrophySlots.log.logInfo(translate("update.trophyslots.checking"))
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
                    TrophySlots.log.logInfo(translate("update.trophyslots.none"))
            }
        } catch (e: Exception) {
            TrophySlots.log.logError(translate("update.trophyslots.failed"))
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

    fun translate(unlocalized: String): String = StatCollector.translateToLocal(unlocalized)

    fun sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            var changeLog = ""
            for (i in this.changeList!!.indices)
                changeLog += "- " + this.changeList!![i]

            val tag = NBTTagCompound()
            tag.setString("modDisplayName", this.modname)
            tag.setString("oldVersion", this.currentVer)
            tag.setString("newVersion", this.version)
            tag.setString("updateUrl", this.downloadURL)
            tag.setBoolean("isDirectLink", this.isDirect)
            tag.setString("changeLog", changeLog)
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag)
        }
        TrophySlots.log.logInfo(translate("update.trophyslots").format(this.version, this.downloadURL))
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END && FMLClientHandler.instance().client.thePlayer != null) {
            val player = FMLClientHandler.instance().client.thePlayer
            if (!this.doneTelling) {
                player.addChatComponentMessage(ChatComponentText(translate("update.trophyslots").format(this.version, this.downloadURL)))
                this.doneTelling = true
            }
        }
    }
}