package net.lomeli.trophyslots.mixin.client;

import net.lomeli.trophyslots.client.accessors.IScreenAccessor;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin implements IScreenAccessor {

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T button);

    @Override
    public void addBtn(ButtonWidget button) {
        addButton(button);
    }
}
