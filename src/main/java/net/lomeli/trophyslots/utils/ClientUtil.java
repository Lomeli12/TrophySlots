package net.lomeli.trophyslots.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
    public static final int LEFT_SHIFT = 340;

    public static boolean safeKeyDown(int code) {
        try {
            return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), code);
        } catch (Exception ex) {
            // no op
        }
        return false;
    }
}
