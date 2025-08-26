package cybercat5555.faunus.core.entity.ai.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class MeleeHungryGoal extends MeleeAttackGoal implements HungerMeter {
    public static final float MAX_HUNGER = 100;
    private float hunger = 0;

    public MeleeHungryGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Override
    public void tick() {
        increaseHunger(0.2f);

        if (this.mob.getTarget() != null) {
            this.mob.setAttacking(this.mob.distanceTo(this.mob.getTarget()) < 3f);
        }

        super.tick();
    }

    @Override
    protected void attack(LivingEntity target) {
        if (isCooledDown()) {
            super.attack(target);

            if (!target.isAlive()) {
                increaseHunger(-(MAX_HUNGER / 3));
            }
        }
    }


    @Override
    public boolean canStart() {
        if (this.mob.getLastAttacker() != null) this.mob.setTarget(this.mob.getLastAttacker());

        if (!doesHaveHunger() || this.mob.getTarget() == null) {
            increaseHunger(0.2f);
            return false;
        }


        return super.canStart();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean shouldContinue() {
        return doesHaveHunger() && super.shouldContinue();
    }

    @Override
    public void increaseHunger(float hunger) {
        this.hunger = Math.max(0, Math.min(MAX_HUNGER, this.hunger + hunger));
    }

    @Override
    public boolean doesHaveHunger() {
        return hunger > (MAX_HUNGER / 2);
    }
}
