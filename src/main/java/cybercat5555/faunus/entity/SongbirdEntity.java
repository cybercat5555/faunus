package cybercat5555.faunus.entity;

import cybercat5555.faunus.FaunusEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SongbirdEntity extends AnimalEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public SongbirdEntity(EntityType<? extends SongbirdEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createSongbirdAttributes()
	{
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4f)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return geoCache;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
	}

	protected <E extends SongbirdEntity> PlayState idleAnimController(final AnimationState<E> state)
	{
		return PlayState.CONTINUE;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity other)
	{
		return FaunusEntities.SONGBIRD.create(world);
	}
	
}
