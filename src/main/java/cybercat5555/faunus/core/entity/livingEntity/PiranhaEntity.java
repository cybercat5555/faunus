package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.ItemRegistry;
import cybercat5555.faunus.core.entity.ai.goals.PiranhaAttackGoal;
import cybercat5555.faunus.core.entity.ai.goals.PiranhaJumpAttackGoal;
import net.minecraft.entity.*;
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
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PiranhaEntity extends FaunusFishEntity implements GeoEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swimming");
    protected static final RawAnimation FLOP_ANIM = RawAnimation.begin().thenLoop("flopping");
    protected static final RawAnimation BITE_ANIM = RawAnimation.begin().thenLoop("bite");
    protected static final RawAnimation JUMP_ANIM = RawAnimation.begin().thenLoop("jump attack");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PiranhaEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.65f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f);
    }

    @Override
    public final void initGoals() {
        super.initGoals();
        goalSelector.add(1, new PiranhaAttackGoal(this, 2.0d, true));
        goalSelector.add(2, new PiranhaJumpAttackGoal(this, 2.0d, false));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, Entity::isTouchingWater));
        targetSelector.add(3, new ActiveTargetGoal<>(this, AbstractHorseEntity.class, false, e -> e.isTouchingWater() && !(e instanceof SkeletonHorseEntity)));
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ItemRegistry.PIRANHA_BUCKET);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
    }

    protected PlayState idleAnimController(final AnimationState<PiranhaEntity> state) {
        if (!isTouchingWater()) {
            state.setAndContinue(isAttacking() ? JUMP_ANIM : FLOP_ANIM);
        } else if (isAttacking()) {
            state.setAndContinue(BITE_ANIM);
        } else if (state.isMoving()) {
            state.setAndContinue(SWIM_ANIM);
        } else {
            state.setAndContinue(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }


    /**
     * Replaces the killed entity with a skeleton horse or a skeleton entity depending on the entity type.
     * @param world The world where the entity is located
     * @param killedEntity The entity that was killed
     * @return True if the entity was replaced, false otherwise
     */
    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity killedEntity) {
        if (killedEntity instanceof HorseEntity horse) {
            world.spawnEntity(replaceHorseWithSkeletonHorse(horse, world));
        } else if (killedEntity instanceof PlayerEntity player) {
            world.spawnEntity(replacePlayerWithSkeleton(player, world));
        }

        return true;
    }

    /**
     * Creates a skeleton horse entity based on a killed horse with the same attributes in the same coords.
     * @param horse The horse entity to replace once killed
     * @param world The world where the horse entity is located
     * @return The new skeleton horse entity
     */
    private SkeletonHorseEntity replaceHorseWithSkeletonHorse(HorseEntity horse, ServerWorld world) {
        SkeletonHorseEntity skeletonHorse = EntityType.SKELETON_HORSE.create(world);

        if (skeletonHorse != null) {
            skeletonHorse.refreshPositionAndAngles(horse.getX(), horse.getY(), horse.getZ(), horse.getYaw(), horse.getPitch());
            skeletonHorse.initialize(world, horse.getWorld().getLocalDifficulty(horse.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
            skeletonHorse.damage(world.getDamageSources().generic(), 0f);
            skeletonHorse.setPersistent();
            skeletonHorse.setBreedingAge(horse.isTame() ? horse.getBreedingAge() : 0);
            skeletonHorse.setTame(horse.isTame());
            skeletonHorse.saddle(horse.isSaddled() ? new ItemStack(Items.SADDLE): ItemStack.EMPTY,skeletonHorse.getSoundCategory());
            skeletonHorse.setCustomName(horse.getCustomName());
            skeletonHorse.setCustomNameVisible(skeletonHorse.hasCustomName());
        }

        return skeletonHorse;
    }

    /**
     * Creates a skeleton entity with the same attributes that the player in the same coords.
     * @param player The player entity to replace once killed
     * @param world The world where the player entity is located
     * @return The new skeleton entity
     */
    private SkeletonEntity replacePlayerWithSkeleton(PlayerEntity player, ServerWorld world) {
        SkeletonEntity skeleton = EntityType.SKELETON.create(world);

        if (skeleton != null) {
            skeleton.initialize(world, player.getWorld().getLocalDifficulty(player.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
            skeleton.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            skeleton.setCustomName(player.getName());
            skeleton.setCustomNameVisible(skeleton.hasCustomName());
            skeleton.damage(world.getDamageSources().generic(), 0f);
        }

        return skeleton;
    }
}
