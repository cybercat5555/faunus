package cybercat5555.faunus.renderer.entity;

import cybercat5555.faunus.core.entity.livingEntity.SongbirdEntity;
import cybercat5555.faunus.core.entity.livingEntity.variant.BirdVariant;
import cybercat5555.faunus.renderer.layer.SongbirdLayer;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class SongbirdEntityRenderer extends GeoEntityRenderer<SongbirdEntity> {


    public SongbirdEntityRenderer(Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<SongbirdEntity>(FaunusID.content("songbird"), true)
                .withAltTexture(FaunusID.content("songbird/songbird_base")));

        this.addRenderLayer(new SongbirdLayer(this));
    }

    @Override
    public Color getRenderColor(SongbirdEntity songbird, float partialTick, int packedLight) {
        BirdVariant variant = songbird.getBirdVariant();
        float[] color = variant.getPrimaryColor();
        return Color.ofRGB(color[0], color[1], color[2]);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, SongbirdEntity songbird, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        var red = 0.0f;
        var green = 0.0f;
        var blue = 0.0f;

        if (bone.getName().equals("belly")) {
            float[] color = songbird.getBirdVariant().getBellyColor();
            red = color[0];
            green = color[1];
            blue = color[2];
        }

        super.renderRecursively(poseStack, songbird, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, Color.ofARGB(1,red,green,blue).argbInt());
    }
}