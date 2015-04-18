package net.lomeli.trophyslots.compat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Just to make things easier on myself, mod specific compatibility is now
 * modular. So, if a specific mod doesn't exist, the module isn't registered
 * thus doesn't import classes that don't exists. Also makes using up
 * custom slots a lot easier as the module will take care of that instead of having
 * a large list of if states in EventHandlerClient
 *
 * @see net.lomeli.trophyslots.client.EventHandlerClient
 */
public interface ICompatModule {
    void replaceSlots(GuiContainer container, EntityPlayer player);

    boolean isCompatibleGui(GuiContainer gui);
}
