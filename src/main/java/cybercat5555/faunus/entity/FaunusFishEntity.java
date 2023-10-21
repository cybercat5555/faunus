package cybercat5555.faunus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * This exists purely because of OOP bullshit:
 *     - The new swimaround goal is required
 *     - The goal requires `hasSelfControl()` which is protected
 *     - We provide that, and can use this class for some deduplication
 */
public abstract class FaunusFishEntity extends SchoolingFishEntity
{

	public FaunusFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public boolean getHasSelfControl()
	{
		return hasSelfControl();
	}

	@Override
	protected void initGoals()
	{
		goalSelector.add(2, new EscapeDangerGoal(this, 1.25));
		goalSelector.add(3, new FaunusSwimAroundGoal(this));
		goalSelector.add(5, new FollowGroupLeaderGoal(this));
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return null;
	}

	/*
	* This exists purely because of OOP bullshit (AS WELL):
	*    - Faunus fish should not run away
	*    - The Fish class registers this goal, so we can't call super in initGoals
	*    - The SwimToRandomPlaceGoal is a nested class in Fish, so we can't access it
	*    - We need to manually setup goals and use this one for moving around
	*/
	public static class FaunusSwimAroundGoal extends SwimAroundGoal
	{
		public final FaunusFishEntity fish;

		public FaunusSwimAroundGoal(FaunusFishEntity fish)
		{
			super(fish, 1.0, 65);
			this.fish = fish;
		}

		@Override
		public boolean canStart()
		{
			return fish.getHasSelfControl() && super.canStart();
		}
	}
}
