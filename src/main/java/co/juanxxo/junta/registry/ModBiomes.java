package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaCoreMain;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModBiomes {
    public static final RegistryKey<Biome> ZERO_POINT_CORRUPTION = RegistryKey.of(
            RegistryKeys.BIOME, Identifier.of(JuntaCoreMain.MOD_ID, "zero_point_corruption")
    );

    public static final RegistryKey<Biome> ZERO_POINT_DIMENSION = RegistryKey.of(
            RegistryKeys.BIOME, Identifier.of(JuntaCoreMain.MOD_ID, "zero_point_dimension")
    );

    public static void registerBiomes() {
        System.out.println("Registrando biomas");
    }
}
