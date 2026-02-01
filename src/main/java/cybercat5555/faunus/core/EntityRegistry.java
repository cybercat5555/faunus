package cybercat5555.faunus.core;

import cybercat5555.faunus.Faunus;
import cybercat5555.faunus.core.entity.livingEntity.*;
import cybercat5555.faunus.core.entity.projectile.CocoaBeanProjectile;
import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class EntityRegistry {
    private EntityRegistry() {
    }

    //-- Multiple --------------------------------------------------------------
    public static EntityType<SongbirdEntity> SONGBIRD = register("songbird", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SongbirdEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.65f))
            .build());

    public static EntityType<IguanaEntity> IGUANA = register("iguana", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, IguanaEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.65f))
            .build());

    //-- Jungles ---------------------------------------------------------------
    public static EntityType<CapuchinEntity> CAPUCHIN = register("capuchin", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CapuchinEntity::new)
            .dimensions(EntityDimensions.fixed(1f, 0.8f))
            .build());

    public static final EntityType<TapirEntity> TAPIR = register("tapir", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TapirEntity::new)
            .dimensions(EntityDimensions.fixed(0.9f, 1.4f))
            .build());

    public static final EntityType<ConstrictorEntity> CONSTRICTOR = register("constrictor", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ConstrictorEntity::new)
            .dimensions(EntityDimensions.fixed(1.5f, 0.25f))
            .build());

    public static final EntityType<QuetzalEntity> QUETZAL = register("quetzal", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, QuetzalEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.65f))
            .build());

    public static final EntityType<HoatzinEntity> HOATZIN = register("hoatzin", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HoatzinEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 0.75f))
            .build());

    public static final EntityType<PiranhaEntity> PIRANHA = register("piranha", FabricEntityTypeBuilder.Mob.createMob()
            .spawnGroup(SpawnGroup.WATER_CREATURE)
            .entityFactory(PiranhaEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.675f))
            .build());

    //-- Swamps ----------------------------------------------------------------
    public static final EntityType<ArapaimaEntity> ARAPAIMA = register("arapaima", FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, ArapaimaEntity::new)
            .dimensions(EntityDimensions.fixed(1.2f, 0.6f))
            .build());

    public static final EntityType<SnappingTurtleEntity> SNAPPING_TURTLE = register("snapping_turtle", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnappingTurtleEntity::new)
            .dimensions(EntityDimensions.fixed(1f, 0.65f))
            .build());

    public static final EntityType<CrayfishEntity> CRAYFISH = register("crayfish", FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, CrayfishEntity::new)
            .dimensions(EntityDimensions.fixed(0.95f, 0.25f))
            .build());

    public static final EntityType<LeechEntity> LEECH = register("leech", FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, LeechEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.15f))
            .build());

    public static final EntityType<YacareEntity> YACARE = register("yacare", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, YacareEntity::new)
            .dimensions(EntityDimensions.fixed(1f, 0.5f))
            .build());

    public static final EntityType<YacareManEaterEntity> YACARE_MANEATER = register("yacare_maneater", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, YacareManEaterEntity::new)
            .dimensions(EntityDimensions.fixed(3f, 1f))
            .build());

    /* Projectile */

    public static final EntityType<CocoaBeanProjectile> COCOA_BEAN_PROJECTILE = register("cocoa_bean_projectile", FabricEntityTypeBuilder.<CocoaBeanProjectile>create(SpawnGroup.MISC, CocoaBeanProjectile::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .build());


    @SuppressWarnings("all")
    public static void init() {
        Faunus.LOG.info("Registering entity's attributes for " + Faunus.MODID);

        FabricDefaultAttributeRegistry.register(SONGBIRD, SongbirdEntity.createSongbirdAttributes());
        FabricDefaultAttributeRegistry.register(IGUANA, IguanaEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CAPUCHIN, CapuchinEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(TAPIR, TapirEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CONSTRICTOR, ConstrictorEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(QUETZAL, QuetzalEntity.createQuetzalAttributes());
        FabricDefaultAttributeRegistry.register(HOATZIN, HoatzinEntity.createParrotAttributes());
        FabricDefaultAttributeRegistry.register(PIRANHA, PiranhaEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ARAPAIMA, ArapaimaEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(SNAPPING_TURTLE, SnappingTurtleEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CRAYFISH, CrayfishEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LEECH, LeechEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(YACARE, YacareEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(YACARE_MANEATER, YacareManEaterEntity.createMobAttributes());

    }


    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, FaunusID.content(name), type);
    }
}
