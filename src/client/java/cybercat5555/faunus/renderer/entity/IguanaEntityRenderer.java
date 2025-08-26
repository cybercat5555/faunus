package cybercat5555.faunus.renderer.entity;

import cybercat5555.faunus.core.entity.livingEntity.IguanaEntity;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class IguanaEntityRenderer extends GeoEntityRenderer<IguanaEntity> {
    public IguanaEntityRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(FaunusID.content("iguana"), true));
    }


    @Override
    public void renderRecursively(MatrixStack poseStack, IguanaEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        // Hide the tail if entity lost it
        if (bone.getName().equals("tail") && !animatable.hasTail()) {
            return;
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
    }
}
