package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.ItemRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.core.entity.ai.goals.BuryOnChased;
import cybercat5555.faunus.core.entity.livingEntity.variant.CrayfishVariant;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrayfishEntity extends AnimalEntity implements GeoEntity, FeedableEntity {
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(CrayfishEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swim");


    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean hasBeenFed = false;
    private boolean isBuried = false;

    public CrayfishEntity(EntityType<? extends CrayfishEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 85, 10, 0.75F, 0.5F, true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new BuryOnChased(this, 2F));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);
        pickUp(player);

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

    protected <E extends CrayfishEntity> PlayState idleAnimController(final AnimationState<E> event) {
        boolean isInWater = touchingWater;
        boolean isWalking = !isInWater && event.isMoving();

        if (isInWater) {
            event.setAnimation(SWIM_ANIM);
        } else {
            event.setAnimation(isWalking ? WALK_ANIM : IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    public static boolean canSpawn(EntityType<CrayfishEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
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

    public void pickUp(PlayerEntity player) {
        if (!player.isSpectator() && player.isSneaking()) {
            CrayfishVariant variant = getVariant();
            Item crayfish = variant == CrayfishVariant.BLUE ? ItemRegistry.BLUE_CRAYFISH : ItemRegistry.CRAYFISH;

            if (!player.getInventory().insertStack(new ItemStack(crayfish))) {
                player.dropItem(new ItemStack(crayfish), false);
            }

            this.remove(RemovalReason.DISCARDED);
        }
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

    public void setHasBeenFed(boolean hasBeenFed) {
        this.hasBeenFed = hasBeenFed;
    }

    @Override
    public TagKey<Item> getBreedingItemsTag() {
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("crayfish_breeding_items"));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityRegistry.CRAYFISH.create(world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", getTypeVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getInt("Variant"));
    }

    public boolean isBuried() {
        return isBuried;
    }

    public void setBuried(boolean isBuried) {
        this.isBuried = isBuried;
    }

    /* VARIANT */

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        final float BLUE_CRAYFISH_CHANCE = 0.1F;
        setVariant(Math.random() < BLUE_CRAYFISH_CHANCE ? CrayfishVariant.BLUE : CrayfishVariant.DEFAULT);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public CrayfishVariant getVariant() {
        return CrayfishVariant.byId(getTypeVariant() & 255);
    }

    public void setVariant(CrayfishVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    private int getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }
}
