package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaTools;
import co.juanxxo.junta.effect.LowGravityEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect LOW_GRAVITY = register("low_gravity", new LowGravityEffect());

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of(JuntaTools.MOD_ID, name), effect);
    }

    public static void registerEffects() {
        System.out.println("Registrando efectos para JuntaTools");
    }
}

