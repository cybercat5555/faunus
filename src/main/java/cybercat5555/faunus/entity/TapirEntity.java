package cybercat5555.faunus.entity;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FleeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TapirEntity extends AnimalEntity implements GeoEntity, SmartBrainOwner<TapirEntity>
{
	protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public TapirEntity(EntityType<? extends TapirEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Override
	public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2)
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createChild'");
	}

	@Override
	public final void initGoals() {}

	@Override
	protected Brain.Profile<?> createBrainProfile()
	{
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<? extends ExtendedSensor<? extends TapirEntity>> getSensors()
	{
		return ObjectArrayList.of
		(
			new NearbyLivingEntitySensor<>(),
			new HurtBySensor<>()
		);
	}

	@Override
	public BrainActivityGroup<TapirEntity> getCoreTasks()
	{
		return BrainActivityGroup.coreTasks
		(
			new LookAtTarget<>(),
			new MoveToWalkTarget<>()
		);
	}

	@Override
	public BrainActivityGroup<TapirEntity> getIdleTasks()
	{
		return BrainActivityGroup.idleTasks
		(
			new FirstApplicableBehaviour<TapirEntity>
			(
				new SetPlayerLookTarget<>(),
				new SetRandomLookTarget<>()
			),
			new OneRandomBehaviour<TapirEntity>
			(
				new SetRandomWalkTarget<>(),
				new Idle<>().runFor(entity -> entity.getRandom().nextBetween(30, 60))
			)
		);
	}

	@Override
	public BrainActivityGroup<TapirEntity> getFightTasks()
	{
		return BrainActivityGroup.fightTasks
		(
			new InvalidateAttackTarget<>(),
			new FleeTarget<>()
		);
	}

	@Override
	public void mobTick()
	{
		super.mobTick();
		tickBrain(this);
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		// TODO Auto-generated method stub
		return super.interactMob(player, hand);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return this.geoCache;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers)
	{
		//TODO: add proper args here for animations
		controllers.add(new AnimationController<>(this, "idle", 5, this::idleAnimController));
	}

	protected <E extends TapirEntity> PlayState idleAnimController(final AnimationState<E> event)
	{
		return PlayState.CONTINUE;
	}
	
}