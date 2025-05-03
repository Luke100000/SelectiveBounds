package net.conczin.selectivebounds;

import net.conczin.selectivebounds.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class Common {
    private static long lastInteractTime;
    public static final String MOD_ID = "selectivebounds";

    public static void init() {
        // No-op
    }

    public static void interact() {
        lastInteractTime = System.currentTimeMillis();
    }

    public static long getLastInteractTime() {
        return lastInteractTime;
    }

    public static boolean shouldShowOutline(Entity entity, BlockPos blockPos, BlockState blockState, Player player) {
        Config c = Config.getInstance();

        // Sneaking
        if (c.showOnSneak && entity.isShiftKeyDown()) return false;

        // A block has been placed or broken recently
        long diff = System.currentTimeMillis() - Common.getLastInteractTime();
        if (c.showOnAction && diff < c.showOnActionTime * 1000) return false;

        Item tool = player.getItemInHand(player.getUsedItemHand()).getItem();

        // The current tool cannot break the block
        if (c.considerCanAttackBlock && !tool.canAttackBlock(blockState, player.level(), blockPos, player)) {
            return true;
        }

        // The current tool is not the correct tool for the block
        return c.considerIsCorrectToolForDrops && !tool.isCorrectToolForDrops(blockState);
    }
}