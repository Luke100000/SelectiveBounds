package net.conczin.selectivebounds;

import net.conczin.selectivebounds.config.Config;
import net.conczin.selectivebounds.mixin.BlockItemInvoker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Common {
    private static long lastInteractTime;
    public static final String MOD_ID = "selectivebounds";

    public static void init() {
        //noinspection ResultOfMethodCallIgnored
        Config.getInstance();
    }

    public static void interact() {
        lastInteractTime = System.currentTimeMillis();
    }

    public static long getLastInteractTime() {
        return lastInteractTime;
    }

    public static boolean shouldBlockOutline(Entity entity, BlockPos blockPos, BlockState blockState, Player player, HitResult hitResult) {
        Config c = Config.getInstance();

        ItemStack toolStack = player.getItemInHand(player.getUsedItemHand());
        Item tool = toolStack.getItem();

        // Main-hand overrides take precedence when both hands match.
        TagManager.FilteredType filteredType = c.tagManager.get(tool, blockState.getBlock());
        if (filteredType == TagManager.FilteredType.IGNORE && !player.getOffhandItem().isEmpty()) {
            filteredType = c.tagManager.get(player.getOffhandItem().getItem(), blockState.getBlock());
        }

        if (filteredType == TagManager.FilteredType.BLACKLIST) {
            return true;
        } else if (filteredType == TagManager.FilteredType.WHITELIST) {
            return false;
        }

        // Sneaking
        if (c.showOnSneak && entity.isShiftKeyDown()) return false;

        // A block has been placed or broken recently
        long diff = System.currentTimeMillis() - Common.getLastInteractTime();
        if (c.showOnAction && diff < c.showOnActionTime * 1000) return false;

        // Use vanilla's placement checks without placing or consuming the item.
        if (c.considerCanBePlaced && hitResult instanceof BlockHitResult blockHit && blockHit.getBlockPos().equals(blockPos)) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (!(stack.getItem() instanceof BlockItem blockItem) || !blockItem.getBlock().isEnabled(player.level().enabledFeatures())) continue;

                BlockPlaceContext context = new BlockPlaceContext(player, hand, stack, blockHit);
                if (!context.canPlace()) continue;

                context = blockItem.updatePlacementContext(context);
                if (context != null && ((BlockItemInvoker) blockItem).selectivebounds$getPlacementState(context) != null) {
                    return false;
                }
            }
        }

        // The current tool cannot break the block
        if (c.considerCanAttackBlock && !tool.canAttackBlock(blockState, player.level(), blockPos, player)) {
            return true;
        }

        // The current tool is not the correct tool for the block
        return c.considerIsCorrectToolForDrops && !tool.isCorrectToolForDrops(toolStack, blockState);
    }
}
