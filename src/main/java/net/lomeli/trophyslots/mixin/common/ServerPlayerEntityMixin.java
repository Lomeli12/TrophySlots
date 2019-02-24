package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.handlers.PlayerHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo callback) {
        PlayerHandler.onPlayerDeath((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "method_14203", at = @At("HEAD"))
    private void clonePlayer(ServerPlayerEntity oldPlayer, boolean switchingDim, CallbackInfo callback) {
        PlayerHandler.clonePlayer((ServerPlayerEntity) (Object) this, oldPlayer, switchingDim);
    }
}
