package cybercat5555.faunus;

import cybercat5555.faunus.entity.ArapaimaEntity;
import cybercat5555.faunus.entity.CapuchinEntity;
import cybercat5555.faunus.entity.CrayfishEntity;
import cybercat5555.faunus.entity.HoatzinEntity;
import cybercat5555.faunus.entity.PiranhaEntity;
import cybercat5555.faunus.entity.QuetzalEntity;
import cybercat5555.faunus.entity.SnappingTurtleEntity;
import cybercat5555.faunus.entity.TapirEntity;
import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class FaunusEntities
{
	private FaunusEntities() {}

	//-- Jungles ---------------------------------------------------------------
	public static EntityType<CapuchinEntity> CAPUCHIN = register("capuchin", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CapuchinEntity::new)
		.dimensions(EntityDimensions.fixed(1f, 0.8f))
		.build());

	public static final EntityType<TapirEntity> TAPIR = register("tapir", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TapirEntity::new)
		.dimensions(EntityDimensions.fixed(0.9f, 1.4f))
		.build());

	public static final EntityType<QuetzalEntity> QUETZAL = register("quetzal", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, QuetzalEntity::new)
		.dimensions(EntityDimensions.fixed(0.5f, 0.65f))
		.build());

	public static final EntityType<HoatzinEntity> HOATZIN = register("hoatzin", FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HoatzinEntity::new)
		.dimensions(EntityDimensions.fixed(0.6f, 0.65f))
		.build());

	public static final EntityType<PiranhaEntity> PIRANHA = register("piranha", FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, PiranhaEntity::new)
		.dimensions(EntityDimensions.fixed(0.5f, 0.475f))
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

	public static void init()
	{
		FabricDefaultAttributeRegistry.register(CAPUCHIN, CapuchinEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(TAPIR, TapirEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(QUETZAL, QuetzalEntity.createParrotAttributes());
		FabricDefaultAttributeRegistry.register(HOATZIN, HoatzinEntity.createChickenAttributes());
		FabricDefaultAttributeRegistry.register(PIRANHA, PiranhaEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(ARAPAIMA, ArapaimaEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(SNAPPING_TURTLE, SnappingTurtleEntity.createTurtleAttributes());
		FabricDefaultAttributeRegistry.register(CRAYFISH, CrayfishEntity.createMobAttributes());
	}

	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type)
	{
		return Registry.register(Registries.ENTITY_TYPE, FaunusID.content(name), type);
	}
}
