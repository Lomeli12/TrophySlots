package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.handlers.ServerEventHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerLoggedIn(ClientConnection connection, ServerPlayerEntity player, CallbackInfo callbackInfo) {
        ServerEventHandler.onPlayerLoggedIn(player);
    }
}
