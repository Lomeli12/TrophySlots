package net.lomeli.trophyslots.client.config;

import net.minecraft.client.gui.GuiScreen;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.network.MessageUpdateWhitelist;

/**
 * GAH! I HAVE NO IDEA WHAT I'M DOING, SEND HELP!
 */
public class GuiWhitelist extends GuiScreen {
    public final GuiScreen parentGui;
    public GuiAchievementList achievementList;

    public GuiWhitelist(GuiScreen parentGui) {
        this.parentGui = parentGui;
    }

    @Override
    public void initGui() {
        achievementList = new GuiAchievementList(this, this.mc);
    }

    @Override
    public void drawScreen(int mx, int my, float f0) {
        this.drawDefaultBackground();
        this.achievementList.drawScreen(mx, my, f0);
        super.drawScreen(mx, my, f0);
    }

    @Override
    protected void mouseClicked(int mx, int my, int i0) {
        if (!this.achievementList.func_148179_a(mx, my, i0))
            super.mouseClicked(mx, my, i0);
    }

    @Override
    protected void mouseMovedOrUp(int mx, int my, int i0) {
        if (i0 != 0 || !this.achievementList.func_148181_b(mx, my, i0))
            super.mouseMovedOrUp(mx, my, i0);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        TrophySlots.packetHandler.sendToServer(new MessageUpdateWhitelist(achievementList.getWhiteList()));
    }
}
