package com.explorassist.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.List;

public class LocateEngine {

    public static final int MAX_DISTANCE = 3000;

    // All When Dungeons Arise structure IDs
    private static final String[] ALL_STRUCTURES = {
        // Major
        "dungeons_arise:aviary",
        "dungeons_arise:bandit_towers",
        "dungeons_arise:bandit_village",
        "dungeons_arise:ceryneian_hind",
        "dungeons_arise:coliseum",
        "dungeons_arise:foundry",
        "dungeons_arise:greenwood_pub",
        "dungeons_arise:heavenly_challenger",
        "dungeons_arise:heavenly_conqueror",
        "dungeons_arise:heavenly_rider",
        "dungeons_arise:illager_campsite",
        "dungeons_arise:illager_corsair",
        "dungeons_arise:illager_fort",
        "dungeons_arise:illager_galley",
        "dungeons_arise:infested_temple",
        "dungeons_arise:keep_kayra",
        "dungeons_arise:mechanical_nest",
        "dungeons_arise:mining_system",
        "dungeons_arise:monastery",
        "dungeons_arise:mushroom_house",
        "dungeons_arise:mushroom_mine",
        "dungeons_arise:plague_asylum",
        "dungeons_arise:scorched_mines",
        "dungeons_arise:shiraz_palace",
        "dungeons_arise:thornborn_towers",
        "dungeons_arise:typhon",
        "dungeons_arise:undead_pirate_ship",
        // Minor
        "dungeons_arise:abandoned_temple",
        "dungeons_arise:bathhouse",
        "dungeons_arise:fishing_hut",
        "dungeons_arise:jungle_tree_house",
        "dungeons_arise:lighthouse",
        "dungeons_arise:merchant_campsite",
        "dungeons_arise:wishing_well"
    };

    public static LocateResult findNearest(ServerLevel level, BlockPos playerPos) {
        Registry<Structure> structureRegistry = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE);

        LocateResult nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (String structureId : ALL_STRUCTURES) {
            ResourceLocation rl = new ResourceLocation(structureId);
            ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, rl);

            Structure structure = structureRegistry.get(key);
            if (structure == null) continue;

            HolderSet.Direct<Structure> holderSet = HolderSet.direct(
                    structureRegistry.getHolderOrThrow(key)
            );

            var result = level.getChunkSource()
                    .getGenerator()
                    .findNearestMapStructure(level, holderSet, playerPos, 100, false);

            if (result == null) continue;

            BlockPos found = result.getFirst();
            double dist = Math.sqrt(playerPos.distSqr(found));

            if (dist <= MAX_DISTANCE && dist < nearestDist) {
                nearestDist = dist;
                nearest = new LocateResult(structureId, found, (int) dist);
            }
        }

        return nearest;
    }

    public static class LocateResult {
        public final String structureId;
        public final BlockPos pos;
        public final int distance;

        public LocateResult(String structureId, BlockPos pos, int distance) {
            this.structureId = structureId;
            this.pos = pos;
            this.distance = distance;
        }
    }
}
