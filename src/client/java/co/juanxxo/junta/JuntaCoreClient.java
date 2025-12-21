package co.juanxxo.junta;

import co.juanxxo.junta.entity.renderer.DefaultJuntaRaccoonRenderer;
import co.juanxxo.junta.registry.ModBlocks;
import co.juanxxo.junta.registry.ModEntities;
import co.juanxxo.junta.entity.renderer.HerobrineRangedAttackRenderer;
import co.juanxxo.junta.overlay.WaitingOverlay;
import co.juanxxo.junta.overlay.CountdownOverlay;
import co.juanxxo.junta.command.WaitingOverlayCommand;
import co.juanxxo.junta.command.CountdownCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import co.juanxxo.junta.entity.renderer.ZeroPointOrbRenderer;
import net.minecraft.client.render.RenderLayer;

public class JuntaCoreClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Registrar overlays
		WaitingOverlay.register();
		CountdownOverlay.register();
		
		// Registrar receptores de paquetes de red - Waiting Overlay
		ClientPlayNetworking.registerGlobalReceiver(WaitingOverlayCommand.ShowWaitingPayload.ID, (payload, context) -> {
			context.client().execute(() -> WaitingOverlay.show(payload.maxPlayers()));
		});
		
		ClientPlayNetworking.registerGlobalReceiver(WaitingOverlayCommand.HideWaitingPayload.ID, (payload, context) -> {
			context.client().execute(() -> WaitingOverlay.hide());
		});
		
		// Registrar receptores de paquetes de red - Countdown
		ClientPlayNetworking.registerGlobalReceiver(CountdownCommand.StartCountdownPayload.ID, (payload, context) -> {
			context.client().execute(() -> CountdownOverlay.start(payload.hours(), payload.minutes(), payload.seconds(), payload.title()));
		});
		
		ClientPlayNetworking.registerGlobalReceiver(CountdownCommand.AddTimePayload.ID, (payload, context) -> {
			context.client().execute(() -> CountdownOverlay.addTime(payload.hours(), payload.minutes(), payload.seconds()));
		});
		
		ClientPlayNetworking.registerGlobalReceiver(CountdownCommand.PauseCountdownPayload.ID, (payload, context) -> {
			context.client().execute(() -> CountdownOverlay.pause());
		});
		
		ClientPlayNetworking.registerGlobalReceiver(CountdownCommand.ResumeCountdownPayload.ID, (payload, context) -> {
			context.client().execute(() -> CountdownOverlay.resume());
		});
		
		ClientPlayNetworking.registerGlobalReceiver(CountdownCommand.StopCountdownPayload.ID, (payload, context) -> {
			context.client().execute(() -> CountdownOverlay.stop());
		});
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