package co.juanxxo.junta.registry;

import co.juanxxo.junta.JuntaCoreMain;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block ZP_BLUE_LASER = createZpLaser("zp_blue_laser");
    public static final Block ZP_AQUA_LASER = createZpLaser("zp_aqua_laser");
    public static final Block ZP_BLACK_LASER = createZpLaser("zp_black_laser");
    public static final Block ZP_GREEN_LASER = createZpLaser("zp_green_laser");
    public static final Block ZP_ORANGE_LASER = createZpLaser("zp_orange_laser");
    public static final Block ZP_PURPLE_LASER = createZpLaser("zp_purple_laser");
    public static final Block ZP_RED_LASER = createZpLaser("zp_red_laser");
    public static final Block ZP_SKY_BLUE_LASER = createZpLaser("zp_sky_blue_laser");
    public static final Block ZP_WHITE_LASER = createZpLaser("zp_white_laser");
    public static final Block ZP_YELLOW_LASER = createZpLaser("zp_yellow_laser");

    public static final Block ZERO_FIELD = registerBlock("zero_field", new StainedGlassBlock(DyeColor.WHITE, AbstractBlock.Settings.create()
            .nonOpaque().noCollision().strength(-1.0f, 3600000.0f)
            .sounds(BlockSoundGroup.GLASS).luminance(s -> 7).pistonBehavior(PistonBehavior.IGNORE)
            .suffocates((s, w, p) -> false).allowsSpawning((state, world, pos, type) -> false)
    ));

    public static final Block LIQUIDO_RARO = registerBlock("liquido_raro", new CarpetBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.CLEAR)
            .nonOpaque().strength(0.1f).sounds(BlockSoundGroup.WOOL)
    ));

    public static final Block ZERO_POINT_BLOCK = registerBlock("zero_point_block", new Block(AbstractBlock.Settings.create()
            .strength(-1.0f, 3600000.0f)
            .allowsSpawning((state, world, pos, type) -> false)
            .luminance(s -> 15)
            .sounds(BlockSoundGroup.STONE)
            ));

    private static Block createZpLaser(String name) {
        return registerBlock(name,
                new StainedGlassBlock(DyeColor.WHITE, AbstractBlock.Settings.create()
                        .strength(-1.0f, 3600000.0f)
                        .sounds(BlockSoundGroup.GLASS)
                        .nonOpaque()
                        .allowsSpawning((state, world, pos, type) -> false)
                        .luminance(s -> 15)
                        .velocityMultiplier(1.2f)
                        .jumpVelocityMultiplier(1.2f)
                        .slipperiness(0.8f)
                ));
    }

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(JuntaCoreMain.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(JuntaCoreMain.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(){
        JuntaCoreMain.LOGGER.info("Registrando bloques de La Junta.");
    }
}
