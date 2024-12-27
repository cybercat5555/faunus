package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.ItemRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.core.entity.ai.goals.HungerMeter;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeechEntity extends PathAwareEntity implements GeoEntity, FeedableEntity {
    private static final EntityType<?>[] BLACKLIST = new EntityType<?>[]{
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.WITHER_SKELETON
    };

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation CRAWLING = RawAnimation.begin().thenLoop("crawling");
    protected static final RawAnimation SWIMMING = RawAnimation.begin().thenLoop("swimming");
    protected static final RawAnimation SUCKING = RawAnimation.begin().thenLoop("sucking");
    protected static final RawAnimation FULL = RawAnimation.begin().thenLoop("full");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean hasBeenFed = false;

    public LeechEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new AttachToEntityGoal(this, 1.0D, false));
        this.goalSelector.add(1, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(2, new SwimAroundGoal(this, 1.0D, 65));
        this.goalSelector.add(3, new LookAroundGoal(this));

        this.targetSelector.add(0, new RevengeGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, true,
                target -> this.getBoundingBox().getAverageSideLength() < target.getBoundingBox().getAverageSideLength()
                        && target.getHealth() > target.getMaxHealth() / 2 &&
                        !isInBlackList(target)));
    }

    private boolean isInBlackList(LivingEntity target) {
        for (EntityType<?> entityType : BLACKLIST) {
            if (target.getType() == entityType) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.2f);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);
        captureOnBottle(player, handItem);

        return super.interactMob(player, hand);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
    }

    public <T extends GeoAnimatable> PlayState idleAnimController(AnimationState<T> event) {
        boolean isMoving = event.isMoving();
        boolean isMovingInWater = isMoving && this.isTouchingWater();
        boolean isSucking = this.getVehicle() != null;
        boolean isFull = getTarget() == null && !isMoving;

        if (isMoving) {
            event.setAnimation(isMovingInWater ? SWIMMING : CRAWLING);
        } else if (isSucking) {
            event.setAnimation(SUCKING);
        } else if (isFull) {
            event.setAnimation(FULL);
        } else {
            event.setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    public static boolean canSpawn(EntityType<LeechEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        return pos.getY() >= world.getSeaLevel() - 13;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() || hasBeenFed;
    }

    @Override
    public void feedEntity(PlayerEntity player, ItemStack stack) {
        if (canFedWithItem(stack)) {
            hasBeenFed = true;

            if (!player.isCreative() && !player.isSpectator()) {
                stack.decrement(1);
            }
        }
    }

    private void captureOnBottle(PlayerEntity player, ItemStack handItem) {
        boolean itemIsBottle = handItem.getItem() == Items.GLASS_BOTTLE;

        if (itemIsBottle) {
            if (!player.isCreative() && !player.isSpectator()) {
                handItem.decrement(1);
            }

            player.giveItemStack(ItemRegistry.BOTTLED_LEECH.getDefaultStack());
            this.getWorld().playSound(player, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // We don't want to play step sounds
    }

    @Override
    public boolean canFedWithItem(ItemStack stack) {
        return stack.isIn(getBreedingItemsTag());
    }

    @Override
    public boolean hasBeenFed() {
        return hasBeenFed;
    }

    @Override
    public TagKey<Item> getBreedingItemsTag() {
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("leech_breeding_items"));
    }

    public static class AttachToEntityGoal extends MeleeAttackGoal implements HungerMeter {
        protected static final int MAX_TIME_TO_LEAVE = 400;
        protected static final int MAX_HUNGER = 800;
        protected final double speed;
        private float hunger = MAX_HUNGER;
        private int timeToLeave = 0;
        private int timeToAttackAgain = 0;
        private LivingEntity target;

        public AttachToEntityGoal(LeechEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.speed = speed;

        }


        @Override
        public void tick() {
            if (target != null) {
                boolean isCloseEnough = this.mob.squaredDistanceTo(target) < 2D;

                if (isCloseEnough) {
                    this.rideEntity();
                    this.suckBlood();
                }
            }
        }

        private void rideEntity() {
            mob.startRiding(target, true);

            if (target instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(player));
            }
        }

        @SuppressWarnings("ConstantConditions")
        private void suckBlood() {
            int attackSpeed = (int) (mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) * 40.0);
            boolean targetIsHalfHealth = mob.getTarget().getHealth() < target.getMaxHealth() / 2;

            if (timeToAttackAgain++ > attackSpeed && !targetIsHalfHealth) {
                mob.tryAttack(mob.getTarget());
                timeToAttackAgain = 0;
            }
        }

        @Override
        public boolean shouldContinue() {
            return mob.getTarget() != null && doesHaveHunger() && !enoughTimePassedSuckingBlood();
        }

        @Override
        public void stop() {
            this.mob.setTarget(null);
            this.mob.stopRiding();
            this.increaseHunger(-MAX_HUNGER);

            super.stop();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingTo(target, this.speed);
            this.timeToLeave = 0;

            super.start();
        }

        @Override
        public boolean canStart() {
            this.increaseHunger(1);

            if (this.mob.getTarget() != null) {
                boolean isBiggerThanMob = this.mob.getWidth() < this.mob.getTarget().getWidth();
                boolean hasEnoughHealth = this.mob.getTarget().getHealth() > this.mob.getTarget().getMaxHealth() / 2;
                this.target = this.mob.getTarget();

                return super.canStart() && doesHaveHunger() && isBiggerThanMob && hasEnoughHealth;
            }

            return false;
        }

        @Override
        public void increaseHunger(float hunger) {
            this.hunger = Math.max(0, Math.min(MAX_HUNGER, this.hunger + hunger));
        }

        @Override
        @SuppressWarnings("all")
        public boolean doesHaveHunger() {
            return hunger > (MAX_HUNGER / 2);
        }

        public boolean enoughTimePassedSuckingBlood() {
            return timeToLeave++ > MAX_TIME_TO_LEAVE;
        }
    }
}
