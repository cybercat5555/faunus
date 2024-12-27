package cybercat5555.faunus.common.config;

import cybercat5555.faunus.common.tags.FaunusBiomeTags;
import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.entity.livingEntity.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

import static cybercat5555.faunus.common.config.MobSpawningConfig.*;

public class SpawnHandler {

    public static void removeSpawn() {
        /* PIG */
        BiomeModifications.create(new Identifier("remove_pig_spawn"))
                .add(
                        ModificationPhase.REMOVALS,
                        BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                        context -> context.getSpawnSettings().removeSpawns((spawnGroup, spawnEntry) -> spawnEntry.type == EntityType.PIG));

        /* COW */
        BiomeModifications.create(new Identifier("remove_cow_spawn"))
                .add(
                        ModificationPhase.REMOVALS,
                        BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                        context -> context.getSpawnSettings().removeSpawns((spawnGroup, spawnEntry) -> spawnEntry.type == EntityType.COW));

        /* SHEEP */
        BiomeModifications.create(new Identifier("remove_sheep_spawn"))
                .add(
                        ModificationPhase.REMOVALS,
                        BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                        context -> context.getSpawnSettings().removeSpawns((spawnGroup, spawnEntry) -> spawnEntry.type == EntityType.SHEEP));
    }

    public static void addSpawn() {
        /* ARAPAIMA */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_ARAPAIMA),
                SpawnGroup.WATER_CREATURE,
                EntityRegistry.ARAPAIMA,
                ARAPAIMA_SPAWN_WEIGHT, ARAPAIMA_SPAWN_MIN_GROUP, ARAPAIMA_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.ARAPAIMA,
                SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.OCEAN_FLOOR,
                ArapaimaEntity::canMobSpawn);

        /* CAPUCHIN */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_CAPUCHIN),
                SpawnGroup.AMBIENT,
                EntityRegistry.CAPUCHIN,
                CAPUCHIN_SPAWN_WEIGHT, CAPUCHIN_SPAWN_MIN_GROUP, CAPUCHIN_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.CAPUCHIN,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                CapuchinEntity::canSpawn);

        /* CRAYFISH */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_CRAYFISH),
                SpawnGroup.WATER_CREATURE,
                EntityRegistry.CRAYFISH,
                CRAYFISH_SPAWN_WEIGHT, CRAYFISH_SPAWN_MIN_GROUP, CRAYFISH_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.CRAYFISH,
                SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.OCEAN_FLOOR,
                CrayfishEntity::canSpawn);

        /* HOATZIN */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_HOATZIN),
                SpawnGroup.CREATURE,
                EntityRegistry.HOATZIN,
                HOATZIN_SPAWN_WEIGHT, HOATZIN_SPAWN_MIN_GROUP, HOATZIN_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.HOATZIN,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                HoatzinEntity::canSpawnHoatzin);

        /* IGUANA */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_IGUANA),
                SpawnGroup.CREATURE,
                EntityRegistry.IGUANA,
                IGUANA_SPAWN_WEIGHT, IGUANA_SPAWN_MIN_GROUP, IGUANA_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.IGUANA,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                IguanaEntity::canSpawn);

        /* LEECH */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_LEECH),
                SpawnGroup.WATER_CREATURE,
                EntityRegistry.LEECH,
                LEECH_SPAWN_WEIGHT, LEECH_SPAWN_MIN_GROUP, LEECH_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.LEECH,
                SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.OCEAN_FLOOR,
                LeechEntity::canSpawn);

        /* PIRANHA */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_PIRANHA),
                SpawnGroup.WATER_CREATURE,
                EntityRegistry.PIRANHA,
                PIRANHA_SPAWN_WEIGHT, PIRANHA_SPAWN_MIN_GROUP, PIRANHA_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.PIRANHA,
                SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.OCEAN_FLOOR,
                PiranhaEntity::canSpawn);

        /* QUETZAL */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_QUETZAL),
                SpawnGroup.CREATURE,
                EntityRegistry.QUETZAL,
                QUETZAL_SPAWN_WEIGHT, QUETZAL_SPAWN_MIN_GROUP, QUETZAL_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.QUETZAL,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                QuetzalEntity::canSpawnQuetzal);

        /* SNAPPING_TURTLE */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_SNAPPING_TURTLE),
                SpawnGroup.CREATURE,
                EntityRegistry.SNAPPING_TURTLE,
                SNAPPING_TURTLE_SPAWN_WEIGHT, SNAPPING_TURTLE_SPAWN_MIN_GROUP, SNAPPING_TURTLE_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.SNAPPING_TURTLE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                SnappingTurtleEntity::canSpawn);

        /* SONGBIRD */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_SONGBIRD),
                SpawnGroup.CREATURE,
                EntityRegistry.SONGBIRD,
                SONGBIRD_SPAWN_WEIGHT, SONGBIRD_SPAWN_MIN_GROUP, SONGBIRD_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.SONGBIRD,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                SongbirdEntity::canSpawnSongbird);

        /* TAPIR */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_TAPIR),
                SpawnGroup.CREATURE,
                EntityRegistry.TAPIR,
                TAPIR_SPAWN_WEIGHT, TAPIR_SPAWN_MIN_GROUP, TAPIR_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.TAPIR,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn);

        /* YACARE */
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(FaunusBiomeTags.SPAWNS_YACARE),
                SpawnGroup.CREATURE,
                EntityRegistry.YACARE,
                YACARE_SPAWN_WEIGHT, YACARE_SPAWN_MIN_GROUP, YACARE_SPAWN_MAX_GROUP
        );

        SpawnRestriction.register(
                EntityRegistry.YACARE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                YacareEntity::canSpawnYacare);
    }
}
