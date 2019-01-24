package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.handlers.EntityItemHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void playerCollision(PlayerEntity player, CallbackInfo callback) {
        if (!player.world.isClient && EntityItemHandler.onItemEntityPickup(player, getStack()))
            callback.cancel();
    }

    @Shadow
    public abstract ItemStack getStack();
}
