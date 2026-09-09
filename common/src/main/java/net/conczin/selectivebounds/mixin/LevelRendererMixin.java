package net.conczin.selectivebounds.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.conczin.selectivebounds.Common;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 900)
public class LevelRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private ClientLevel level;

    @Inject(method = "renderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void selectivebounds$renderBlockOutline(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean translucent, LevelRenderState renderState, CallbackInfo ci) {
        BlockOutlineRenderState outline = renderState.blockOutlineRenderState;
        if (outline == null) {
            return;
        }

        BlockPos blockPos = outline.pos();
        BlockState blockState = this.level.getBlockState(blockPos);
        Entity entity = this.minecraft.getCameraEntity();
        if (entity instanceof Player player && Common.shouldBlockOutline(entity, blockPos, blockState, player)) {
            renderState.blockOutlineRenderState = null;
            ci.cancel();
        }
    }
}
