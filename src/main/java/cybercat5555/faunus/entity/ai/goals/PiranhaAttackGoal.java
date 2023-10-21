package cybercat5555.faunus.entity.ai.goals;

import cybercat5555.faunus.entity.PiranhaEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class PiranhaAttackGoal extends MeleeAttackGoal
{
	private final PiranhaEntity piranha;

	public PiranhaAttackGoal(PiranhaEntity piranha, double speed, boolean pauseWhenMobIdle)
	{
		super((PathAwareEntity)piranha, speed, pauseWhenMobIdle);
		this.piranha = piranha;
	}

	@Override
	public void start()
	{
		super.start();
		piranha.setAttacking(true);
	}

	@Override
	public boolean canStart()
	{
		LivingEntity target = mob.getTarget();
		if(target != null)
		{
			return target.isTouchingWater() && super.canStart();
		}
		return false;
	}

	@Override
	public void stop()
	{
		super.stop();
		piranha.setAttacking(false);
	}

	@Override
	public boolean shouldContinue() 
	{
		LivingEntity target = mob.getTarget();
		if(target != null)
		{
			return target.isTouchingWater() && super.shouldContinue();
		}
		return false;
	}
}
