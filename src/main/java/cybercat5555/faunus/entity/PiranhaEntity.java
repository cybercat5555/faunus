package cybercat5555.faunus.entity;

import java.util.function.Predicate;

import cybercat5555.faunus.FaunusItems;
import cybercat5555.faunus.entity.ai.goals.PiranhaAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PiranhaEntity extends FaunusFishEntity implements GeoEntity
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
	protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swimming");
	protected static final RawAnimation FLOP_ANIM = RawAnimation.begin().thenLoop("flopping");
	protected static final RawAnimation BITE_ANIM = RawAnimation.begin().thenLoop("bite");
	protected static final RawAnimation JUMP_ANIM = RawAnimation.begin().thenLoop("jump attack");
	
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public PiranhaEntity(EntityType<? extends SchoolingFishEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createMobAttributes()
	{
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 4f)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.65f)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1f)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1f)
			.add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f);
	}

	@Override
	public final void initGoals()
	{
		super.initGoals();
		// goalSelector.add(5, null);
		goalSelector.add(1, new PiranhaAttackGoal(this, 2.0d, true));
		targetSelector.add(1, new RevengeGoal(this, new Class[0]));
		targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, e -> e.isTouchingWater()));
		targetSelector.add(3, new ActiveTargetGoal<>(this, AbstractHorseEntity.class, false, e -> e.isTouchingWater() && !(e instanceof SkeletonHorseEntity)));
	}

	@Override
	public ItemStack getBucketItem()
	{
		return new ItemStack(FaunusItems.PIRANHA_BUCKET);
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return SoundEvents.ENTITY_COW_AMBIENT; // why not?
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
	
	protected <E extends PiranhaEntity> PlayState idleAnimController(final AnimationState<E> state)
	{
		if(!isTouchingWater())
		{
			state.setAndContinue(FLOP_ANIM);
		}
		else if(isAttacking())
		{
			state.setAndContinue(BITE_ANIM);
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

	@Override
	public boolean onKilledOther(ServerWorld world, LivingEntity other)
	{
		// getWorld().spawnEntity(Entity)
		if(other instanceof HorseEntity)
		{
			HorseEntity horse = (HorseEntity)other;
			SkeletonHorseEntity ent = EntityType.SKELETON_HORSE.create(world);
			ent.refreshPositionAndAngles(other.getX(), other.getY(), other.getZ(), other.getYaw(), other.getPitch());
			ent.initialize(world, other.getWorld().getLocalDifficulty(other.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
			ent.damage(world.getDamageSources().generic(), 0f);
			ent.setPersistent();
			if(horse.isTame())
			{
				ent.setTame(true);
				ent.setBreedingAge(horse.getBreedingAge());
			}
			else
			{
				ent.setBreedingAge(0);
			}
			
			if(horse.isSaddled())
			{
				ent.saddle(ent.getSoundCategory());
			}

			ent.setCustomName(horse.getCustomName());
			ent.setCustomNameVisible(ent.hasCustomName());
			world.spawnEntity(ent);
		}
		else if(other instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)other;
			// SkeletonEntity ent = EntityType.SKELETON.create(world, null, null, other.getBlockPos(), SpawnReason.MOB_SUMMONED, true, false);
			SkeletonEntity ent = EntityType.SKELETON.create(world);
			ent.initialize(world, other.getWorld().getLocalDifficulty(other.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
			ent.refreshPositionAndAngles(other.getX(), other.getY(), other.getZ(), other.getYaw(), other.getPitch());
			ent.setCustomName(player.getName());
			ent.setCustomNameVisible(ent.hasCustomName());
			ent.damage(world.getDamageSources().generic(), 0f);
			
			world.spawnEntity(ent);
		}

		return true;
	}
}
