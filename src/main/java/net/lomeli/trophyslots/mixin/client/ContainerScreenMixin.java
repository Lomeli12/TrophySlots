package net.lomeli.trophyslots.mixin.client;

import net.lomeli.trophyslots.client.accessors.IContainerScreenAccessor;
import net.lomeli.trophyslots.client.screen.LockedSlotScreen;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends Screen implements IContainerScreenAccessor {
    @Shadow
    protected int left;

    @Shadow
    protected int top;

    public ContainerScreenMixin() {
        super(new StringTextComponent(""));
    }

    @Override
    public int getLeft() {
        return this.left;
    }

    @Override
    public int getTop() {
        return this.top;
    }

    @Inject(method = "onInitialized", at = @At("TAIL"))
    private void onInit(CallbackInfo callback) {
        this.addButton(new LockedSlotScreen((ContainerScreen) (Object) this));
    }
}
