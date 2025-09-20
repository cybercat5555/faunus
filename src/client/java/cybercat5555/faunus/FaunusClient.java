package cybercat5555.faunus;

import cybercat5555.faunus.core.BlockRegistry;
import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.renderer.EntityRendererRegistry;
import cybercat5555.faunus.renderer.ShoulderQuetzalFeatureRenderer;
import cybercat5555.faunus.renderer.entity.QuetzalEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class FaunusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EntityRendererRegistry.init();

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ARAPAIMA_EGG, RenderLayer.getCutout());
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::onEntityRender);
    }

    private void onEntityRender(EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> livingEntityRenderer, LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper registrationHelper, EntityRendererFactory.Context context) {
        if (entityType.equals(EntityType.PLAYER)) {
            registrationHelper.register(new ShoulderQuetzalFeatureRenderer<>(livingEntityRenderer, context));
        }
    }
}