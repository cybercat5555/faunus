package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.SoundRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.core.entity.ai.goals.CapuchinBringItemGoal;
import cybercat5555.faunus.core.entity.ai.goals.CapuchinCollectItemGoal;
import cybercat5555.faunus.core.entity.projectile.CocoaBeanProjectile;
import cybercat5555.faunus.util.FaunusID;
import net.fabricmc.fabric.impl.tag.convention.v2.TagRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class CapuchinEntity extends TameableShoulderEntity implements GeoEntity, FeedableEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation HANGING_TAIL_IDLE_ANIM = RawAnimation.begin().thenLoop("hanging_tail_idle");
    protected static final RawAnimation SITTING_ANIM = RawAnimation.begin().thenLoop("sitting_idle");
    protected static final RawAnimation SITTING_SHOULDER_ANIM = RawAnimation.begin().thenLoop("sitting_idle_shoulder");
    protected static final RawAnimation SITTING_GROOMING_ANIM = RawAnimation.begin().thenLoop("sitting_grooming");
    protected static final RawAnimation WALKING_ANIM = RawAnimation.begin().thenLoop("walking");
    protected static final RawAnimation RUNNING_ANIM = RawAnimation.begin().thenLoop("running");
    protected static final RawAnimation ATTACK_LEFT_ANIM = RawAnimation.begin().thenLoop("attack_left");
    protected static final RawAnimation ATTACK_RIGHT_ANIM = RawAnimation.begin().thenLoop("attack_right");
    protected static final RawAnimation HANGING_HANDS_IDLE_ANIM = RawAnimation.begin().thenLoop("hanging_hands_idle");
    protected static final RawAnimation HANGING_MOVEMENT_ANIM = RawAnimation.begin().thenLoop("hanging_movement");
    protected static final RawAnimation SITTING_GROOMING_FLARE_ANIM = RawAnimation.begin().thenLoop("sitting_grooming_flare");
    private static final int MAX_LOVE_TICKS = 600;


    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean hasBeenFed;

    public CapuchinEntity(EntityType<? extends CapuchinEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(false);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1f);
    }

    private List<ItemEntity> targetItems = new ArrayList<>();
    public List<ItemEntity> getTargetItems() {
        return targetItems;
    }

    public void setTargetItems(List<ItemEntity> items) {
        this.targetItems = items;
    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new CapuchinCollectItemGoal(this));
        this.goalSelector.add(1, new CapuchinBringItemGoal(this));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new RunAwayCapuchinGoal(this, 1.5));
        this.goalSelector.add(4, new AttackCapuchinGoal(this, 1.5, false));
        this.goalSelector.add(5, new SitOnOwnerShoulderGoal(this));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 5.0f, 1.0f));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0){
            @Override
            public boolean canStart() {
                return super.canStart() && !((CapuchinEntity)this.mob).isSitting();
            }

            @Override
            public boolean shouldContinue() {
                return super.shouldContinue() && !((CapuchinEntity)this.mob).isSitting();
            }
        });
//        this.goalSelector.add(4, new HangTreeGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));

        this.targetSelector.add(1, new RevengeGoal(this));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementAnimController));
        controllers.add(new AnimationController<>(this, "attack", 0, this::attackAnimController));
    }

    protected <E extends CapuchinEntity> PlayState movementAnimController(final AnimationState<E> event) {
        boolean isMoving = event.isMoving();
        boolean isRunning = this.getVelocity().lengthSquared() > 0.02;
        boolean isSitting = this.isSitting();
        boolean isSittingInPlayerShoulder = this.getVehicle() instanceof PlayerEntity;
        boolean isNear2Capuchin = this.nearCapuchinCount(16) >= 2;
        boolean isHangingTree = isHangingTree(this.getPos().add(0, 1, 0)) && getWorld().getBlockState(getBlockPos().down()).isAir();
        boolean isAlreadyPlayingHangingAnim = event.getController().getCurrentRawAnimation() == HANGING_HANDS_IDLE_ANIM || event.getController().getCurrentRawAnimation() == HANGING_TAIL_IDLE_ANIM;

        if (isSitting) {
            if (isSittingInPlayerShoulder) {
                event.setAnimation(SITTING_SHOULDER_ANIM);
            } else if (isNear2Capuchin) {
                event.setAnimation(SITTING_GROOMING_FLARE_ANIM);
            } else if (isNearCapuchin()) {
                event.setAnimation(SITTING_GROOMING_ANIM);
            } else {
                event.setAnimation(SITTING_ANIM);
            }

        } else if (isMoving) {
            event.setAnimation(isHangingTree ? HANGING_MOVEMENT_ANIM : isRunning ? RUNNING_ANIM : WALKING_ANIM);
            event.setControllerSpeed(1.5f);

        } else {
            if (isAlreadyPlayingHangingAnim) {
                return PlayState.CONTINUE;
            }

            event.setAnimation(isHangingTree ? Math.random() < 0.5 ? HANGING_HANDS_IDLE_ANIM : HANGING_TAIL_IDLE_ANIM : IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    protected <E extends CapuchinEntity> PlayState attackAnimController(final AnimationState<E> event) {
        boolean isAlreadyPlayingAttackAnim = event.getController().getCurrentRawAnimation() == ATTACK_LEFT_ANIM || event.getController().getCurrentRawAnimation() == ATTACK_RIGHT_ANIM;
        boolean isAttacking = event.getLimbSwingAmount() > 0.001f;

        if (isAttacking) {
            if (isAlreadyPlayingAttackAnim) {
                return PlayState.CONTINUE;
            }

            event.setAnimation(Math.random() < 0.5 ? ATTACK_LEFT_ANIM : ATTACK_RIGHT_ANIM);
        }

        return PlayState.STOP;
    }

    public static boolean canSpawn(EntityType<CapuchinEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.PARROTS_SPAWNABLE_ON);
    }

    public boolean canSpawn(WorldView world) {
        if (world.doesNotIntersectEntities(this) && !world.containsFluid(this.getBoundingBox())) {
            BlockPos blockPos = this.getBlockPos();
            if (blockPos.getY() < world.getSeaLevel()) {
                return false;
            }

            BlockState blockState = world.getBlockState(blockPos.down());
            return  blockState.isIn(BlockTags.ANIMALS_SPAWNABLE_ON) ||
                    blockState.isIn(BlockTags.LEAVES) ||
                    blockState.isIn(BlockTags.LOGS);
        }

        return false;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public SoundEvent getAmbientSound() {
        boolean isAngry = this.getTarget() != null;
        boolean isTamed = this.isTamed();

        if (isAngry) {
            return SoundRegistry.CAPUCHIN_ANGRY;
        } else if (isTamed) {
            return SoundRegistry.CAPUCHIN_TAMED_IDLE;
        } else {
            return SoundRegistry.CAPUCHIN_IDLE;
        }
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundEvent = this.getAmbientSound();

        if (soundEvent != null && this.random.nextFloat() <= 0.1) {
            this.getWorld().playSoundFromEntity(null, this, soundEvent, this.getSoundCategory(), this.getSoundVolume(), this.getPitch());
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.CAPUCHIN_HURT;
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        super.playHurtSound(source);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return (int) (super.computeFallDamage(fallDistance, damageMultiplier) * 0.8f);
    }

    @Override
    public void tick() {
        super.tick();

        if (getVehicle() != null && getVehicle() instanceof PlayerEntity player && !player.isOnGround()) {
            stopRiding();
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.dropInventory();
    }

    @Override
    protected void dropInventory() {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.dropStack(itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }
    protected static final TrackedData<Boolean> SITTING = DataTracker.registerData(CapuchinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);

        if(player.getWorld().isClient()){
            if(this.getOwner() == player){
                return ActionResult.SUCCESS;
            }else {
                return ActionResult.FAIL;
            }
        }

        if (isOwner(player) && hand.equals(Hand.MAIN_HAND)) {
            if (player.isSneaking()) {
                startRiding(player, true);
            } else if (getVehicle() == null && (isOnGround() || isSitting())) {
                setSitting(!isSitting());
            }
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.setSitting(nbt.getBoolean("Sitting"));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt = super.writeNbt(nbt);
        nbt.putBoolean("Sitting", this.isSitting());
        return nbt;
    }

    @Override
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
    }

    @Override
    public void feedEntity(PlayerEntity player, ItemStack stack) {

        if(!(player.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        if (canFedWithItem(stack)) {
            hasBeenFed = true;
            setLoveTicks(MAX_LOVE_TICKS);

            if (!this.isTamed()) {
                double changeToTame = Math.random();
                if (changeToTame < 0.1) {
                    this.setTamed(true,true);
                    this.setOwner(player);
                    this.setPersistent();
                    serverWorld.spawnParticles(ParticleTypes.HEART, this.getX(), this.getY(), this.getZ(), 15, 0.5,0.5,0.5, 1);
                } else {
                    serverWorld.spawnParticles(ParticleTypes.ASH,  this.getX(), this.getY(), this.getZ(), 15, 0.5,0.5,0.5,1);
                }

                if (!player.isCreative() && !player.isSpectator()) {
                    stack.decrement(1);
                }
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
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("capuchin_breeding_items"));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(getBreedingItemsTag());
    }

    public boolean isNearCapuchin() {
        return nearCapuchinCount(16) > 0;
    }

    public int nearCapuchinCount(double distance) {
        ArrayList<CapuchinEntity> entities = (ArrayList<CapuchinEntity>) this.getWorld().getEntitiesByClass(
                CapuchinEntity.class,
                this.getBoundingBox().expand(distance),
                entity -> entity != this
        );

        return entities.size();
    }

    private boolean isHangingTree(Vec3d pos) {
        BlockPos blockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);

        return getWorld().getBlockState(blockPos).getBlock().getTranslationKey().contains("leave");
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityRegistry.CAPUCHIN.create(world);
    }


    static class AttackCapuchinGoal extends MeleeAttackGoal {

        public AttackCapuchinGoal(CapuchinEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        protected void attack(LivingEntity target) {
            if (this.mob.isInAttackRange(target)) {
                this.mob.tryAttack(target);
            } else if(isCooledDown()) {
                throwCocoaBean(target);
                resetCooldown();
            }
        }

        private void throwCocoaBean(LivingEntity target) {
            CocoaBeanProjectile projectile = new CocoaBeanProjectile(mob, 1);
            double dX = target.getX() - this.mob.getX();
            double dY = target.getBodyY(0.3333333333333333) - projectile.getY();
            double dZ = target.getZ() - this.mob.getZ();
            double distance = Math.sqrt(dX * dX + dZ * dZ) * 0.2D;

            projectile.setVelocity(dX, dY + distance, dZ, 1.6F, 2.0F);
            mob.getWorld().spawnEntity(projectile);
        }

        @Override
        public boolean canStart() {
            int nearCapuchin = ((CapuchinEntity) this.mob).nearCapuchinCount(16);
            boolean shouldAttack = ((nearCapuchin >= 3 && !((CapuchinEntity) this.mob).isTamed()) || this.mob.getLastAttacker() != null) && Math.random() < 0.25;

            if (this.mob.getLastAttacker() != null) {
                this.mob.setTarget(this.mob.getLastAttacker());
                this.mob.getNavigation().startMovingTo(this.mob.getLastAttacker(), 1.0);
            }


            if (shouldAttack && this.mob.getTarget() != null) {
                attack(this.mob.getTarget());
                this.mob.setAttacking(true);

                return !(this.mob.getTarget() instanceof PlayerEntity player) || !player.isCreative();
            } else {
                this.mob.setAttacking(false);
            }


            return false;
        }

        @Override
        public void stop() {
            this.mob.setAttacking(false);
            super.stop();
        }

        @Override
        public boolean shouldContinue() {
            if(this.mob.getTarget() instanceof PlayerEntity player && !player.isCreative()) return false;

            return super.shouldContinue();
        }
    }

    static class RunAwayCapuchinGoal extends EscapeDangerGoal {

        public RunAwayCapuchinGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        protected boolean isInDanger() {
            int nearCapuchin = ((CapuchinEntity) this.mob).nearCapuchinCount(8);
            return (nearCapuchin > 0 && nearCapuchin < 3 && !((CapuchinEntity) this.mob).isTamed());
        }
    }
}
