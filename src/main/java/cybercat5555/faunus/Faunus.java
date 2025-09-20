package cybercat5555.faunus;

import cybercat5555.faunus.common.EventManager;
import cybercat5555.faunus.common.config.MobSpawningConfig;
import cybercat5555.faunus.common.config.SpawnHandler;
import cybercat5555.faunus.common.network.PunchAirPacket;
import cybercat5555.faunus.common.network.UseAirPacket;
import cybercat5555.faunus.core.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Faunus implements ModInitializer {
    public static final String MODID = "faunus";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        EntityRegistry.init();
        ItemRegistry.init();
        BlockRegistry.init();
        SoundRegistry.init();
        EffectStatusRegistry.init();
        PotionRegistry.init();
        configHandler();
        eventHandler();
        SpawnHandler.removeSpawn();
        SpawnHandler.addSpawn();
    }

    public void eventHandler() {
        EventManager.onAttackBlock();
        EventManager.onAttackEntity();
        EventManager.onAttackMiss();
        EventManager.onUseMiss();
        EventManager.onPlayerTick();
    }


    public void configHandler() {
        Path configPath = FabricLoader.getInstance().getConfigDir();
        MobSpawningConfig.init(configPath);
    }
}