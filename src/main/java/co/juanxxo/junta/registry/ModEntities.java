package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaTools;
import co.juanxxo.junta.entity.DefaultJuntaRaccoonEntity;
import co.juanxxo.junta.entity.HerobrineRangedAttackEntity;
import co.juanxxo.junta.entity.ZeroPointOrbEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ZeroPointOrbEntity> ZERO_POINT_ORB = registerEnity("zero_point_orb", ZeroPointOrbEntity::new, 3,4);
    public static final EntityType<DefaultJuntaRaccoonEntity> DEFAULT_JUNTA_RACCOON = registerEnity("default_junta_raccoon", DefaultJuntaRaccoonEntity::new, 1,1.25F);

    public static final EntityType<HerobrineRangedAttackEntity> HEROBRINE_RANGED_ATTACK_ENTITY = registerEnity("hb_ranged_attack_entity", HerobrineRangedAttackEntity::new, 1, 1);


    public static <T extends Entity> EntityType<T> registerEnity(String name, EntityType.EntityFactory<T> entity, float w, float h){
        return Registry.register(
                Registries.ENTITY_TYPE, Identifier.of(JuntaTools.MOD_ID, name),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, entity).dimensions(EntityDimensions.fixed(w,h)).build());
    }

    public static void register() {
        JuntaTools.LOGGER.info("Mobs registrados para Junta");
    }
}