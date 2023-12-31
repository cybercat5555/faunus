package cybercat5555.faunus.entity;

import cybercat5555.faunus.FaunusItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ArapaimaEntity extends SchoolingFishEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
	protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swimming");
	protected static final RawAnimation FLOP_ANIM = RawAnimation.begin().thenLoop("flopping");
	protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("attack");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public ArapaimaEntity(EntityType<? extends SchoolingFishEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Override
	public ItemStack getBucketItem()
	{
		return new ItemStack(FaunusItems.ARAPAIMA_BUCKET);
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return SoundEvents.ENTITY_CHICKEN_HURT; // again, why not?
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
	
	protected <E extends ArapaimaEntity> PlayState idleAnimController(final AnimationState<E> state)
	{
		if(!isSubmergedInWater())
		{
			state.setAndContinue(FLOP_ANIM);
		}
		else if(state.isMoving())
		{
			state.setAndContinue(SWIM_ANIM);
		}
		else
		{
			state.setAndContinue(IDLE_ANIM);
		}

		return PlayState.CONTINUE;
	}
}
