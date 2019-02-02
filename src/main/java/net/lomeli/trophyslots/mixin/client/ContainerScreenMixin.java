package net.lomeli.trophyslots.mixin.client;

import net.lomeli.trophyslots.client.accessors.IContainerScreenAccessor;
import net.minecraft.client.gui.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin implements IContainerScreenAccessor {
    @Shadow
    protected int left;

    @Shadow
    protected int top;

    @Override
    public int getLeft() {
        return this.left;
    }

    @Override
    public int getTop() {
        return this.top;
    }
}
