package net.conczin.selectivebounds.mixin;

import net.conczin.selectivebounds.config.Config;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = {"<init>", "handleUpdateTags"}, at = @At("RETURN"))
    private void selectivebounds$clearTagCaches(CallbackInfo ci) {
        Config.getInstance().tagManager.clearCaches();
    }
}
