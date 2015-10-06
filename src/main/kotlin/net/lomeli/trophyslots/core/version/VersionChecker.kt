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

public class VersionChecker {
    private val mod_major: Int
    private val mod_minor:Int
    private val mod_rev:Int
    private var needsUpdate: Boolean = false
    private var isDirect:Boolean = false
    private var doneTelling:Boolean = false
    private var version: String? = null
    private var downloadURL:String? = null
    private val jsonURL:String
    private val modname:String
    private val currentVer:String
    private var changeList: List<String>? = null

    constructor(jsonURL: String, modname: String, major: Int, minor: Int, rev: Int) {
        this.jsonURL = jsonURL
        this.modname = modname
        this.mod_major = major
        this.mod_minor = minor
        this.mod_rev = rev
        this.currentVer = "$major.$minor.$rev"
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
                if (this.mod_major >= update.getMajor()) {
                    if (this.mod_minor >= update.getMinor()) {
                        if (this.mod_rev >= update.getRevision())
                            this.needsUpdate = false
                        else {
                            if (this.mod_minor >= update.getMinor())
                                this.needsUpdate = this.mod_major < update.getMajor()
                        }
                    } else
                        this.needsUpdate = this.mod_major < update.getMajor()
                }
                if (this.needsUpdate) {
                    this.downloadURL = update.getDownloadURL()
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

    private fun translate(unlocalized: String): String = StatCollector.translateToLocal(unlocalized)

    private fun sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            var changeLog = ""
            for (i in this.changeList!!)
                changeLog += "- " + i

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