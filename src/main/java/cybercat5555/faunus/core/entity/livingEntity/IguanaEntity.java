package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.ItemRegistry;
import cybercat5555.faunus.core.entity.FeedableEntity;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IguanaEntity extends AnimalEntity implements GeoEntity, FeedableEntity {
    private static final int GROW_TAIL_TIME = 24000;
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swimming");
    protected static final RawAnimation HEADBOB_ANIM = RawAnimation.begin().thenLoop("headbob");
    private boolean isHeadBobbing = false;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean hasBeenFed = false;
    private boolean hasTail = true;
    private int timeUntilGrowTail = 0;

    public IguanaEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 85, 10, 1F, 1F, true);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new IguanaFleeGoal(this, 1.5d));
        goalSelector.add(2, new FollowParentGoal(this, 1.25d));
        goalSelector.add(2, new WanderAroundGoal(this, 1.0));
        goalSelector.add(3, new SwimAroundGoal(this, 1.0f, 120));
        goalSelector.add(3, new SwimGoal(this));
        goalSelector.add(3, new LookAroundGoal(this));
        goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 3.0f));

        targetSelector.add(1, new AnimalMateGoal(this, 1.0));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0);
    }

    @Override
    public void tick() {
        growTail();

        super.tick();
    }

    private void growTail() {
        if (!hasTail) {
            timeUntilGrowTail++;

            if (timeUntilGrowTail >= GROW_TAIL_TIME) {
                hasTail = true;
                timeUntilGrowTail = 0;
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack handItem = player.getStackInHand(hand);
        feedEntity(player, handItem);
        shorn(player);

        return super.interactMob(player, hand);
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        boolean dropTailChance = Math.random() < 0.05;

        if (dropTailChance) {
            dropTail();
        }

        super.onDamaged(damageSource);
    }

    private void dropTail() {
        dropItem(ItemRegistry.IGUANA_RAW_TAIL);

        hasTail = false;
        timeUntilGrowTail = 0;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
        controllers.add(new AnimationController<>(this, "headbob", 5, this::headBobAnimController));
    }

    private <T extends GeoAnimatable> PlayState headBobAnimController(AnimationState<T> event) {
        if (event.getController().hasAnimationFinished()) {
            isHeadBobbing = false;
        }

        if (!isHeadBobbing) {
            float random = (float) Math.random();

            if (random < 0.1f) {
                event.setAnimation(HEADBOB_ANIM);
                isHeadBobbing = true;
            }
        }

        return isHeadBobbing ? PlayState.CONTINUE : PlayState.STOP;
    }

    private <T extends GeoAnimatable> PlayState idleAnimController(AnimationState<T> event) {
        boolean isMoving = event.isMoving();
        boolean isSwimming = !isSwimming();

        if (!isMoving) {
            event.setAnimation(IDLE_ANIM);
        } else {
            event.setAnimation(isSwimming ? WALK_ANIM : SWIM_ANIM);
        }

        return PlayState.CONTINUE;
    }


    public static boolean canSpawn(EntityType<IguanaEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return true;
    }

    @Override
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

    private void shorn(PlayerEntity player) {
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.SHEARS && hasTail) {
            dropTail();
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

    public boolean hasTail() {
        return hasTail;
    }

    @Override
    public TagKey<Item> getBreedingItemsTag() {
        return TagKey.of(RegistryKeys.ITEM, FaunusID.content("crayfish_breeding_items"));
    }


    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public static class IguanaFleeGoal extends EscapeDangerGoal {
        public IguanaFleeGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        public void start() {
            float attackDistance = 2.0f;

            if (mob.distanceTo(mob.getLastAttacker()) < attackDistance) {
                mob.tryAttack(mob.getLastAttacker());
            }

            super.start();
        }
    }
}
