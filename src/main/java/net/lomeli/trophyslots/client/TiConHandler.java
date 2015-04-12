package net.lomeli.trophyslots.client;

import tconstruct.tools.gui.CraftingStationGui;
import tconstruct.tools.gui.PartCrafterGui;
import tconstruct.tools.gui.ToolForgeGui;
import tconstruct.tools.gui.ToolStationGui;
import tconstruct.tools.logic.CraftingStationLogic;

import net.minecraft.client.gui.inventory.GuiContainer;

import net.lomeli.trophyslots.core.ObfUtil;

public class TiConHandler {
    public static int applyXChange(GuiContainer gui) {
        int change = 0;
        if (gui instanceof CraftingStationGui) {
            if (((CraftingStationGui) gui).hasChest())
                change -= CraftingStationGui.CHEST_WIDTH / 2;
        } else if (gui instanceof PartCrafterGui) {
            PartCrafterGui crafter = (PartCrafterGui) gui;
            ObfUtil.setFieldAccessible(CraftingStationGui.class, "logic");
            ObfUtil.setFieldAccessible(PartCrafterGui.class, "drawChestPart");
            boolean flag = ObfUtil.getFieldValue(PartCrafterGui.class, crafter, "drawChestPart");
            if (flag)
                change -= CraftingStationGui.CHEST_WIDTH / 2;
        } else if (gui instanceof ToolForgeGui || gui instanceof ToolStationGui)
            change -= (CraftingStationGui.CHEST_WIDTH / 2) - 3;
        return change;
    }

    public static int applyYChange(GuiContainer gui) {
        int change = 0;
        if (gui instanceof CraftingStationGui) {
            CraftingStationGui craft = (CraftingStationGui) gui;
            ObfUtil.setFieldAccessible(CraftingStationGui.class, "logic");
            CraftingStationLogic logic = ObfUtil.getFieldValue(CraftingStationGui.class, craft, "logic");
            if (logic != null && logic.doubleChest != null)
                change += 11;
        }
        return change;
    }
}
