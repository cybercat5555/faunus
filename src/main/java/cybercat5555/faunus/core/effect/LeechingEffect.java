package cybercat5555.faunus.core.effect;

import cybercat5555.faunus.util.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Random;

public class LeechingEffect extends StatusEffect {
    private LivingEntity affectedEntity;

    public LeechingEffect() {
        super(StatusEffectCategory.HARMFUL, 0x4A4A4A);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        affectedEntity = entity;
        super.onApplied(entity, amplifier);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        Random random = new Random();
        amplifier = Math.max(1, amplifier);

        if (random.nextFloat() <= 0.002f) {
            int heartsToHeal = (int) (((float) amplifier) * random.nextInt((int) (entity.getMaxHealth() / 2)));
            entity.heal(heartsToHeal);
        }

        super.applyUpdateEffect(entity, amplifier);
        return true;
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return affectedEntity != null && !MCUtil.containsEffect(affectedEntity, StatusEffects.RESISTANCE.value()) && !MCUtil.containsEffect(affectedEntity, StatusEffects.REGENERATION.value());
    }

}
