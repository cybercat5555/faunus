package cybercat5555.faunus.core.entity.ai.goals;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public class TerritorialSelectorGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private static final int ATTACK_COOLDOWN = 20 * 5;
    private final Predicate<Entity> predicate;
    private final boolean followOutWater;
    private BlockPos startBlockPos;
    private int timeToAttack;

    public TerritorialSelectorGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, boolean followOutWater, Predicate<Entity> targetPredicate) {
        super(mob, targetClass, checkVisibility);
        this.predicate = targetPredicate;
        this.followOutWater = followOutWater;
    }

    @Override
    public void tick() {
        if (target == null) targetEntity();

        super.tick();
    }

    public void targetEntity() {
        List<Entity> entities = mob.getWorld().getOtherEntities(mob, mob.getBoundingBox().expand(16.0D, 8.0D, 16.0D), predicate).stream().filter(entity -> entity instanceof LivingEntity).toList();
        if (!entities.isEmpty()) {
            target = (LivingEntity) entities.get(0);
            startBlockPos = mob.getBlockPos();
            timeToAttack = 0;
        }
    }

    @Override
    public boolean canStart() {
        if (target != null) return true;

        return !mob.getWorld().getOtherEntities(mob, mob.getBoundingBox().expand(16.0D, 8.0D, 16.0D), predicate).isEmpty();
    }

    @Override
    public boolean shouldContinue() {
        if (mob.isTouchingWater() || followOutWater) {
            timeToAttack = 0;
        } else if (mob.isTouchingWater()) {
            timeToAttack++;

            return timeToAttack < ATTACK_COOLDOWN;
        }

        return super.shouldContinue();
    }

    @Override
    public void stop() {
        if(startBlockPos != null)
            mob.getNavigation().startMovingTo(startBlockPos.getX(), startBlockPos.getY(), startBlockPos.getZ(), 1.0D);
    }
}