package net.conczin.selectivebounds;

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
}