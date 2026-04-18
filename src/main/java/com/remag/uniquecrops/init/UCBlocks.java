package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.*;
import com.remag.uniquecrops.blocks.crops.*;
import com.remag.uniquecrops.blocks.supercrops.*;
import com.remag.uniquecrops.core.enums.EnumLily;
import com.remag.uniquecrops.items.base.ItemBlockUC;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UCBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UniqueCrops.MOD_ID);
    public static final List<Block> CROPS = new ArrayList<>();

    /**
     * GENERAL
     */
    public static final DeferredBlock<Block> ABSTRACT_BARREL = register("abstractbarrel", AbstractBarrelBlock::new);
    public static final DeferredBlock<Block> BUCKET_ROPE = register("bucketrope", BucketRopeBlock::new);
    public static final DeferredBlock<Block> CINDER_TORCH = register("cindertorch", CinderTorchBlock::new);
    public static final DeferredBlock<Block> DARK_BLOCK = register("darkblock", () -> new Block(Properties.of().sound(SoundType.STONE).strength(6000000.0F).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> DRIED_THATCH = register("driedthatch", () -> new Block(Properties.of().sound(SoundType.GRASS).strength(0.1F).mapColor(MapColor.DIRT)));
    public static final DeferredBlock<Block> EGG_BASKET = register("egg_basket", EggBasketBlock::new);
    public static final DeferredBlock<Block> GOBLET = register("goblet", GobletBlock::new);
    public static final DeferredBlock<Block> HOURGLASS = register("hourglass", HourglassBlock::new, true, true);
    public static final DeferredBlock<Block> LILY_ENDER = registerLily("enderlily", () -> new BaseLilyBlock(EnumLily.ENDER), false);
    public static final DeferredBlock<Block> LILY_ICE = registerLily("icelily", () -> new BaseLilyBlock(EnumLily.ICE), false);
    public static final DeferredBlock<Block> LILY_JUNGLE = registerLily("junglelily", () -> new BaseLilyBlock(EnumLily.JUNGLE), false);
    public static final DeferredBlock<Block> LILY_LAVA = registerLily("lavalily", () -> new BaseLilyBlock(EnumLily.LAVA), true);
    public static final DeferredBlock<Block> NORMIECRATE = register("normiecrate", () -> new Block(Properties.of().sound(SoundType.WOOD).strength(0.25F, 5.0F).mapColor(MapColor.WOOD)));
    public static final DeferredBlock<Block> OBTUSE_PLATFORM = register("obtuse_platform", ObtusePlatformBlock::new);
    public static final DeferredBlock<Block> OLDCOBBLE = register("oldcobble", () -> new Block(propsCopy(Blocks.COBBLESTONE)));
    public static final DeferredBlock<Block> OLDBRICK = register("oldbrick", () -> new Block(propsCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> OLDCOBBLEMOSS = register("oldcobblemoss", () -> new Block(propsCopy(Blocks.MOSSY_COBBLESTONE)));
    public static final DeferredBlock<Block> OLDDIAMOND = register("olddiamond", () -> new Block(propsCopy(Blocks.DIAMOND_BLOCK)));
    public static final DeferredBlock<Block> OLDGOLD = register("oldgold", () -> new Block(propsCopy(Blocks.GOLD_BLOCK)));
    public static final DeferredBlock<Block> OLDGRASS = register("oldgrass", () -> new GrassBlock(propsCopy(Blocks.GRASS_BLOCK)));
    public static final DeferredBlock<Block> OLDGRAVEL = register("oldgravel", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), propsCopy(Blocks.GRAVEL)));
    public static final DeferredBlock<Block> OLDIRON = register("oldiron", () -> new Block(propsCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> PRECISION_BLOCK = register("precision_block", () -> new Block(propsCopy(Blocks.DIAMOND_BLOCK)));
    public static final DeferredBlock<Block> RUINEDBRICKS = register("ruinedbricks", () -> new Block(propsCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> RUINEDBRICKSCARVED = register("ruinedbrickscarved", () -> new Block(propsCopy(Blocks.CHISELED_STONE_BRICKS)));
    public static final DeferredBlock<Block> RUINEDBRICKSRED = register("ruinedbricksred", () -> new RotatedPillarBlock(propsCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> DREAMCATCHER = register("dreamcatcher", DreamcatcherBlock::new);
    public static final DeferredBlock<Block> HARVEST_TRAP = register("harvest_trap", HarvestTrapBlock::new);
    //public static final RegistryObject<Block> CROP_PORTAL = register("crop_portal", CropPortalBlock::new, false, false);
    public static final DeferredBlock<Block> DEMO_CORD = register("demo_cord", DemoCordBlock::new);
    public static final DeferredBlock<Block> TOTEMHEAD = register("totemhead", TotemheadBlock::new);
    public static final DeferredBlock<Block> SUN_DIAL = register("sun_dial", SundialBlock::new);
    public static final DeferredBlock<Block> SUN_BLOCK = register("sun_block", SunBlock::new);
    public static final DeferredBlock<Block> INVISIBILIA_GLASS = register("invisibilia_glass", InvisibiliaGlass::new);
    public static final DeferredBlock<Block> FLYWOOD_LEAVES = register("flywood_leaves", () -> new LeavesBlock(propsCopy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<Block> FLYWOOD_LOG = register("flywood_log", () -> new RotatedPillarBlock(propsCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> FLYWOOD_SAPLING = register("flywood_sapling", () -> new SaplingBlock(TreeGrower.OAK, propsCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<Block> FLYWOOD_PLANKS = register("flywood_planks", () -> new Block(propsCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<Block> FLYWOOD_STAIRS = register("flywood_stairs", () -> new StairBlock(FLYWOOD_PLANKS.get().defaultBlockState(), propsCopy(FLYWOOD_PLANKS.get())));
    public static final DeferredBlock<Block> RUINEDBRICKS_STAIRS = register("ruinedbricks_stairs", () -> new StairBlock(RUINEDBRICKS.get().defaultBlockState(), propsCopy(RUINEDBRICKS.get())));
    public static final DeferredBlock<Block> FLYWOOD_SLAB = register("flywood_slab", () -> new SlabBlock(propsCopy(FLYWOOD_PLANKS.get())));
    public static final DeferredBlock<Block> RUINEDBRICKS_SLAB = register("ruinedbricks_slab", () -> new SlabBlock(propsCopy(RUINEDBRICKS.get())));
    public static final DeferredBlock<Block> RUINEDBRICKSCARVED_SLAB = register("ruinedbrickscarved_slab", () -> new SlabBlock(propsCopy(RUINEDBRICKSCARVED.get())));
    public static final DeferredBlock<Block> ROSEWOOD_PLANKS = register("rosewood_planks", () -> new Block(propsCopy(FLYWOOD_PLANKS.get())));
    public static final DeferredBlock<Block> ROSEWOOD_STAIRS = register("rosewood_stairs", () -> new StairBlock(ROSEWOOD_PLANKS.get().defaultBlockState(), propsCopy(ROSEWOOD_PLANKS.get())));
    public static final DeferredBlock<Block> ROSEWOOD_SLAB = register("rosewood_slab", () -> new SlabBlock(propsCopy(ROSEWOOD_PLANKS.get())));
    public static final DeferredBlock<Block> FLYWOOD_TRAPDOOR = register("flywood_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, propsCopy(Blocks.OAK_TRAPDOOR)));
    public static final DeferredBlock<Block> ROSEWOOD_TRAPDOOR = register("rosewood_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, propsCopy(Blocks.OAK_TRAPDOOR)));

    /**
     * CROPS
     */
    public static final DeferredBlock<BaseCropsBlock> NORMAL_CROP = registerCrop("normal", Normal::new);
    public static final DeferredBlock<BaseCropsBlock> ARTISIA_CROP = registerCrop("artisia", Artisia::new);
    public static final DeferredBlock<BaseCropsBlock> PRECISION_CROP = registerCrop("precision", Precision::new);
    public static final DeferredBlock<BaseCropsBlock> KNOWLEDGE_CROP = registerCrop("knowledge", Knowledge::new);
    public static final DeferredBlock<BaseCropsBlock> DIRIGIBLE_CROP = registerCrop("dirigible", Dirigible::new);
    public static final DeferredBlock<BaseCropsBlock> MILLENNIUM_CROP = registerCrop("millennium", Millennium::new);
    public static final DeferredBlock<BaseCropsBlock> ENDERLILY_CROP = registerCrop("enderlily", Enderlily::new);
    public static final DeferredBlock<BaseCropsBlock> COLLIS_CROP = registerCrop("collis", Collis::new);
    public static final DeferredBlock<BaseCropsBlock> INVISIBILIA_CROP = registerCrop("invisibilia", Invisibilia::new);
    public static final DeferredBlock<BaseCropsBlock> MARYJANE_CROP = registerCrop("maryjane", MaryJane::new);
    public static final DeferredBlock<BaseCropsBlock> WEEPINGBELLS_CROP = registerCrop("weepingbells", WeepingBells::new);
    public static final DeferredBlock<BaseCropsBlock> MUSICA_CROP = registerCrop("musica", Musica::new);
    public static final DeferredBlock<BaseCropsBlock> CINDERBELLA_CROP = registerCrop("cinderbella", Cinderbella::new);
    public static final DeferredBlock<BaseCropsBlock> MERLINIA_CROP = registerCrop("merlinia", Merlinia::new);
    public static final DeferredBlock<BaseCropsBlock> EULA_CROP = registerCrop("eula", Eula::new);
    public static final DeferredBlock<BaseCropsBlock> COBBLONIA_CROP = registerCrop("cobblonia", Cobblonia::new);
    public static final DeferredBlock<BaseCropsBlock> DYEIUS_CROP = registerCrop("dyeius", Dyeius::new);
    public static final DeferredBlock<BaseCropsBlock> ABSTRACT_CROP = registerCrop("abstract", Abstract::new);
    public static final DeferredBlock<BaseCropsBlock> WAFFLONIA_CROP = registerCrop("wafflonia", Wafflonia::new);
    public static final DeferredBlock<BaseCropsBlock> DEVILSNARE_CROP = registerCrop("devilsnare", DevilSnare::new);
    public static final DeferredBlock<BaseCropsBlock> PIXELSIUS_CROP = registerCrop("pixelsius", () -> new BaseCropsBlock(UCItems.PIXELS, UCItems.PIXELSIUS_SEED));
    public static final DeferredBlock<BaseCropsBlock> PETRAMIA_CROP = registerCrop("petramia", Petramia::new);
    public static final DeferredBlock<BaseCropsBlock> MALLEATORIS_CROP = registerCrop("malleatoris", Malleatoris::new);
    public static final DeferredBlock<BaseCropsBlock> IMPERIA_CROP = registerCrop("imperia", Imperia::new);
    public static final DeferredBlock<BaseCropsBlock> LACUSIA_CROP = registerCrop("lacusia", Lacusia::new);
    public static final DeferredBlock<BaseCropsBlock> HEXIS_CROP = registerCrop("hexis", Hexis::new);
    public static final DeferredBlock<BaseCropsBlock> INDUSTRIA_CROP = registerCrop("industria", Industria::new);
    public static final DeferredBlock<BaseCropsBlock> QUARRY_CROP = registerCrop("quarry", Fossura::new);
    public static final DeferredBlock<BaseCropsBlock> DONUTSTEEL_CROP = registerCrop("donutsteel", DonutSteel::new);
    public static final DeferredBlock<BaseCropsBlock> INSTABILIS_CROP = registerCrop("instabilis", Instabilis::new);
    public static final DeferredBlock<BaseCropsBlock> SUCCO_CROP = registerCrop("succo", Succo::new);
    public static final DeferredBlock<BaseCropsBlock> ADVENTUS_CROP = registerCrop("adventus", Adventus::new);
    public static final DeferredBlock<BaseCropsBlock> HOLY_CROP = registerCrop("holy", HolyCrop::new);
    public static final DeferredBlock<BaseCropsBlock> MAGNES_CROP = registerCrop("magnes", Magnes::new);
    public static final DeferredBlock<BaseCropsBlock> FEROXIA_CROP = registerCrop("feroxia", Feroxia::new);

    /**
     * SUPER CROPS
     */
    public static final DeferredBlock<Block> STALK = register("stalk", StalkBlock::new, false, false);
    public static final DeferredBlock<Block> EXEDO = register("exedo", Exedo::new, false, false);
    public static final DeferredBlock<Block> COCITO = register("cocito", Cocito::new, false, false);
    public static final DeferredBlock<Block> ITERO = register("itero", Itero::new, false, false);
    public static final DeferredBlock<Block> FASCINO = register("fascino", Fascino::new, false, false);
    public static final DeferredBlock<Block> WEATHERFLESIA = register("weatherflesia", Weatherflesia::new, false, false);
    public static final DeferredBlock<Block> LIGNATOR = register("lignator", Lignator::new, false, false);
    public static final DeferredBlock<Block> SANALIGHT = register("sanalight", Sanalight::new, false, false);
    public static final DeferredBlock<Block> FUNNY_LIGHT = register("funnylight", FunnyLightBlock::new, false, false);

    public static final DeferredBlock<BaseCropsBlock> DUMMY_CROP = registerCrop("dummy", () -> new BaseCropsBlock(null, null));

    public static <B extends BaseCropsBlock> DeferredBlock<B> registerCrop(String name, Supplier<? extends B> supplier) {

        return BLOCKS.register("crop_" + name, supplier);
    }

    private static <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> supplier) {

        return register(name, supplier, true, false);
    }

    private static <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> supplier, boolean itemBlock, boolean custom) {

        DeferredBlock<B> block = BLOCKS.register(name, supplier);
        if (itemBlock) {
            if (!custom)
                UCItems.registerItem(name, () -> new BlockItem(block.get(), UCItems.defaultBuilder()));
            else
                UCItems.registerItem(name, () -> new ItemBlockUC(block.get()));
        }
        return block;
    }

    private static <B extends Block> DeferredBlock<B> registerLily(String name, Supplier<? extends B> supplier, boolean lavaproof) {

        DeferredBlock<B> block = BLOCKS.register(name, supplier);
        if (lavaproof)
            UCItems.registerItem(name, () -> new PlaceOnWaterBlockItem(block.get(), UCItems.defaultBuilder().fireResistant()));
        else
            UCItems.registerItem(name, () -> new PlaceOnWaterBlockItem(block.get(), UCItems.defaultBuilder()));
        return block;
    }

    private static Properties propsCopy(Block block) {

        return Properties.ofFullCopy(block);
    }

}
