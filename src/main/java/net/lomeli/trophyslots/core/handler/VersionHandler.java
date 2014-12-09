package net.lomeli.trophyslots.core.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SimpleUtil;

import static cpw.mods.fml.relauncher.Side.CLIENT;

public class VersionHandler {
    private int mod_major, mod_minor, mod_rev;
    private boolean needsUpdate, isDirect, promptMsg;
    private String version, downloadURL, jsonURL, modname, currentVer;
    private List<String> changeList;

    public VersionHandler(String jsonURL, String modname, int major, int minor, int rev) {
        this.jsonURL = jsonURL;
        this.modname = modname;
        this.mod_major = major;
        this.mod_minor = minor;
        this.mod_rev = rev;
        this.currentVer = this.mod_major + "." + this.mod_minor + "." + this.mod_rev;
        this.needsUpdate = false;
        this.isDirect = false;
        this.promptMsg = false;
        this.changeList = new ArrayList<String>();

        FMLCommonHandler.instance().bus().register(this);
    }

    public void checkForUpdates() {
        try {
            URL url = new URL(this.jsonURL);
            int major = 0, minor = 0, revision = 0;
            JsonParser parser = new JsonParser();
            JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
            if (reader != null) {
                JsonElement element = parser.parse(reader);
                JsonObject jsonObject = element.getAsJsonObject();
                major = jsonObject.get("major").getAsInt();
                minor = jsonObject.get("minor").getAsInt();
                revision = jsonObject.get("revision").getAsInt();
                this.downloadURL = jsonObject.get("downloadURL").getAsString();
                this.isDirect = jsonObject.get("direct").getAsBoolean();
                JsonArray changeLog = jsonObject.get("changeLog").getAsJsonArray();
                Iterator<JsonElement> it = changeLog.iterator();
                while (it.hasNext()) {
                    this.changeList.add(it.next().getAsString());
                }
                reader.endObject();
                reader.close();
            }

            this.needsUpdate = (this.mod_major > major ? (this.mod_minor > minor ? (this.mod_rev > revision ? false : true) : true) : true);
            if (this.needsUpdate) {
                this.version = major + "." + minor + "." + revision;
                this.promptMsg = true;
                sendMessage();
            }
        } catch (Exception e) {
            TrophySlots.log(1, "Could not check for updates!");
        }
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
        TrophySlots.log(0, String.format(SimpleUtil.translate("update.trophyslots"), this.version, this.downloadURL));
    }

    @SubscribeEvent
    @SideOnly(CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && FMLClientHandler.instance().getClient().thePlayer != null) {
            EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
            if (this.promptMsg) {
                player.addChatComponentMessage(new ChatComponentText(String.format(SimpleUtil.translate("update.trophyslots"), this.version, this.downloadURL)));
                this.promptMsg = false;
            }
        }
    }
}
