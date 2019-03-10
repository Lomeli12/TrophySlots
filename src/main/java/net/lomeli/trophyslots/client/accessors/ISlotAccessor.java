package net.lomeli.trophyslots.client.accessors;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ISlotAccessor {
    int getSlotIndex();
}
