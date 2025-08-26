package cybercat5555.faunus.renderer.layer;

import cybercat5555.faunus.core.entity.livingEntity.SongbirdEntity;
import cybercat5555.faunus.core.entity.livingEntity.variant.BirdPatterns;
import cybercat5555.faunus.core.entity.livingEntity.variant.BirdVariant;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class SongbirdLayer extends GeoRenderLayer<SongbirdEntity> {
    private static final Identifier BELLY_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_belly.png");
    private static final Identifier CAPED_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_caped.png");
    private static final Identifier COLLARED_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_collared.png");
    private static final Identifier COUNTERSHADED_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_countershaded.png");
    private static final Identifier HOODED_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_hooded.png");
    private static final Identifier MASKED_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_masked.png");
    private static final Identifier STRIPEWING_TEXTURE = FaunusID.content("textures/entity/songbird/songbird_stripewing.png");

    public SongbirdLayer(GeoRenderer<SongbirdEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, SongbirdEntity songbird, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        BirdVariant variant = songbird.getBirdVariant();
        BirdPatterns pattern = songbird.getBirdPattern();
        RenderLayer layer = switch (pattern) {
            case NONE -> null;
            case BELLY -> RenderLayer.getEntityTranslucent(BELLY_TEXTURE);
            case CAPED -> RenderLayer.getEntityTranslucent(CAPED_TEXTURE);
            case COLLARED -> RenderLayer.getEntityTranslucent(COLLARED_TEXTURE);
            case COUNTERSHADED -> RenderLayer.getEntityTranslucent(COUNTERSHADED_TEXTURE);
            case HOODED -> RenderLayer.getEntityTranslucent(HOODED_TEXTURE);
            case MASKED -> RenderLayer.getEntityTranslucent(MASKED_TEXTURE);
            case STRIPEWING -> RenderLayer.getEntityTranslucent(STRIPEWING_TEXTURE);
        };

        float[] color = variant.getSecondaryColor();

        if (layer != null) {
            getRenderer().reRender(
                    getDefaultBakedModel(songbird),
                    poseStack,
                    bufferSource,
                    songbird,
                    layer,
                    bufferSource.getBuffer(layer),
                    partialTick,
                    packedLight,
                    OverlayTexture.DEFAULT_UV,
                    Color.ofRGB(color[0],color[1],color[2]).argbInt()
            );
        }

        super.render(poseStack, songbird, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
