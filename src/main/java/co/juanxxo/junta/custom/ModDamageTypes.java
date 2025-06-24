package co.juanxxo.junta.custom;

import co.juanxxo.junta.JuntaTools;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> HEROBRINE_MELEE_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(JuntaTools.MOD_ID, "herobrine_melee"));

    public static DamageSource of(World world, RegistryKey<DamageType> damageTypeKey) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(HEROBRINE_MELEE_DAMAGE_TYPE));
    }
}
