package net.lomeli.trophyslots.compat

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import java.util.*

public object CompatManager {
    private var moduleList: MutableList<ICompatModule>? = null

    fun initCompatModules() {
        addModlue(PlayerInvMod)

        //if (Loader.isModLoaded("appliedenergistics2"))
        //    addModlue(new AE2Mod());
        //if (Loader.isModLoaded("StevesWorkshop"))
        //    addModlue(new StWorkshopMod());
    }

    fun addModlue(module: ICompatModule?) {
        if (moduleList == null)
            moduleList = ArrayList<ICompatModule>()
        if (module == null)
            return
        moduleList!!.add(module)
    }

    fun useCompatReplace(gui: GuiContainer, player: EntityPlayer): Boolean {
        var flag = false
        for (module in moduleList!!) {
            if (module.isCompatibleGui(gui)) {
                flag = true
                module.replaceSlots(gui, player)
                break
            }
        }
        return flag
    }
}
