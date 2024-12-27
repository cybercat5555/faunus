package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.SoundRegistry;
import cybercat5555.faunus.core.entity.livingEntity.variant.BirdPatterns;
import cybercat5555.faunus.core.entity.livingEntity.variant.BirdVariant;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SongbirdEntity extends ParrotEntity implements GeoEntity {
    private static final TrackedData<Integer> PATTERN = DataTracker.registerData(SongbirdEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(SongbirdEntity.class, TrackedDataHandlerRegistry.INTEGER);


    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("flight");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SongbirdEntity(EntityType<? extends SongbirdEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PATTERN, 0);
        this.dataTracker.startTracking(VARIANT, 0);
    }

    public static DefaultAttributeContainer.Builder createSongbirdAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }


    @Override
    protected void addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.075f, 1.0f);
    }


    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public SoundEvent getAmbientSound() {
        return SoundRegistry.TROPICAL_BIRD_AMBIANCE;
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundEvent = this.getAmbientSound();

        if (soundEvent != null && this.random.nextFloat() <= 0.2) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.TROPICAL_BIRD_HURT;
    }


    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
    }

    protected <E extends SongbirdEntity> PlayState idleAnimController(final AnimationState<E> event) {
        boolean isMoving = event.isMoving();
        boolean isFlying = isInAir();

        if(isMoving && isFlying) {
            event.setAnimation(FLY_ANIM);
        } else if(isMoving) {
            event.setAnimation(WALK_ANIM);
        } else {
            event.setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    public static boolean canSpawnSongbird(EntityType<SongbirdEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.PARROTS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity other) {
        return EntityRegistry.SONGBIRD.create(world);
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Pattern", getBirdPattern().getId());
        nbt.putInt("Variant", getBirdVariant().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(PATTERN, nbt.getInt("Pattern"));
        this.dataTracker.set(VARIANT, nbt.getInt("Variant"));
    }

    /* VARIANT */

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        world.getBiome(this.getBlockPos()).isIn(BiomeTags.IS_JUNGLE);

        RegistryEntry<Biome> biome = world.getBiome(this.getBlockPos());
        BirdVariant variant = BirdVariant.byBiome(biome);

        this.setVariant(variant);
        this.setPattern(variant.getPattern());

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }


    public void setVariant(BirdVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public void setPattern(BirdPatterns pattern) {
        this.dataTracker.set(PATTERN, pattern.getId());
    }

    public BirdPatterns getBirdPattern() {
        return BirdPatterns.byId(this.dataTracker.get(PATTERN) & 255);
    }

    public BirdVariant getBirdVariant() {
        return BirdVariant.byId(this.dataTracker.get(VARIANT) & 255);
    }
}
