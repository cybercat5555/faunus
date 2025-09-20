package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.SoundRegistry;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;

public class QuetzalEntity extends ParrotEntity implements GeoEntity {
    public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation IDLE_LOOK_ANIM = RawAnimation.begin().thenPlayXTimes("idle_look_around", 3).thenLoop("idle");
    public static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation FLYING_ANIM = RawAnimation.begin().thenLoop("flight");
    public static final RawAnimation FLYING_UPRIGHT_ANIM = RawAnimation.begin().thenLoop("flight_upright");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public QuetzalEntity(EntityType<? extends QuetzalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createQuetzalAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return SoundRegistry.QUETZAL_IDLE;
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundEvent = this.getAmbientSound();

        if (soundEvent != null && this.random.nextFloat() <= 0.2) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 10, this::idleAnimController).triggerableAnim("idle", IDLE_ANIM));
    }

    public static boolean canSpawnQuetzal(EntityType<QuetzalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.PARROTS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.075f, 1.0f);
    }


    protected <E extends QuetzalEntity> PlayState idleAnimController(final AnimationState<E> state) {
        if (isTouchingWater()) {
            state.setAndContinue(FLYING_UPRIGHT_ANIM);
        } else if (!isOnGround()) {
            state.setAndContinue(getVelocity().getY() > 0.05f ? FLYING_ANIM : FLYING_UPRIGHT_ANIM);
        } else if (state.isMoving() && isOnGround()) {
            state.setAndContinue(WALK_ANIM);
        } else if (!state.isCurrentAnimation(IDLE_LOOK_ANIM)) {
            state.setAndContinue(IDLE_ANIM);
        }

        state.setControllerSpeed(state.isCurrentAnimation(FLYING_ANIM) || state.isCurrentAnimation(FLYING_UPRIGHT_ANIM) ? 1.5f : 1f);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }


    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity other) {
        return EntityRegistry.QUETZAL.create(world);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.isTamed() && itemStack.isIn(TagKey.of(RegistryKeys.ITEM,FaunusID.content("quetzal_taming_items"))) && player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            itemStack.decrementUnlessCreative(1, player);
            if (!this.isSilent()) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.getWorld().isClient) {
                if (this.random.nextInt(10) == 0) {
                    this.setOwner(player);
                    this.getWorld().sendEntityStatus(this, (byte)7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte)6);
                }
            }

            return ActionResult.success(this.getWorld().isClient);
        } else if (!itemStack.isIn(ItemTags.PARROT_POISONOUS_FOOD)) {
            if (!this.isInAir() && this.isTamed() && this.isOwner(player)) {
                if (!this.getWorld().isClient) {
                    this.setSitting(!this.isSitting());
                }

                return ActionResult.success(this.getWorld().isClient);
            } else {
                if(!itemStack.isIn(ItemTags.PARROT_FOOD)){
                    return super.interactMob(player, hand);
                }
                else {
                    return ActionResult.PASS;
                }
            }
        } else {
            itemStack.decrementUnlessCreative(1, player);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
            if (player.isCreative() || !this.isInvulnerable()) {
                this.damage(this.getDamageSources().playerAttack(player), Float.MAX_VALUE);
            }

            return ActionResult.success(this.getWorld().isClient);
        }
    }



    static int ticks = 0;
    public static void globalTick(MinecraftServer server) {
        if(ticks++ != 80){
            return;
        }
        ticks = 0;
        server.getPlayerManager().getPlayerList().stream().filter(player -> {
            if (
                    player.getShoulderEntityLeft().isEmpty() &&
                    player.getShoulderEntityRight().isEmpty()) {
                    return false;
            }
            else return true;
        }).forEach(player -> {
            var left = EntityType.getEntityFromNbt(player.getShoulderEntityLeft(),player.getWorld());
            var right = EntityType.getEntityFromNbt(player.getShoulderEntityRight(),player.getWorld());
            var entities = new ArrayList<Entity>();
            left.ifPresent(entities::add);
            right.ifPresent(entities::add);

            var quetzals = entities.stream().filter(entity -> entity.getType() == EntityRegistry.QUETZAL).count();
            if(quetzals > 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 320));
                if(player.getHealth() < 5){
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 4));
                }
            }
        });
    }

}

