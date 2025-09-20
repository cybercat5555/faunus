package cybercat5555.faunus.renderer;

import cybercat5555.faunus.core.EntityRegistry;
import cybercat5555.faunus.core.entity.livingEntity.QuetzalEntity;
import cybercat5555.faunus.renderer.entity.QuetzalEntityRenderer;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;

import java.util.*;

public class ShoulderQuetzalFeatureRenderer<T extends PlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {

    private final Context context;
    private final LivingEntityRenderer<?, ?> renderer;

    public ShoulderQuetzalFeatureRenderer(LivingEntityRenderer<?, ?> livingEntityRenderer, Context context) {
        super(null);
        this.renderer = livingEntityRenderer;
        this.context = context;
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        this.renderShoulderBird(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, headYaw, headPitch, true);
        this.renderShoulderBird(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, headYaw, headPitch, false);
    }


    private static final Identifier QUETZAL_TEXTURE = FaunusID.content("textures/entity/quetzal.png");

    private void renderShoulderBird(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T player, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch, boolean leftShoulder) {
        NbtCompound nbtCompound = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        EntityType.get(nbtCompound.getString("id")).filter(type -> type == EntityRegistry.QUETZAL).ifPresent(type -> {
            matrices.push();
            matrices.scale(1, 1, 1);
            matrices.scale(.75f,.75f,.75f);
            matrices.translate(0, -1.5, 0);
            matrices.translate(0, player.isInSneakingPose() ? 0.25f : 0f, 0);
            matrices.translate(leftShoulder ? 0.5F : -0.5F, 0, -0.1F);
            QuetzalEntity quetzal = (QuetzalEntity) EntityType.getEntityFromNbt(nbtCompound, player.getEntityWorld()).get();


            var part = new ModelPart(new ArrayList<>(),new HashMap<>());
            QuetzalShoulderModel quetzalModel = new QuetzalShoulderModel(QuetzalShoulderModel.data.createModel());

            var renderer = ((QuetzalEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(quetzal));

            quetzalModel.setAngles(quetzal,limbAngle,limbDistance,tickDelta,0,0);
            quetzalModel.render(matrices,vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(QUETZAL_TEXTURE)),light, OverlayTexture.packUv(0,10));

            matrices.pop();
        });
    }

    class QuetzalShoulderModel<T extends QuetzalEntity> extends SinglePartEntityModel<T> {
        public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(FaunusID.content("quetzal"), "main");
        private final ModelPart quetzal;
        private final ModelPart lLeg;
        private final ModelPart rLeg;
        private final ModelPart body;
        private final ModelPart breathingrig;
        private final ModelPart lowerBody;
        private final ModelPart tail;
        private final ModelPart lTailFeather;
        private final ModelPart rTailFeather;
        private final ModelPart head;
        private final ModelPart beak;
        private final ModelPart lWing01;
        private final ModelPart lWing02;
        private final ModelPart lWingFeathers02;
        private final ModelPart lWingFeathers01;
        private final ModelPart rWing01;
        private final ModelPart rWing02;
        private final ModelPart rWingFeathers01;
        private final ModelPart rWingFeathers02;

        public QuetzalShoulderModel(ModelPart root) {
            this.quetzal = root.getChild("quetzal");
            this.lLeg = this.quetzal.getChild("lLeg");
            this.rLeg = this.quetzal.getChild("rLeg");
            this.body = this.quetzal.getChild("body");
            this.breathingrig = this.body.getChild("breathingrig");
            this.lowerBody = this.body.getChild("lowerBody");
            this.tail = this.lowerBody.getChild("tail");
            this.lTailFeather = this.tail.getChild("lTailFeather");
            this.rTailFeather = this.tail.getChild("rTailFeather");
            this.head = this.body.getChild("head");
            this.beak = this.head.getChild("beak");
            this.lWing01 = this.body.getChild("lWing01");
            this.lWing02 = this.lWing01.getChild("lWing02");
            this.lWingFeathers02 = this.lWing02.getChild("lWingFeathers02");
            this.lWingFeathers01 = this.lWing01.getChild("lWingFeathers01");
            this.rWing01 = this.body.getChild("rWing01");
            this.rWing02 = this.rWing01.getChild("rWing02");
            this.rWingFeathers01 = this.rWing02.getChild("rWingFeathers01");
            this.rWingFeathers02 = this.rWing01.getChild("rWingFeathers02");
        }
        public static TexturedModelData data = createBodyLayer();

        public static TexturedModelData createBodyLayer() {
            ModelData meshdefinition = new ModelData();
            ModelPartData ModelPartData = meshdefinition.getRoot();

            ModelPartData quetzal = ModelPartData.addChild("quetzal", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.5F, 0.0F));

            ModelPartData lLeg = quetzal.addChild("lLeg", ModelPartBuilder.create().uv(0, 14).cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F)), ModelTransform.pivot(1.0F, -0.25F, 0.5F));

            ModelPartData rLeg = quetzal.addChild("rLeg", ModelPartBuilder.create().uv(0, 14).mirrored().cuboid(-1.0F, -0.1F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F)).mirrored(false), ModelTransform.pivot(-1.0F, -0.25F, 0.5F));

            ModelPartData body = quetzal.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

            ModelPartData breathingrig = body.addChild("breathingrig", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0513F, -1.7832F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

            ModelPartData lowerBody = body.addChild("lowerBody", ModelPartBuilder.create().uv(0, 8).cuboid(-1.5F, 0.0F, -1.25F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.5513F, 0.2168F, 0.1309F, 0.0F, 0.0F));

            ModelPartData tail = lowerBody.addChild("tail", ModelPartBuilder.create().uv(8, 17).cuboid(-2.0F, 0.1478F, -1.0265F, 4.0F, 5.0F, 2.0F, new Dilation(-0.01F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 0.2618F, 0.0F, 0.0F));

            ModelPartData lTailFeather = tail.addChild("lTailFeather", ModelPartBuilder.create().uv(26, 6).cuboid(-1.75F, -2.0F, -0.25F, 3.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 1.8978F, 1.2235F, 0.0F, 0.0436F, -0.1745F));

            ModelPartData rTailFeather = tail.addChild("rTailFeather", ModelPartBuilder.create().uv(26, 6).mirrored().cuboid(-1.25F, -2.0F, -0.25F, 3.0F, 16.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, 1.8978F, 1.2235F, 0.0F, -0.0436F, 0.1745F));

            ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(18, 0).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                    .uv(16, 1).cuboid(0.0F, -5.0F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.8013F, 0.2168F));

            ModelPartData beak = head.addChild("beak", ModelPartBuilder.create().uv(13, 0).cuboid(-0.75F, -1.0987F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.25F, -0.5F, -1.5F));

            ModelPartData lWing01 = body.addChild("lWing01", ModelPartBuilder.create().uv(8, 14).cuboid(0.25F, -1.0F, -1.0F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.75F, -3.0F, 1.0F));

            ModelPartData lWing02 = lWing01.addChild("lWing02", ModelPartBuilder.create().uv(16, 14).cuboid(0.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.25F, 0.0F, -0.5F));

            ModelPartData lWingFeathers02 = lWing02.addChild("lWingFeathers02", ModelPartBuilder.create().uv(6, 24).cuboid(-1.0F, -2.0F, -0.5F, 13.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 1.0F, 0.5F));

            ModelPartData lWingFeathers01 = lWing01.addChild("lWingFeathers01", ModelPartBuilder.create().uv(0, 24).cuboid(-1.0F, -2.0F, -0.5F, 3.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.25F, 1.0F, 0.0F));

            ModelPartData rWing01 = body.addChild("rWing01", ModelPartBuilder.create().uv(8, 14).mirrored().cuboid(-3.25F, -1.0F, -1.0F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.75F, -3.0F, 1.0F));

            ModelPartData rWing02 = rWing01.addChild("rWing02", ModelPartBuilder.create().uv(16, 14).mirrored().cuboid(-4.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.25F, 0.0F, -0.5F));

            ModelPartData rWingFeathers01 = rWing02.addChild("rWingFeathers01", ModelPartBuilder.create().uv(6, 24).mirrored().cuboid(-12.0F, -2.0F, -0.5F, 13.0F, 7.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, 1.0F, 0.5F));

            ModelPartData rWingFeathers02 = rWing01.addChild("rWingFeathers02", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(-2.0F, -2.0F, -0.5F, 3.0F, 7.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.25F, 1.0F, 0.0F));

            return TexturedModelData.of(meshdefinition, 32, 32);
        }



        @Override
        public void setAngles(T entity, float limbAngle, float limbDistance, float maybeTickDelta, float headYaw, float headPitch) {
            quetzal.traverse().forEach(ModelPart::resetTransform);
            var state = new net.minecraft.entity.AnimationState(){};
            state.startIfNotRunning(0);
            this.updateAnimation(state,idle,entity.age+ maybeTickDelta);
        }
        @Override
        public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
            quetzal.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        }

        @Override
        public ModelPart getPart() {
            return quetzal;
        }

        public static final Animation idle = Animation.Builder.create(1.5F).looping()
                .addBoneAnimation("tail", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(30.0F, 0.7229F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lTailFeather", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(17.6707F, -2.2689F, 7.1511F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rTailFeather", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(17.6707F, 2.2689F, -7.1511F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWing01", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(75.0F, 28.0F, 78.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWing01", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWing02", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -2.5F, 145.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWing02", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWingFeathers02", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWingFeathers01", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("lWingFeathers01", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWing01", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(75.0F, -28.0F, -78.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWing01", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWing02", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 2.5F, -145.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWing02", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWingFeathers01", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWingFeathers02", new Transformation(Transformation.Targets.ROTATE,
                        new Keyframe(0.0F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("rWingFeathers02", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("breathingrig", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -0.1F, 0.0F), Transformation.Interpolations.LINEAR),
                        new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("breathingrig", new Transformation(Transformation.Targets.SCALE,
                        new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.7917F, AnimationHelper.createScalingVector(1.02F, 1.02F, 1.02F), Transformation.Interpolations.LINEAR),
                        new Keyframe(1.5F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
                ))
                .addBoneAnimation("body", new Transformation(Transformation.Targets.TRANSLATE,
                        new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                        new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.1F, 0.0F), Transformation.Interpolations.LINEAR),
                        new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
                ))
                .build();

    }
}