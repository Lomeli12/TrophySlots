package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.handlers.ServerEventHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerLoggedIn(ClientConnection connection, ServerPlayerEntity player, CallbackInfo callback) {
        ServerEventHandler.updateClientSlots(player);
    }

    @Inject(method = "method_14556", at = @At("TAIL"))
    private void onPlayerRespawned(ServerPlayerEntity player, DimensionType dimType, boolean boolean_1,
                                   CallbackInfoReturnable<ServerPlayerEntity> callback) {
        ServerEventHandler.updateClientSlots(callback.getReturnValue());
    }
}
