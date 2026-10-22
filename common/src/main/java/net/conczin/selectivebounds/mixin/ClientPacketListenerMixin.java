package net.conczin.selectivebounds.mixin;

import net.conczin.selectivebounds.config.Config;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleUpdateTags", at = @At("TAIL"))
    private void selectivebounds$updateTags(ClientboundUpdateTagsPacket packet, CallbackInfo ci) {
        Config.getInstance().tagManager.clearCaches();
    }
}
