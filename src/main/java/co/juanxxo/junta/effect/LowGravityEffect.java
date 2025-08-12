package co.juanxxo.junta.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

public class LowGravityEffect extends StatusEffect {
    public LowGravityEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xADD8E6);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.isOnGround() && entity.getVelocity().y < 0 && !entity.isFallFlying()) {
            Vec3d vel = entity.getVelocity();
            double newY = Math.max(vel.y, -0.125);
            entity.setVelocity(vel.x, newY, vel.z);
            entity.velocityModified = true;
        }
        return true;
    }





    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Se llama cada tick
    }
}
