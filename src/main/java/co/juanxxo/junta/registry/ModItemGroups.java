package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaCoreMain;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup JUNTA_TAB = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(JuntaCoreMain.MOD_ID, "junta_tab"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.LIMONCITO))
                    .displayName(Text.translatable("itemgroup.junta.junta_tab"))
                    .entries((displayContext, entries) -> {

                        entries.add(ModItems.GOLDEN_ACCORDION);
                        entries.add(ModItems.LIMONCITO);
                        entries.add(ModItems.JHC_LETTER);
                        entries.add(ModItems.ZERO_FRAGMENT);

                        entries.add(ModItems.ENERGY_DRINK);
                        entries.add(ModItems.AGUARDIENTE_AZUL);
                        entries.add(ModItems.AGUARDIENTE_VERDE);
                        entries.add(ModItems.AGUARDIENTE_ROJO);
                        entries.add(ModItems.BUCACHANANS);

                        entries.add(ModBlocks.LIQUIDO_RARO);

                        entries.add(ModBlocks.ZP_SKY_BLUE_LASER);
                        entries.add(ModBlocks.ZP_PURPLE_LASER);
                        entries.add(ModBlocks.ZP_AQUA_LASER);
                        entries.add(ModBlocks.ZP_BLACK_LASER);
                        entries.add(ModBlocks.ZP_WHITE_LASER);
                        entries.add(ModBlocks.ZP_RED_LASER);
                        entries.add(ModBlocks.ZP_ORANGE_LASER);
                        entries.add(ModBlocks.ZP_YELLOW_LASER);
                        entries.add(ModBlocks.ZP_GREEN_LASER);
                        entries.add(ModBlocks.ZP_BLUE_LASER);

                        entries.add(ModBlocks.ZERO_POINT_BLOCK);
                        entries.add(ModBlocks.ZERO_FIELD);

                    }).build());

    public static void registerItems() {
        JuntaCoreMain.LOGGER.info("Grupos de items registrados para JUNTA");
    }
}
