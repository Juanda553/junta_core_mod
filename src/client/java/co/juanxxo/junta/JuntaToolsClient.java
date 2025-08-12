package co.juanxxo.junta;

import co.juanxxo.junta.entity.renderer.DefaultJuntaRaccoonRenderer;
import co.juanxxo.junta.registry.ModBlocks;
import co.juanxxo.junta.registry.ModEntities;
import co.juanxxo.junta.entity.renderer.HerobrineRangedAttackRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import co.juanxxo.junta.entity.renderer.ZeroPointOrbRenderer;
import net.minecraft.client.render.RenderLayer;

public class JuntaToolsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.ZERO_POINT_ORB, ZeroPointOrbRenderer::new);
		EntityRendererRegistry.register(ModEntities.DEFAULT_JUNTA_RACCOON, DefaultJuntaRaccoonRenderer::new);
		EntityRendererRegistry.register(ModEntities.HEROBRINE_RANGED_ATTACK_ENTITY, HerobrineRangedAttackRenderer::new);

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_BLUE_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_AQUA_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_SKY_BLUE_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_PURPLE_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_BLACK_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_WHITE_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_RED_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_YELLOW_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_ORANGE_LASER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZP_GREEN_LASER, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZERO_FIELD, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIQUIDO_RARO, RenderLayer.getTranslucent());


//		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZERO_POINT_BLOCK, RenderLayer.getEndPortal());
	}
}