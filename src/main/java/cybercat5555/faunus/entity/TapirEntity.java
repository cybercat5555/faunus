package cybercat5555.faunus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TapirEntity extends AnimalEntity implements GeoEntity
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