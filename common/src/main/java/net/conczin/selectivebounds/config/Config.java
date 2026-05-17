package net.conczin.selectivebounds.config;

import net.conczin.selectivebounds.Common;
import net.conczin.selectivebounds.TagManager;

import java.util.ArrayList;
import java.util.List;

public final class Config extends JsonConfig {
    private static final Config INSTANCE = loadOrCreate(new Config(), Config.class);

    public Config() {
        super(Common.MOD_ID);
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    @Override
    int getVersion() {
        return 0;
    }

    public static class OutlineOverride {
        public OutlineOverride(String item, String block, boolean show) {
            this.item = item;
            this.block = block;
            this.show = show;
        }

        public String item;
        public String block;
        public boolean show;
    }

    @SuppressWarnings("unused")
    public String _documentation = "https://github.com/Luke100000/SelectiveBounds/blob/main/common/src/main/java/net/conczin/selectivebounds/config/Config.java";

    // Show bounds when sneaking
    public boolean showOnSneak = true;

    // Show bounds when interacting with a block (placement, breaking, etc.)
    public boolean showOnAction = true;
    public float showOnActionTime = 2.0f;

    // Hide bounds when attack is blocked (e.g., sword in creative mode)
    public boolean considerCanAttackBlock = true;

    // Hide bounds when the tool is not the correct tool for drops
    public boolean considerIsCorrectToolForDrops = true;

    // Shows bounds when a block can be placed (Experimental)
    public boolean considerCanBePlaced = false;

    // Overrides of item-on-block interactions
    // Supports item/block resource locations, tags, and "all".
    public List<OutlineOverride> overrides = new ArrayList<>();

    {
        overrides.add(new OutlineOverride("minecraft:stick", "#minecraft:candles", true));
        overrides.add(new OutlineOverride("all", "minecraft:nether_portal", false));
    }

    transient public TagManager tagManager = new TagManager(this);
}
