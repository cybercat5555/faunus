package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.BlockRegistry;
import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.SoundRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.core.entity.ai.goals.MeleeHungryGoal;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class YacareEntity extends TurtleEntity implements GeoEntity, FeedableEntity {
    private static final TrackedData<Boolean> HAS_EGG = DataTracker.registerData(YacareEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final RawAnimation IDLE_LAND_ANIM = RawAnimation.begin().thenLoop("idle_land");
    protected static final RawAnimation IDLE_WATER_ANIM = RawAnimation.begin().thenLoop("idle_water");
    protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swim");
    protected static final RawAnimation RUSH_WATER_ANIM = RawAnimation.begin().thenLoop("rush_water");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation RUSH_LAND_ANIM = RawAnimation.begin().thenLoop("rush_land");
    protected static final RawAnimation DEATH_ROLL_ANIM = RawAnimation.begin().thenLoop("death_roll").thenLoop("idle_land");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("attack").thenLoop("idle_water");
    private boolean hasBeenFed;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public YacareEntity(EntityType<? extends TurtleEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity other) {
        return EntityRegistry.YACARE.create(world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new YacareEntity.MateGoal(this, 1.0));
        this.goalSelector.add(0, new YacareEntity.LayEggGoal(this, 1.0));
        this.goalSelector.add(1, new MeleeHungryGoal(this, isSubmergedInWater() ? 3 : 1.35f, false));
        this.goalSelector.add(2, new TravelGoal(this, 0.2));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, true,
                target ->
                        (target instanceof WaterCreatureEntity && target.getBoundingBox().getAverageSideLength() < getBoundingBox().getAverageSideLength())
                                || target instanceof IguanaEntity || target instanceof CrayfishEntity));

    }

    public static boolean canSpawnYacare(EntityType<YacareEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() < world.getSeaLevel() + 4 && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HAS_EGG, false);
    }

    @Override
    public void tick() {
        setAir(getMaxAir());
        super.tick();
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        if (other instanceof PlayerEntity) {
            this.turnIntoManEater();
        }

        return super.onKilledOther(world, other);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);

        return super.interactMob(player, hand);
    }


    public void turnIntoManEater() {
        YacareManEaterEntity manEater = EntityRegistry.YACARE_MANEATER.create(this.getWorld());

        if (manEater != null) {
            manEater.updatePositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
            this.getWorld().spawnEntity(manEater);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
        controllers.add(new AnimationController<>(this, "attack", 5, this::attackAnimController));
    }

    private PlayState attackAnimController(AnimationState<YacareEntity> event) {
        boolean isAttacking = isAttacking();

        if (isAttacking) {
            event.setAndContinue(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    protected <E extends YacareEntity> PlayState idleAnimController(final AnimationState<E> event) {
        boolean hasTarget = getTarget() != null;
        boolean isMoving = getVelocity().lengthSquared() > 0.006f;

        if (touchingWater) {
            event.setAndContinue(isMoving && hasTarget ? RUSH_WATER_ANIM : isMoving ? SWIM_ANIM : IDLE_WATER_ANIM);
        } else {
            event.setAndContinue(isMoving && hasTarget ? RUSH_LAND_ANIM : isMoving ? WALK_ANIM : IDLE_LAND_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public SoundEvent getAmbientSound() {
        boolean isBaby = isBaby();

        if(isBaby){
            return SoundRegistry.YACARE_BABY;
        } else {
            return SoundRegistry.YACARE_IDLE;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.YACARE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.YACARE_HURT;
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

    @Override
    public boolean canFedWithItem(ItemStack stack) {
        return stack.isIn(getBreedingItemsTag());
    }

    @Override
    public boolean hasBeenFed() {
        return hasBeenFed;
    }


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(getBreedingItemsTag());
    }

    @Override
    public TagKey<Item> getBreedingItemsTag() {
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("yacare_breeding_items"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHasEgg(nbt.getBoolean("HasEgg"));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
    }

    public boolean hasEgg() {
        return this.dataTracker.get(HAS_EGG);
    }

    void setHasEgg(boolean hasEgg) {
        this.dataTracker.set(HAS_EGG, hasEgg);
    }

    static class MateGoal
            extends AnimalMateGoal {
        private final YacareEntity yacare;

        MateGoal(YacareEntity yacare, double speed) {
            super(yacare, speed);
            this.yacare = yacare;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.yacare.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
            if (serverPlayerEntity == null && this.mate != null && this.mate.getLovingPlayer() != null) {
                serverPlayerEntity = this.mate.getLovingPlayer();
            }

            if (serverPlayerEntity != null) {
                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.mate, null);
            }

            this.yacare.setHasEgg(true);
            this.animal.setBreedingAge(6000);
            this.mate.setBreedingAge(6000);

            this.animal.resetLoveTicks();
            this.mate.resetLoveTicks();

            Random random = this.animal.getRandom();

            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }
        }
    }

    static class LayEggGoal
            extends MoveToTargetPosGoal {
        private final YacareEntity yacare;

        LayEggGoal(YacareEntity yacare, double speed) {
            super(yacare, speed, 16);
            this.yacare = yacare;
        }

        @Override
        public boolean canStart() {
            return this.yacare.hasEgg() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.yacare.hasEgg();
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos blockPos = this.yacare.getBlockPos();
            if (!this.yacare.isTouchingWater()) {
                World world = this.yacare.getWorld();
                world.playSound(null, blockPos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3f, 0.9f + world.random.nextFloat() * 0.2f);
                BlockPos blockPos2 = this.targetPos.up();
                BlockState blockState = BlockRegistry.YACARE_EGG.getDefaultState();

                world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Emitter.of(this.yacare, blockState));

                this.yacare.setHasEgg(false);
                this.yacare.setLoveTicks(600);
            }
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            if (!world.isAir(pos.up())) {
                return false;
            }

            return !world.getBlockState(pos).isOf(Blocks.WATER);
        }
    }

}
