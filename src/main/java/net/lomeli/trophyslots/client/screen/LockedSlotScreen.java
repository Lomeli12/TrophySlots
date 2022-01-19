package net.lomeli.trophyslots.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.opengl.GL11;

public class LockedSlotScreen extends Button {
    private static final int GREY_COLOR = 2130706433;
    private final AbstractContainerScreen<?> parentScreen;

    @SuppressWarnings("all")
    public LockedSlotScreen(AbstractContainerScreen<?> parentScreen) {
        super(0, 0, 0, 0, new TextComponent(""), null);
        this.active = false;
        this.parentScreen = parentScreen;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void render(PoseStack stack, int mouseX, int mouseY, float renderTick) {
        Minecraft mc = Minecraft.getInstance();

        for (Slot slot : parentScreen.getMenu().slots) {
            if (slot.container instanceof Inventory) {
                Player player = ((Inventory) slot.container).player;
                if (!player.isCreative()) {
                    IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                    if (playerSlots != null && !playerSlots.slotUnlocked(slot.getSlotIndex()))
                        drawLockedSlot(mc, slot.x, slot.y);
                }
            }
        }
    }

    private void drawLockedSlot(Minecraft mc, int xPos, int yPos) {
        if (ClientConfig.renderType == SlotRenderType.NONE)
            return;
        int left = parentScreen.getGuiLeft();
        int top = parentScreen.getGuiTop();
        int x = left + xPos;
        int y = top + yPos;

        PoseStack matrix = new PoseStack();
        matrix.pushPose();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (ClientConfig.renderType.isGreyOut()) {
            Lighting.setupForEntityInInventory();
            this.fillGradient(matrix, x, y, x + 16, y + 16, GREY_COLOR, GREY_COLOR);
            Lighting.setupFor3DItems();
        }
        if (ClientConfig.renderType.drawCross()) {
            TextureAtlasSprite crossSprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(SpriteHandler.CROSS_SPRITE);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            GuiComponent.blit(matrix, x, y, 100, 16, 16, crossSprite);
        }

        GlStateManager._disableBlend();
        matrix.popPose();
    }

    @Override
    public void onPress() {
    }
}
