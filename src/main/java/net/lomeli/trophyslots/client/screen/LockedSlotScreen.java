package net.lomeli.trophyslots.client.screen;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.StringTextComponent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class LockedSlotScreen extends Button {
    private static final int GREY_COLOR = 2130706433;
    private final ContainerScreen<?> parentScreen;

    @SuppressWarnings("all")
    public LockedSlotScreen(ContainerScreen<?> parentScreen) {
        super(0, 0, 0, 0, new StringTextComponent(""), null);
        this.field_230693_o_ = false;
        this.parentScreen = parentScreen;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void func_230431_b_(MatrixStack stack, int mouseX, int mouseY, float renderTick) {
        if (!this.field_230694_p_) return;
        Minecraft mc = Minecraft.getInstance();

        for (Slot slot : parentScreen.getContainer().inventorySlots) {
            if (slot.inventory instanceof PlayerInventory) {
                PlayerEntity player = ((PlayerInventory) slot.inventory).player;
                if (!player.abilities.isCreativeMode) {
                    IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                    if (playerSlots != null && !playerSlots.slotUnlocked(slot.getSlotIndex()))
                        drawLockedSlot(mc, slot.xPos, slot.yPos);
                }
            }
        }
    }

    private void drawLockedSlot(Minecraft mc, int xPos, int yPos) {
        if (ClientConfig.slotRenderType == 3)
            return;
        int left = parentScreen.getGuiLeft();
        int top = parentScreen.getGuiTop();
        int x = left + xPos;
        int y = top + yPos;

        MatrixStack matrix = new MatrixStack();
        matrix.push();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (ClientConfig.slotRenderType == 1 || ClientConfig.slotRenderType == 2) {
            RenderSystem.enableLighting();

            this.func_238468_a_(matrix, x, y, x + 16, y + 16, GREY_COLOR, GREY_COLOR);
            RenderSystem.disableLighting();
        }
        if (ClientConfig.slotRenderType == 0 || ClientConfig.slotRenderType == 2) {
            TextureAtlasSprite crossSprite = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
                    .apply(SpriteHandler.CROSS_SPRITE);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            mc.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            AbstractGui.func_238470_a_(matrix, x, y, this.func_230927_p_(), 16, 16, crossSprite);
        }

        GlStateManager.disableBlend();
        matrix.pop();
    }

    @Override
    public void func_230930_b_() {
    }
}
