package cybercat5555.faunus.entity;

import cybercat5555.faunus.FaunusEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class QuetzalEntity extends ParrotEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
	protected static final RawAnimation IDLE_LOOK_ANIM = RawAnimation.begin().thenPlayXTimes("idle_look_around", 3).thenLoop("idle");
	protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
	protected static final RawAnimation FLYING_ANIM = RawAnimation.begin().thenLoop("flight");
	protected static final RawAnimation FLYING_UPRIGHT_ANIM = RawAnimation.begin().thenLoop("flight_upright");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	// private static final int DEFAULT_TRANSITION_LENGTH = 10;
	// private int randomInt = 0;

	public QuetzalEntity(EntityType<? extends QuetzalEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createQuetzalAttributes()
	{
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4f)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
	}

	// @Override
	// public void tick()
	// {
	// 	super.tick();
	// 	if(getWorld().isClient)
	// 	{
	// 		randomInt = random.nextInt(1024);
	// 	}
	// }

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return geoCache;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idle", 10, this::idleAnimController));
	}

	protected <E extends QuetzalEntity> PlayState idleAnimController(final AnimationState<E> state)
	{
		if(isTouchingWater())
		{
			state.setAndContinue(FLYING_UPRIGHT_ANIM);
			// state.getController().transitionLength(DEFAULT_TRANSITION_LENGTH * 2);
		}
		// else if(isDescending())
		// {
		// 	state.setAndContinue(FLYING_UPRIGHT_ANIM);
		// }
		else if(!isOnGround())
		{
			state.setAndContinue(getVelocity().getY() > 0.05f ? FLYING_ANIM : FLYING_UPRIGHT_ANIM);
			// state.setAndContinue(FLYING_ANIM);
		}
		else if(state.isMoving())
		{
			state.setAndContinue(WALK_ANIM);
			// state.getController().transitionLength(DEFAULT_TRANSITION_LENGTH);
		}
		else if(!state.isCurrentAnimation(IDLE_LOOK_ANIM))
		{
			state.setAndContinue(IDLE_ANIM);
			// state.getController().transitionLength(DEFAULT_TRANSITION_LENGTH / 2);
		}

		// if(state.isCurrentAnimation(IDLE_ANIM) && randomInt < 10)
		// {
		// 	state.setAndContinue(IDLE_LOOK_ANIM);
		// }

		return PlayState.CONTINUE;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity other)
	{
		return FaunusEntities.QUETZAL.create(world);
	}
}
