package co.juanxxo.junta.custom.procedures;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Borracheras {

    public static void level1(LivingEntity user){
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 3600, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 3600, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3600, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 3600, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 360, 1, false, false));
    }

    public static void level2(LivingEntity user){
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 6000, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 6000, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 6000, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 6000, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 600, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 1, false, false));
    }
}
