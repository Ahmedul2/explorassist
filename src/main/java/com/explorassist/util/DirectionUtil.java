package com.explorassist.util;

import net.minecraft.core.BlockPos;

public class DirectionUtil {

    /**
     * Returns cardinal direction from playerPos toward targetPos.
     */
    public static String getDirection(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();

        double angle = Math.toDegrees(Math.atan2(dz, dx));

        // Normalize to 0-360
        if (angle < 0) angle += 360;

        if (angle >= 337.5 || angle < 22.5)   return "East";
        if (angle < 67.5)                       return "SE";
        if (angle < 112.5)                      return "South";
        if (angle < 157.5)                      return "SW";
        if (angle < 202.5)                      return "West";
        if (angle < 247.5)                      return "NW";
        if (angle < 292.5)                      return "North";
        return "NE";
    }

    /**
     * Formats structure ID into a readable name.
     * e.g. "dungeons_arise:shiraz_palace" → "Shiraz Palace"
     */
    public static String formatName(String structureId) {
        String name = structureId.contains(":") 
            ? structureId.split(":")[1] 
            : structureId;
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)))
                  .append(part.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
