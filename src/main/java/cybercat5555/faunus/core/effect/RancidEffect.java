package cybercat5555.faunus.core.effect;

import cybercat5555.faunus.util.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class RancidEffect extends StatusEffect {
    private final List<MobEntity> runningEntities = new ArrayList<>();

    public RancidEffect() {
        super(StatusEffectCategory.HARMFUL, 0x4A4A4A);
    }


    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        amplifier = Math.max(1, amplifier);

        loadAffectedEntities(entity, 24.0D, amplifier);
        removeAffectedEntities(entity, 24.0D, amplifier);
        applyRunAwayEffect(entity);

        super.applyUpdateEffect(entity, amplifier);
        return true;
    }


    /**
     * Makes the entities around the affected entity run away from the affected entity.
     * @param effectEntity The entity that has the effect.
     */
    private void applyRunAwayEffect(LivingEntity effectEntity) {
        for (MobEntity mob : runningEntities) {
            Vec3d runAwayPos = MCUtil.getRunAwayPos(mob, effectEntity, 8.0D);

            mob.getNavigation().startMovingTo(runAwayPos.getX(), runAwayPos.getY(), runAwayPos.getZ(), 1.0D);
            mob.setTarget(null);
        }
    }


    /**
     * Load the entities that are affected by the effect.
     *
     * @param effectEntity The entity that has the effect.
     * @param radius       The radius of the effect.
     * @param amplifier    The amplifier of the radius.
     */
    private void loadAffectedEntities(LivingEntity effectEntity, double radius, float amplifier) {
        double distance = radius * Math.max(1, amplifier * 0.25D);

        runningEntities.addAll(effectEntity.getWorld().getEntitiesByClass(MobEntity.class, effectEntity.getBoundingBox().expand(distance), this::predicate));
    }

    private void removeAffectedEntities(LivingEntity effectEntity, double radius, float amplifier) {
        double distance = radius * Math.max(1, amplifier * 0.25D);

        for(MobEntity mob : runningEntities) {
            if(!effectEntity.getBoundingBox().expand(distance).intersects(mob.getBoundingBox())) {
                removeEntity(mob);
            }
        }
    }

    private void removeEntity(MobEntity mob) {
        mob.setTarget(null);
        mob.getNavigation().stop();
        runningEntities.remove(mob);
    }

    private boolean predicate(MobEntity nearbyEntity) {
        return nearbyEntity != null && !runningEntities.contains(nearbyEntity) && !MCUtil.isBossEntity(nearbyEntity.getClass()) && !MCUtil.isUndeadEntity(nearbyEntity.getClass());
    }

    @Override
    public void onRemoved(AttributeContainer attributes) {
        for (Entity nearbyEntity : runningEntities) {
            if (nearbyEntity instanceof MobEntity mob) {
                mob.setTarget(null);
                mob.getNavigation().stop();
            }
        }

        runningEntities.clear();
        super.onRemoved(attributes);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
