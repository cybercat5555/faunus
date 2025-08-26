package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.BlockRegistry;
import cybercat5555.faunus.core.ItemRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.core.entity.MateEntity;
import cybercat5555.faunus.core.entity.ai.goals.MateGoal;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.xml.crypto.Data;

public class ArapaimaEntity extends FishEntity implements GeoEntity, FeedableEntity, MateEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swimming");
    protected static final RawAnimation FLOP_ANIM = RawAnimation.begin().thenLoop("flopping");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("attack");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    private static final int MAX_LOVE_TICKS = 600;
    private boolean hasBeenFed;
    private int loveTicks;
    private static TrackedData<Boolean> HAS_EGG = DataTracker.registerData(ArapaimaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public ArapaimaEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new ArapaimaRamGoal(this, 10d));
        this.goalSelector.add(2, new ArapaimaMateGoal(this, 1.0D));
        this.goalSelector.add(3, new LayEggGoal(this, 1.0D));
        this.goalSelector.add(4, new SwimAroundGoal(this, 1.0D, 65));
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.5f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HAS_EGG, false);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);

        return super.interactMob(player, hand);
    }

    @Override
    public void feedEntity(PlayerEntity player, ItemStack stack) {
        if (canFedWithItem(stack)) {
            hasBeenFed = true;
            setInLove(MAX_LOVE_TICKS);

            if (!player.isCreative() && !player.isSpectator()) {
                stack.decrement(1);
            }
        }
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
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("arapaima_breeding_items"));
    }

    @Override
    protected void applyDamage(DamageSource source, float amount) {
        if (source.getSource() instanceof ArrowEntity) {
            amount *= 0.5F;
        }

        super.applyDamage(source, amount);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ItemRegistry.ARAPAIMA_BUCKET);
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

    protected <E extends ArapaimaEntity> PlayState idleAnimController(final AnimationState<E> state) {
        if (!isSubmergedInWater()) {
            state.setAndContinue(FLOP_ANIM);
        } else if (state.isMoving()) {
            state.setAndContinue(isAttacking() ? ATTACK_ANIM : SWIM_ANIM);
        } else {
            state.setAndContinue(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public boolean isInLove() {
        return this.loveTicks > 0;
    }

    @Override
    public void setInLove(int ticks) {
        this.loveTicks = Math.min(MAX_LOVE_TICKS, ticks);
    }

    @Override
    public void resetLoveTicks() {
        this.loveTicks = 0;
    }

    @Override
    public boolean canBreedWith(LivingEntity mate) {
        if (mate == this || !(mate instanceof MateEntity)) {
            return false;
        }

        return this.isInLove() && ((MateEntity) mate).isInLove();
    }

    @Override
    public void breed(ServerWorld world, LivingEntity other) {
        MobEntity passiveEntity = this.createChild(world, other);
        if (passiveEntity == null) {
            return;
        }

        passiveEntity.setBaby(true);
        passiveEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
        this.breed(world, other, passiveEntity);
        world.spawnEntityAndPassengers(passiveEntity);
    }

    @Override
    public void breed(ServerWorld world, LivingEntity other, LivingEntity baby) {
        this.resetLoveTicks();
        ((MateEntity) other).resetLoveTicks();
        world.sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(HAS_EGG, nbt.getBoolean("HasEgg"));
    }

    public boolean hasEgg() {
        return this.dataTracker.get(HAS_EGG);
    }

    void setHasEgg(boolean hasEgg) {
        this.dataTracker.set(HAS_EGG, hasEgg);
    }


    @Override
    public MobEntity createChild(ServerWorld world, LivingEntity other) {
        EntityType<?> entityType = this.getType();
        ((MateEntity) other).resetLoveTicks();

        return (MobEntity) entityType.create(world);
    }

    static class ArapaimaMateGoal
            extends MateGoal {
        private final ArapaimaEntity arapaima;

        ArapaimaMateGoal(ArapaimaEntity arapaima, double speed) {
            super(arapaima, speed);
            this.arapaima = arapaima;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.arapaima.hasEgg();
        }

        @Override
        protected void breed() {
            this.arapaima.setHasEgg(true);

            ((MateEntity) animal).resetLoveTicks();
            ((MateEntity) mate).resetLoveTicks();

            Random random = this.animal.getRandom();

            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }
        }
    }

    static class LayEggGoal
            extends MoveToTargetPosGoal {
        private final ArapaimaEntity arapaima;

        LayEggGoal(ArapaimaEntity arapaima, double speed) {
            super(arapaima, speed, 16);
            this.arapaima = arapaima;
        }

        @Override
        public boolean canStart() {
            return this.arapaima.hasEgg() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.arapaima.hasEgg();
        }

        @Override
        public void tick() {
            super.tick();

            BlockPos blockPos = this.arapaima.getBlockPos();
            if (this.arapaima.isTouchingWater()) {
                World world = this.arapaima.getWorld();
                world.playSound(null, blockPos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3f, 0.9f + world.random.nextFloat() * 0.2f);
                BlockPos blockPos2 = this.targetPos.up();
                BlockState blockState = BlockRegistry.ARAPAIMA_EGG.getDefaultState();

                world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Emitter.of(this.arapaima, blockState));

                this.arapaima.setHasEgg(false);
            }
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            return world.getBlockState(pos).isOf(Blocks.WATER);
        }
    }

    public static class ArapaimaRamGoal extends Goal {
        private final double speed;
        private final PathAwareEntity mob;
        private boolean mustFlee = false;


        public ArapaimaRamGoal(PathAwareEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
        }

        @Override
        public void tick() {
            if (mustFlee) {
                Vec3d vec3d = NoPenaltyTargeting.find(this.mob, 4, 1);

                if (vec3d != null && this.mob.getNavigation().isIdle()) {
                    this.mob.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
                    stop();
                }
            } else {
                this.mob.getNavigation().startMovingTo(this.mob.getLastAttacker(), this.speed);
                this.mob.setVelocity(this.mob.getVelocity().multiply(3));

                if (attack(this.mob.getLastAttacker())) {
                    mustFlee = true;
                }
            }

            super.tick();
        }

        protected boolean attack(LivingEntity target) {
//            double squaredDistance = this.mob.getSquaredDistanceToAttackPosOf(this.mob.getLastAttacker());
//            double distance = this.mob.getSquaredDistanceToAttackPosOf(target);
//
//            if (squaredDistance <= distance) {
                this.mob.tryAttack(target);
                return true;
//            }

//            return false;
        }

        @Override
        public void start() {
            if (Math.random() < 0.6F) {
                this.mob.setTarget(this.mob.getLastAttacker());
            } else {
                mustFlee = true;
            }

            super.start();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue();
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public boolean canStart() {
            return this.mob.getLastAttacker() != null;
        }
    }
}
