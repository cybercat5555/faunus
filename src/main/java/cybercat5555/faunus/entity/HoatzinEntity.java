package cybercat5555.faunus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HoatzinEntity extends ChickenEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public HoatzinEntity(EntityType<? extends ChickenEntity> entityType, World world)
	{
		super(entityType, world);
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

	protected <E extends HoatzinEntity> PlayState idleAnimController(final AnimationState<E> event)
	{
		return PlayState.CONTINUE;
	}
}
