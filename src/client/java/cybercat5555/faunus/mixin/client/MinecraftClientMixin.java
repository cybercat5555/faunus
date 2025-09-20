package cybercat5555.faunus.mixin.client;

import cybercat5555.faunus.common.network.PunchAirPacket;
import cybercat5555.faunus.common.network.UseAirPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public HitResult crosshairTarget;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "doAttack",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    private void onAttackMiss(CallbackInfoReturnable<Boolean> cir){
        ClientPlayNetworking.send(new PunchAirPacket());
    }

    @Inject(method = "handleInputEvents",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"))
    private void onAirUseMiss(CallbackInfo ci){
        if((this.crosshairTarget == null || this.crosshairTarget.getType() == HitResult.Type.MISS) && this.player.getMainHandStack() == ItemStack.EMPTY && this.player.getOffHandStack() == ItemStack.EMPTY){
            ClientPlayNetworking.send(new UseAirPacket());
        }
    }
}
