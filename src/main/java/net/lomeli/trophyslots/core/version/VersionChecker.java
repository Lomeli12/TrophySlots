package net.lomeli.trophyslots.core.version;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.core.Logger;

/**
 * An example of the JSON file you'll need online can be seen <a href="http://paste.ubuntu.com/10992152/">here</a> (now with comments!)
 *
 * @author Lomeli12
 */
public class VersionChecker {
    private int mod_major, mod_minor, mod_rev;
    private boolean needsUpdate, isDirect, doneTelling;
    private String version, downloadURL, jsonURL, modname, currentVer;
    private String[] changeList;

    public VersionChecker(String jsonURL, String modname, int major, int minor, int rev) {
        this.jsonURL = jsonURL;
        this.modname = modname;
        this.mod_major = major;
        this.mod_minor = minor;
        this.mod_rev = rev;
        this.currentVer = this.mod_major + "." + this.mod_minor + "." + this.mod_rev;
        this.needsUpdate = false;
        this.isDirect = false;
        this.doneTelling = true;

        FMLCommonHandler.instance().bus().register(this);
    }

    public void checkForUpdates() {
        try {
            Logger.INSTANCE$.logInfo("Checking for updates...");
            URL url = new URL(this.jsonURL);
            Gson gson = new Gson();
            UpdateJson update = gson.fromJson(new InputStreamReader(url.openStream()), UpdateJson.class);
            if (update != null) {
                this.needsUpdate = true;
                if (this.mod_major >= update.getMajor()) {
                    if (this.mod_minor >= update.getMinor()) {
                        if (this.mod_rev >= update.getRevision())
                            this.needsUpdate = false;
                        else {
                            if (this.mod_minor >= update.getMinor())
                                this.needsUpdate = this.mod_major < update.getMajor();
                        }
                    } else
                        this.needsUpdate = this.mod_major < update.getMajor();
                }
                if (this.needsUpdate) {
                    this.downloadURL = update.getDownloadURL();
                    this.isDirect = update.isDirect();
                    this.changeList = update.getChangeLog();
                    this.version = update.getVersion();
                    this.doneTelling = false;
                    sendMessage();
                } else
                    Logger.INSTANCE$.logInfo("Using latest version of " + this.modname);
            }
        } catch (Exception e) {
            Logger.INSTANCE$.logError("Could not check for updates for " + this.modname + "!");
        }
    }

    private String translate(String unlocalized) {
        return StatCollector.translateToLocal(unlocalized);
    }

    private void sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            String changeLog = "";
            for (String i : this.changeList)
                changeLog += "- " + i;

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("modDisplayName", this.modname);
            tag.setString("oldVersion", this.currentVer);
            tag.setString("newVersion", this.version);
            tag.setString("updateUrl", this.downloadURL);
            tag.setBoolean("isDirectLink", this.isDirect);
            tag.setString("changeLog", changeLog);
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
        }
        Logger.INSTANCE$.logInfo(String.format(translate("update.trophyslots"), this.version, this.downloadURL));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && FMLClientHandler.instance().getClient().thePlayer != null) {
            EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
            if (!this.doneTelling) {
                player.addChatComponentMessage(new ChatComponentText(String.format(translate("update.trophyslots"), this.version, this.downloadURL)));
                this.doneTelling = true;
            }
        }
    }
}
