package com.explorassist.engine;

public class CooldownManager {

    // 10 minutes in milliseconds
    private static final long COOLDOWN_MS = 10 * 60 * 1000L;

    private static long lastUsed = 0L;

    public static boolean isReady() {
        return (System.currentTimeMillis() - lastUsed) >= COOLDOWN_MS;
    }

    public static void markUsed() {
        lastUsed = System.currentTimeMillis();
    }

    public static long getRemainingSeconds() {
        long elapsed = System.currentTimeMillis() - lastUsed;
        long remaining = COOLDOWN_MS - elapsed;
        return remaining > 0 ? remaining / 1000L : 0L;
    }
}
