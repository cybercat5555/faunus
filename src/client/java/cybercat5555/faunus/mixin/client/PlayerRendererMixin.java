package cybercat5555.faunus.mixin.client;

import cybercat5555.faunus.core.entity.livingEntity.YacareManEaterEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    @Shadow
    protected abstract void setModelPose(AbstractClientPlayerEntity player);

    public PlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (abstractClientPlayerEntity.getVehicle() != null && abstractClientPlayerEntity.getVehicle() instanceof YacareManEaterEntity) {
            renderInYacareMouth(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
            ci.cancel();
        }
    }

    private void renderInYacareMouth(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.setModelPose(abstractClientPlayerEntity);

        // Rotate body model based on vehicle look direction
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));

        matrixStack.translate(0.0D, -1.0D, 0.0D);
        super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }
}
