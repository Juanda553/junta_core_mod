package co.juanxxo.junta;

import co.juanxxo.junta.command.WaitingOverlayCommand;
import co.juanxxo.junta.command.CountdownCommand;
import co.juanxxo.junta.entity.DefaultJuntaRaccoonEntity;
import co.juanxxo.junta.entity.ZeroPointOrbEntity;
import co.juanxxo.junta.overlay.OverlayState;
import co.juanxxo.junta.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JuntaCoreMain implements ModInitializer {
	public static final String MOD_ID = "junta";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		// Registrar biomas
		ModBiomes.registerBiomes();
		// Registrar items
		ModItems.registerModItems();
		ModItemGroups.registerItems();
		ModBlocks.registerModBlocks();
		// Registrar mobs
		FabricDefaultAttributeRegistry.register(ModEntities.ZERO_POINT_ORB, ZeroPointOrbEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DEFAULT_JUNTA_RACCOON, DefaultJuntaRaccoonEntity.setAttributes());
		// Registrar efectos
		ModEffects.registerEffects();
		
		// Registrar tipos de payload - Waiting Overlay
		PayloadTypeRegistry.playS2C().register(WaitingOverlayCommand.ShowWaitingPayload.ID, WaitingOverlayCommand.ShowWaitingPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(WaitingOverlayCommand.HideWaitingPayload.ID, WaitingOverlayCommand.HideWaitingPayload.CODEC);
		
		// Registrar tipos de payload - Countdown
		PayloadTypeRegistry.playS2C().register(CountdownCommand.StartCountdownPayload.ID, CountdownCommand.StartCountdownPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(CountdownCommand.AddTimePayload.ID, CountdownCommand.AddTimePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(CountdownCommand.PauseCountdownPayload.ID, CountdownCommand.PauseCountdownPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(CountdownCommand.ResumeCountdownPayload.ID, CountdownCommand.ResumeCountdownPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(CountdownCommand.StopCountdownPayload.ID, CountdownCommand.StopCountdownPayload.CODEC);
		
		// Registrar comandos
		CommandRegistrationCallback.EVENT.register(WaitingOverlayCommand::register);
		CommandRegistrationCallback.EVENT.register(CountdownCommand::register);
		
		// Sincronizar overlays cuando un jugador se conecta
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			OverlayState.syncAll(handler.player);
		});

		LOGGER.info("La Junta Core Cargado");
	}
}