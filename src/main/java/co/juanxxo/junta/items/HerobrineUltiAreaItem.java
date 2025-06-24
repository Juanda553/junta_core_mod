package co.juanxxo.junta.items;

import co.juanxxo.junta.custom.ModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HerobrineUltiAreaItem extends Item {
    public HerobrineUltiAreaItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        if (!world.isClient) {
            BlockPos pos = user.getBlockPos();

            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                    SoundCategory.MASTER,
                    2.0f,
                    1.0f
            );
            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
                    SoundCategory.MASTER,
                    2.0f,
                    1.0f
            );

            perfomAttackArea((ServerWorld) world, pos, user);

//            user.getItemCooldownManager().set(this, 240);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private void perfomAttackArea(ServerWorld world, BlockPos pos, PlayerEntity user) {
        double attackRadius = 25.0;

        // particulas
        for (int i = 0; i < 100; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * attackRadius * 2;
            double offsetY = world.random.nextDouble() * attackRadius;
            double offsetZ = (world.random.nextDouble() - 0.5) * attackRadius * 2;

            // el q lo lea es maricón
            world.spawnParticles(
                    ParticleTypes.FLAME,
                    pos.getX() + offsetX,
                    pos.getY() + offsetY,
                    pos.getZ() + offsetZ,
                    10,
                    3.0, 3.0, 3.0,
                    0.1
            );

            world.spawnParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    pos.getX() + offsetX,
                    pos.getY() + offsetY,
                    pos.getZ() + offsetZ,
                    6,
                    5.0, 5.0, 5.0,
                    0.1
            );

            world.spawnParticles(
                    ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    pos.getX() + offsetX,
                    pos.getY() + offsetY,
                    pos.getZ() + offsetZ,
                    1,
                    0.0, 0.0, 0.0,
                    0.1
            );

            world.spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    pos.getX() + offsetX,
                    pos.getY() + offsetY,
                    pos.getZ() + offsetZ,
                    5,
                    5.0, 5.0, 5.0,
                    0.1
            );
        }

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                new Box(pos).expand(attackRadius),
                entity -> entity != user && entity.isAlive()
        );

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(ModDamageTypes.of(world, ModDamageTypes.HEROBRINE_MELEE_DAMAGE_TYPE), 100f);
            }
        }
    }
}
