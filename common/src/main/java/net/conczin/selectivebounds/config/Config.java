package net.conczin.selectivebounds.config;

import net.conczin.selectivebounds.Common;

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

    @SuppressWarnings("unused")
    public String _documentation = "https://github.com/Luke100000/SelectiveBounds/blob/1.20.1/common/src/main/java/net/conczin/selectivebounds/config/Config.java";

    // Show bounds when sneaking
    public boolean showOnSneak = true;

    // Show bounds when interacting with a block (placement, breaking, etc.)
    public boolean showOnAction = true;
    public float showOnActionTime = 2.0f;

    // Hide bounds when attack is blocked (e.g., sword in creative mode)
    public boolean considerCanAttackBlock = true;

    // Hide bounds when the tool is not the correct tool for drops
    public boolean considerIsCorrectToolForDrops = true;
}
