package net.lomeli.trophyslots.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    public static final int LEFT_SHIFT = 340;

    public static boolean safeKeyDown(int code) {
        try {
            return InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), code);
        } catch (Exception e) {
            return false;
        }
    }
}
