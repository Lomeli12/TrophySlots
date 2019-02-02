package net.lomeli.trophyslots.mixin.client;

import net.lomeli.trophyslots.client.accessors.IScreenAccessor;
import net.lomeli.trophyslots.client.screen.LockedSlotScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "openScreen", at = @At(shift = At.Shift.AFTER, value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Screen;initialize(Lnet/minecraft/client/MinecraftClient;II)V",
            opcode = Opcodes.INVOKEVIRTUAL))
    private void openingScreen(Screen screen, CallbackInfo callback) {
        if (screen instanceof ContainerScreen && screen instanceof IScreenAccessor)
            ((IScreenAccessor) screen).addBtn(new LockedSlotScreen((ContainerScreen) screen));
    }
}
