package net.conczin.selectivebounds;

import net.conczin.selectivebounds.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

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

    public static boolean shouldBlockOutline(Entity entity, BlockPos blockPos, BlockState blockState, Player player) {
        Config c = Config.getInstance();

        ItemStack toolStack = player.getItemInHand(player.getUsedItemHand());
        Item tool = toolStack.getItem();

        // The item is explicitly black-/whitelisted
        TagManager.FilteredType filteredType = c.tagManager.get(tool, blockState.getBlock());
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

        // The current tool cannot break the block
        if (c.considerCanAttackBlock && !tool.canAttackBlock(blockState, player.level(), blockPos, player)) {
            return true;
        }

        // The item can be placed
        // TODO: Not accurate
        if (c.considerCanBePlaced && tool instanceof BlockItem && blockState.canSurvive(entity.level(), blockPos.above())) {
            return false;
        }

        // The current tool is not the correct tool for the block
        return c.considerIsCorrectToolForDrops && !tool.isCorrectToolForDrops(toolStack, blockState);
    }
}