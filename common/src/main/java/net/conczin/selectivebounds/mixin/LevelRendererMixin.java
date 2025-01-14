package net.conczin.selectivebounds.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.conczin.selectivebounds.Common;
import net.conczin.selectivebounds.config.Config;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void selectivebounds$renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (entity instanceof Player player) {
            Config c = Config.getInstance();

            // Sneaking
            if (c.showOnSneak && entity.isShiftKeyDown()) return;

            // A block has been placed or broken recently
            long diff = System.currentTimeMillis() - Common.getLastInteractTime();
            if (c.showOnAction && diff < c.showOnActionTime * 1000) return;

            Item tool = player.getItemInHand(player.getUsedItemHand()).getItem();

            // The current tool can not break the block
            if (c.considerCanAttackBlock && !tool.canAttackBlock(blockState, player.level(), blockPos, player)) {
                ci.cancel();
            }

            // The current tool is not the correct tool for the block
            if (c.considerIsCorrectToolForDrops && !tool.isCorrectToolForDrops(blockState)) {
                ci.cancel();
            }
        }
    }
}
