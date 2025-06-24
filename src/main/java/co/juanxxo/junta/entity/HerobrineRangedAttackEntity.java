package co.juanxxo.junta.entity;

import co.juanxxo.junta.custom.ModDamageTypes;
import co.juanxxo.junta.registry.ModEntities;
import co.juanxxo.junta.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class HerobrineRangedAttackEntity extends ThrownItemEntity {
    public HerobrineRangedAttackEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public HerobrineRangedAttackEntity(World world, LivingEntity owner, Item item) {
        super(ModEntities.HEROBRINE_RANGED_ATTACK_ENTITY, owner, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient && this.age > 1) {
            ((ServerWorld) this.getWorld()).spawnParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    25,
                    0.3, 0.3, 0.3,
                    0.01
            );
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        if (entityHitResult.getEntity() instanceof LivingEntity target) {
            target.damage(ModDamageTypes.of(this.getWorld(), ModDamageTypes.HEROBRINE_MELEE_DAMAGE_TYPE), 15.0f);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            ((ServerWorld) this.getWorld()).spawnParticles(
                    ParticleTypes.SONIC_BOOM, // Tipo de partícula
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    6,
                    1, 1, 1,
                    0.1
            );
        }
        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HEROBRINE_RANGED_ATTACK;
    }
}
