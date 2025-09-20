package cybercat5555.faunus.mixin;

import cybercat5555.faunus.core.entity.livingEntity.QuetzalEntity;
import cybercat5555.faunus.core.entity.livingEntity.YacareManEaterEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void stopRiding() {
        if (this.getVehicle() != null && this.getVehicle() instanceof YacareManEaterEntity && this.isSneaking() && this.isAlive()) {
            return;
        }

        super.stopRiding();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropShoulderEntities()V"))
    private void dropButNotQuetzals(PlayerEntity instance) {
        if (instance.shoulderEntityAddedTime + 20L < instance.getWorld().getTime()) {
            var leftnbt = instance.getShoulderEntityLeft();
            var left = EntityType.getEntityFromNbt(leftnbt, instance.getWorld()).orElse(null);
            if (!(left instanceof QuetzalEntity)) {
                instance.dropShoulderEntity(leftnbt);
                instance.setShoulderEntityLeft(new NbtCompound());
            }
            var rightnbt = instance.getShoulderEntityLeft();
            var right = EntityType.getEntityFromNbt(rightnbt, instance.getWorld()).orElse(null);
            if (!(right instanceof QuetzalEntity)) {

                instance.dropShoulderEntity(instance.getShoulderEntityRight());
                instance.setShoulderEntityRight(new NbtCompound());
            }
        }
    }
}
