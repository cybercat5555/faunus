package cybercat5555.faunus.entity;

import cybercat5555.faunus.FaunusEntities;
import net.minecraft.entity.EntityType;
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

public class YacareEntity extends AnimalEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public YacareEntity(EntityType<? extends AnimalEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity other)
	{
		return FaunusEntities.YACARE.create(world);
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
	
	protected <E extends YacareEntity> PlayState idleAnimController(final AnimationState<E> event)
	{
		return PlayState.CONTINUE;
	}
}
