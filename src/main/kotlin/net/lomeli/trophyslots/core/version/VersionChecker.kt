package net.lomeli.trophyslots.core.version

import com.google.gson.Gson
import net.lomeli.trophyslots.core.Logger
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StatCollector
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
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

        FMLCommonHandler.instance().bus().register(this)
    }

    fun checkForUpdates() {
        try {
            Logger.logInfo("Checking for updates...")
            val url = URL(this.jsonURL)
            val gson = Gson()
            val update = gson.fromJson(InputStreamReader(url.openStream()), UpdateJson::class.java)
            if (update != null) {
                this.needsUpdate = true
                if (this.mod_major >= update.major) {
                    if (this.mod_minor >= update.minor) {
                        if (this.mod_rev >= update.revision)
                            this.needsUpdate = false
                        else {
                            if (this.mod_minor >= update.minor)
                                this.needsUpdate = this.mod_major < update.major
                        }
                    } else
                        this.needsUpdate = this.mod_major < update.major
                }
                if (this.needsUpdate) {
                    this.downloadURL = update.downloadURL
                    this.isDirect = update.isDirect()
                    this.changeList = update.getChangeLog()
                    this.version = update.getVersion()
                    this.doneTelling = false
                    sendMessage()
                } else
                    Logger.logInfo("Using latest version of $modname")
            }
        } catch (e: Exception) {
            Logger.logError("Could not check for updates for $modname!")
        }

    }

    fun translate(unlocalized: String): String = StatCollector.translateToLocal(unlocalized)

    fun sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            var changeLog = ""
            var i = 0;
            while (i < this.changeList!!.size()) {
                changeLog += "- " + this.changeList!![i]
                ++i
            }

            val tag = NBTTagCompound()
            tag.setString("modDisplayName", this.modname)
            tag.setString("oldVersion", this.currentVer)
            tag.setString("newVersion", this.version)
            tag.setString("updateUrl", this.downloadURL)
            tag.setBoolean("isDirectLink", this.isDirect)
            tag.setString("changeLog", changeLog)
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag)
        }
        Logger.logInfo(translate("update.trophyslots").format(this.version, this.downloadURL))
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