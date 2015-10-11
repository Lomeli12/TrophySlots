package net.lomeli.trophyslots.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.Loader;

public class CompatManager {
    private static List<ICompatModule> moduleList;

    public static void initCompatModules() {
        addModlue(new PlayerInvMod());
        //if (Loader.isModLoaded("appliedenergistics2"))
        //    addModlue(new AE2Mod());
        //if (Loader.isModLoaded("StevesWorkshop"))
        //    addModlue(new StWorkshopMod());
    }

    public static void addModlue(ICompatModule module) {
        if (moduleList == null)
            moduleList = new ArrayList<ICompatModule>();
        if (module == null)
            return;
        moduleList.add(module);
    }

    public static boolean useCompatReplace(GuiContainer gui, EntityPlayer player) {
        boolean flag = false;
        for (ICompatModule module : moduleList) {
            if (module.isCompatibleGui(gui)) {
                flag = true;
                module.replaceSlots(gui, player);
                break;
            }
        }
        return flag;
    }
}
