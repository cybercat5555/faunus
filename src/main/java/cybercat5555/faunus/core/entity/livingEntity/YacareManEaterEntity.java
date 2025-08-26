package cybercat5555.faunus.core.entity.livingEntity;

import cybercat5555.faunus.core.entity.BiteGrabEntity;
import cybercat5555.faunus.core.entity.ai.goals.BiteGrabGoal;
import cybercat5555.faunus.core.entity.ai.goals.TerritorialSelectorGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
public class YacareManEaterEntity extends YacareEntity implements GeoEntity, BiteGrabEntity {
    protected boolean isGrabbing;
    protected boolean isPerformingDeathRoll;

    public YacareManEaterEntity(EntityType<? extends TurtleEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        goalSelector.add(1, new BiteGrabGoal(this));
        goalSelector.add(2, new LookAroundGoal(this));
        goalSelector.add(3, new WanderAroundGoal(this, 0.7D));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new TerritorialSelectorGoal<>(this, LivingEntity.class, true, false,
                target -> !(target instanceof YacareEntity) &&
                        (target instanceof AnimalEntity || target instanceof PlayerEntity || target instanceof VillagerEntity || target instanceof IllagerEntity)));
    }

    @Override
    protected <E extends YacareEntity> PlayState idleAnimController(AnimationState<E> event) {
        if (isPerformingDeathRoll || (event.isCurrentAnimation(DEATH_ROLL_ANIM))) {
            event.setAndContinue(DEATH_ROLL_ANIM);

            if(event.getController().hasAnimationFinished()){
                setPerformDeathRoll(false);
            }

            return PlayState.CONTINUE;
        }

        return super.idleAnimController(event);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        Vec3d vec3 = (new Vec3d(0, 0.5, 1.7)).rotateY(-bodyYaw * 0.017453292F);
        passenger.setPose(EntityPose.STANDING);

        positionUpdater.accept(passenger, this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
    }

    @Override
    public void setPerformDeathRoll(boolean performDeathRoll) {
        this.isPerformingDeathRoll = performDeathRoll;
    }

    @Override
    public void setGrabbing(boolean isGrabbing) {
        this.isGrabbing = isGrabbing;
    }
}
