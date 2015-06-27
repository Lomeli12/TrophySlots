package net.lomeli.trophyslots.client.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumChatFormatting;

import net.minecraftforge.common.AchievementPage;

/**
 * GAH! I HAVE NO IDEA WHAT I'M DOING, SEND HELP!
 */
public class GuiAchievementList extends GuiListExtended {
    private AchievementEntry[] entryList;

    public GuiAchievementList(GuiScreen parent, Minecraft mc) {
        super(mc, parent.width, parent.height, 63, parent.height - 32, 20);
        List<Achievement> achievements = new ArrayList<Achievement>();
        Set<AchievementPage> achievementPages = AchievementPage.getAchievementPages();
        for (AchievementPage page : achievementPages) {
            achievements.addAll(page.getAchievements());
        }
        achievements.addAll(AchievementList.achievementList);
        this.entryList = new AchievementEntry[achievements.size()];
        for (int i = 0; i < entryList.length; i++) {
            this.entryList[i] = new AchievementEntry(achievements.get(i));
        }
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return entryList[index];
    }

    @Override
    protected int getSize() {
        return entryList.length;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public List<String> getWhiteList() {
        List<String> whiteList = new ArrayList<String>();
        for (AchievementEntry entry : entryList) {
            if (entry.isAchievementAdded())
                whiteList.add(entry.getAchievement().statId);
        }
        return whiteList;
    }

    public class AchievementEntry implements IGuiListEntry {
        private final Achievement achievement;
        private final GuiButton button;
        private Minecraft mc;
        private boolean addAchievement;

        public AchievementEntry(Achievement achievement) {
            this.achievement = achievement;
            this.mc = Minecraft.getMinecraft();
            this.button = new GuiButton(0, 0, 0, 75, 18, I18n.format(achievement.func_150951_e().getUnformattedText(), new Object[0]));
            this.button.width *= 2.5;
        }

        @Override
        public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator tess, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
            this.button.xPosition = p_148279_2_ + 53;
            this.button.yPosition = p_148279_3_;
            if (addAchievement)
                this.button.displayString = EnumChatFormatting.GREEN + this.button.displayString;
            else
                this.button.displayString = EnumChatFormatting.RED + this.button.displayString;

            this.button.drawButton(this.mc, p_148279_7_, p_148279_8_);
        }

        @Override
        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            if (this.button.mousePressed(this.mc, p_148278_2_, p_148278_3_)) {
                this.addAchievement = !addAchievement;
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
            this.button.mouseReleased(p_148277_2_, p_148277_3_);
        }

        public Achievement getAchievement() {
            return achievement;
        }

        public boolean isAchievementAdded() {
            return addAchievement;
        }
    }
}
