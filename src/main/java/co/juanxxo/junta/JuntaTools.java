package co.juanxxo.junta;

import co.juanxxo.junta.entity.ZeroPointOrbEntity;
import co.juanxxo.junta.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JuntaTools implements ModInitializer {
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

		LOGGER.info("La Junta Hardcore mobs ha cargado xd");
	}
}