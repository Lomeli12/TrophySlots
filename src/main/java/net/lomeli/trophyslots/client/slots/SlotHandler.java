package net.lomeli.trophyslots.client.slots;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;

import net.lomeli.trophyslots.core.ObfUtil;

public class SlotHandler {

    public static Slot getSlotBasedOnGui(GuiContainer gui, Slot oldSlot) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Loader.isModLoaded("StevesWorkshop")) {
            if (gui.getClass().getCanonicalName() == "vswe.production.gui.GuiTable" && oldSlot.getClass().getCanonicalName() == "vswe.production.gui.container.slot.SlotPlayer")
                return (Slot) invokeMethod("net.lomeli.trophyslots.client.slots", null, new String[]{"getSlot"}, mc, gui, oldSlot); // I hate having to deal with NoClassDefFoundErrors.... T_T
        }
        return new SlotLocked(mc.thePlayer.inventory, oldSlot.getSlotIndex(), oldSlot.xDisplayPosition, oldSlot.yDisplayPosition);
    }

    public static <T, E> Object invokeMethod(String name, E instance, String[] names, Object... args) {
        try {
            Class<?> clazz = Class.forName(name);
            Method method = ObfUtil.getMethod(clazz, names);
            if (method != null)
                return method.invoke(instance, args);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return null;
    }
}
