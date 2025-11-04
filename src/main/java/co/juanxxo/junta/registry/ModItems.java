package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaCoreMain;
import co.juanxxo.junta.items.*;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item HEROBRINE_AREA_ATTACK = register("hb_area_attack", new HerobrineAreaAttackItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item HEROBRINE_ULTRA_ATTACK = register("hb_ulti_attack", new HerobrineUltiAreaItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item HEROBRINE_RANGED_ATTACK = register("hb_ranged_attack", new HerobrineRangedAttackItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

    public static final Item LIMONCITO = register("limoncito", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item GOLDEN_ACCORDION = register("golden_accordion", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item JHC_LETTER = register("jhc_letter", new Item(new Item.Settings().rarity(Rarity.EPIC)));

    public static final Item AGUARDIENTE_AZUL = register("aguardiente_azul", new AguardienteAzul(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0f).build())));
    public static final Item AGUARDIENTE_VERDE = register("aguardiente_verde", new AguardienteVerde(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0f).build())));
    public static final Item AGUARDIENTE_ROJO = register("aguardiente_rojo", new AguardienteRojo(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0f).build())));
    public static final Item BUCACHANANS = register("buchanans", new Buchanans(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0f).build())));
    public static final Item ENERGY_DRINK = register("energy_drink", new EnergyDrink(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0f).build())));

    public static final Item ZERO_FRAGMENT = register("zero_fragment", new Item(new Item.Settings().rarity(Rarity.EPIC)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(JuntaCoreMain.MOD_ID, name), item);
    }

    public static void registerModItems() {
        JuntaCoreMain.LOGGER.info("Registrando ítems de " + JuntaCoreMain.MOD_ID);
    }
}
