package software.bernie.example.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.geo.render.AnimatingModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GeoExampleLayer<T extends LivingEntity> extends GeoLayerRenderer<T> {
	// A resource location for the texture of the layer. This will be applied onto
	// pre-existing cubes on the model
	private static final ResourceLocation LAYER = new ResourceLocation(GeckoLib.ModID,
			"textures/entity/le_glasses.png");
	// A resource location for the model of the entity. This model is put on top of
	// the normal one, which is then given the texture
	@SuppressWarnings("unused")
	private static final ResourceLocation MODEL = new ResourceLocation(GeckoLib.ModID, "geo/le.geo.json");

	public GeoExampleLayer(IGeoRenderer<T> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch) {
		RenderType cameo = RenderType.armorCutoutNoCull(LAYER);
		matrixStackIn.pushPose();
		// Move or scale the model as you see fit
		matrixStackIn.scale(1.0f, 1.0f, 1.0f);
		matrixStackIn.translate(0.0d, 0.0d, 0.0d);
		this.getRenderer().render((AnimatingModel) getEntityModel().getOrCreateAnimator(entityLivingBaseIn).boneTree,
				entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn, bufferIn.getBuffer(cameo),
				packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		matrixStackIn.popPose();
	}
}
