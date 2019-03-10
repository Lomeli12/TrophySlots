package net.lomeli.trophyslots.client.accessors;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public interface IScreenAccessor {
    void addBtn(ButtonWidget button);
}
