package net.lomeli.trophyslots.compat;

import cpw.mods.fml.common.Loader;

public class CompatManager {
    public static AE2Mod ae2Mod;
    public static StWorkshopMod stWorkshopMod;

    public static void initCompatModules() {
        if (Loader.isModLoaded("appliedenergistics2"))
            ae2Mod = new AE2Mod();
        if (Loader.isModLoaded("StevesWorkshop"))
            stWorkshopMod = new StWorkshopMod();
    }
}
