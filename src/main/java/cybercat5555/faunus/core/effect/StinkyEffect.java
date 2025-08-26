package cybercat5555.faunus.core.effect;

import cybercat5555.faunus.util.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;

import java.util.ArrayList;
import java.util.List;

public class StinkyEffect extends StatusEffect {
    private final List<Entity> attackingEntities = new ArrayList<>();

    public StinkyEffect() {
        super(StatusEffectCategory.HARMFUL, 0x4A4A4A);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        amplifier = Math.max(1, amplifier);

        loadAffectedEntities(entity, 24.0D, amplifier);
        targetEntity(entity);

        super.applyUpdateEffect(entity, amplifier);
        return true;
    }

    private void loadAffectedEntities(LivingEntity effectEntity, double radius, float amplifier) {
        double distance = radius * Math.min(1, amplifier * 0.25D);

        attackingEntities.addAll(effectEntity.getWorld().getEntitiesByClass(HostileEntity.class, effectEntity.getBoundingBox().expand(distance), this::predicate));
    }

    private boolean predicate(HostileEntity nearbyEntity) {
        return nearbyEntity != null && !attackingEntities.contains(nearbyEntity) && !MCUtil.isBossEntity(nearbyEntity.getClass()) && !MCUtil.isUndeadEntity(nearbyEntity.getClass());
    }


    private void targetEntity(LivingEntity entity) {
        for (Entity attackingEntity : attackingEntities) {
            ((HostileEntity) attackingEntity).setTarget(entity);
        }
    }

    @Override
    public void onRemoved(AttributeContainer attributes) {
        for (Entity attackingEntity : attackingEntities) {
            ((HostileEntity) attackingEntity).setTarget(null);
        }

        super.onRemoved(attributes);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
