package net.conczin.selectivebounds.mixin;

import net.conczin.selectivebounds.Common;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelExtractor.class, priority = 900)
public class LevelExtractorMixin {
    @Shadow
    private ClientLevel level;

    @Inject(method = "extractBlockOutline", at = @At("TAIL"))
    private void selectivebounds$extractBlockOutline(Camera camera, LevelRenderState renderState, CallbackInfo ci) {
        BlockOutlineRenderState outline = renderState.blockOutlineRenderState;
        if (outline == null) {
            return;
        }

        BlockPos blockPos = outline.pos();
        BlockState blockState = this.level.getBlockState(blockPos);
        Entity entity = camera.entity();
        if (entity instanceof Player player && Common.shouldBlockOutline(entity, blockPos, blockState, player)) {
            renderState.blockOutlineRenderState = null;
        }
    }
}
