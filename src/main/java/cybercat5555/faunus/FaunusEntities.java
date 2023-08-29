package cybercat5555.faunus;

import cybercat5555.faunus.entity.ArapaimaEntity;
import cybercat5555.faunus.entity.PiranhaEntity;
import cybercat5555.faunus.entity.TapirEntity;
import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class FaunusEntities
{
	private FaunusEntities() {}

	public static final EntityType<TapirEntity> TAPIR = Registry.register
	(
		Registries.ENTITY_TYPE, 
		FaunusID.content("tapir"), 
		FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TapirEntity::new)
			.dimensions(EntityDimensions.fixed(0.9f, 1.4f))
			.build()
	);

	public static final EntityType<PiranhaEntity> PIRANHA = Registry.register
	(
		Registries.ENTITY_TYPE,
		FaunusID.content("piranha"),
		FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, PiranhaEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
			.build()
	);

	public static final EntityType<ArapaimaEntity> ARAPAIMA = Registry.register
	(
		Registries.ENTITY_TYPE,
		FaunusID.content("arapaima"),
		FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, ArapaimaEntity::new)
			.dimensions(EntityDimensions.fixed(1.2f, 0.5f))
			.build()
	);

	public static void init()
	{
		FabricDefaultAttributeRegistry.register(TAPIR, TapirEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(PIRANHA, PiranhaEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(ARAPAIMA, ArapaimaEntity.createFishAttributes());
	}
}
