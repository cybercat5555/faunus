package cybercat5555.faunus.common;

import cybercat5555.faunus.common.network.PunchAirPacket;
import cybercat5555.faunus.common.network.UseAirPacket;
import cybercat5555.faunus.core.entity.livingEntity.CapuchinEntity;
import cybercat5555.faunus.core.entity.livingEntity.QuetzalEntity;
import cybercat5555.faunus.core.entity.livingEntity.YacareManEaterEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EventManager {

    public static void onAttackBlock() {
        AttackBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) -> {
            if (player.getVehicle() != null && player.getVehicle() instanceof YacareManEaterEntity && player.isAlive()) {
                player.attack(player.getVehicle());
            }

            return ActionResult.PASS;
        });
    }

    public static void onAttackEntity() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.getVehicle() != null && player.getVehicle() instanceof YacareManEaterEntity && player.isAlive()) {
                float damage = player.getMainHandStack().isEmpty() ? 2 : player.getMainHandStack().getDamage();
                entity.damage(player.getDamageSources().playerAttack(player), damage);
            }

            return ActionResult.PASS;
        });
    }

    private static int CAPUCHIN_BOX_RADIUS = 24;


    public static void onPlayerTick() {
        ServerTickEvents.END_SERVER_TICK.register(QuetzalEntity::globalTick);
    }

    public static void onAttackMiss() {
        PayloadTypeRegistry.playC2S().register(PunchAirPacket.ID, PunchAirPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PunchAirPacket.ID, (punchAirPacket, context) -> {
            var player = context.player();
            ServerWorld world = (ServerWorld) context.player().getWorld();
            var capuchins = world.getEntitiesByType(TypeFilter.equals(CapuchinEntity.class), player.getBoundingBox().expand(CAPUCHIN_BOX_RADIUS), capuchinEntity -> {
                return capuchinEntity.getOwner() != null && capuchinEntity.getOwner().getUuid().equals(player.getUuid());
            });
            HitResult blockHit = player.raycast(24.0, 0.0F, false); // maxDistance=24

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult bhr = (BlockHitResult) blockHit;
                BlockPos hitPos = bhr.getBlockPos();
                Vec3d hitVec = bhr.getPos();

                double distance = 2.0;
                Box box = new Box(
                        hitVec.x - distance, hitVec.y - distance, hitVec.z - distance,
                        hitVec.x + distance, hitVec.y + distance, hitVec.z + distance
                );

                var items = world.getEntitiesByType(TypeFilter.equals(ItemEntity.class), box, e -> true);
                items.forEach(item -> {
                    world.spawnParticles(
                            ParticleTypes.CLOUD,
                            item.getX(), item.getY() + 0.4, item.getZ(),
                            1, 0, 0, 0, 0
                    );
                });

                capuchins.forEach(c->c.setTargetItems(items));

                double step = 0.25;
                for (double x = box.minX; x <= box.maxX; x += step) {
                    for (double y = box.minY; y <= box.maxY; y += step) {
                        for (double z = box.minZ; z <= box.maxZ; z += step) {
                            int edges = 0;
                            if (x == box.minX || x == box.maxX) edges++;
                            if (y == box.minY || y == box.maxY) edges++;
                            if (z == box.minZ || z == box.maxZ) edges++;
                            if (edges >= 2) {
                                world.spawnParticles(
                                        ParticleTypes.FLAME,
                                        x, y, z,
                                        1, 0, 0, 0, 0
                                );
                            }
                        }
                    }
                }
            }
        });
    }

    public static void onUseMiss() {
        PayloadTypeRegistry.playC2S().register(UseAirPacket.ID, UseAirPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(UseAirPacket.ID, (punchAirPacket, context) -> {
            var instance = context.player();
            if (instance.shoulderEntityAddedTime + 20L < instance.getWorld().getTime()) {
                var leftnbt = instance.getShoulderEntityLeft();
                var left = EntityType.getEntityFromNbt(leftnbt, instance.getWorld()).orElse(null);
                if ((left instanceof QuetzalEntity)) {
                    instance.dropShoulderEntity(leftnbt);
                    instance.setShoulderEntityLeft(new NbtCompound());
                }
                var rightnbt = instance.getShoulderEntityLeft();
                var right = EntityType.getEntityFromNbt(rightnbt, instance.getWorld()).orElse(null);
                if ((right instanceof QuetzalEntity)) {

                    instance.dropShoulderEntity(instance.getShoulderEntityRight());
                    instance.setShoulderEntityRight(new NbtCompound());
                }
            }
        });
    }
}
