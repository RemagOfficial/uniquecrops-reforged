package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.tiles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class UCTiles {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, UniqueCrops.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileArtisia>> ARTISIA = register("artisia", TileArtisia::new, set(UCBlocks.ARTISIA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFeroxia>> FEROXIA = register("feroxia", TileFeroxia::new, set(UCBlocks.FEROXIA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileGoblet>> GOBLET = register("goblet", TileGoblet::new, set(UCBlocks.GOBLET));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSunBlock>> SUNTILE = register("suntile", TileSunBlock::new, set(UCBlocks.SUN_BLOCK));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSundial>> SUNDIAL = register("sundial", TileSundial::new, set(UCBlocks.SUN_DIAL));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFascino>> FASCINO = register("fascino", TileFascino::new, set(UCBlocks.FASCINO));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDigger>> QUARRY = register("digger", TileDigger::new, set(UCBlocks.QUARRY_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMillennium>> MILLENNIUM = register("millennium", TileMillennium::new, set(UCBlocks.MILLENNIUM_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBarrel>> BARREL = register("abstractbarrel", TileBarrel::new, set(UCBlocks.ABSTRACT_BARREL));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileWeatherflesia>> WEATHERFLESIA = register("weatherflesia", TileWeatherflesia::new, set(UCBlocks.WEATHERFLESIA));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCraftyPlant>> CRAFTYPLANT = register("craftyplant", TileCraftyPlant::new, set(UCBlocks.STALK));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileLacusia>> LACUSIA = register("lacusia", TileLacusia::new, set(UCBlocks.LACUSIA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileItero>> ITERO = register("itero", TileItero::new, set(UCBlocks.ITERO));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileExedo>> EXEDO = register("exedo", TileExedo::new, set(UCBlocks.EXEDO));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileWeepingBells>> WEEPINGBELLS = register("weepingbells", TileWeepingBells::new, set(UCBlocks.WEEPINGBELLS_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileIndustria>> INDUSTRIA = register("industria", TileIndustria::new, set(UCBlocks.INDUSTRIA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSucco>> SUCCO = register("succo", TileSucco::new, set(UCBlocks.SUCCO_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMusica>> MUSICA = register("musica", TileMusica::new, set(UCBlocks.MUSICA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileHarvestTrap>> HARVESTTRAP = register("harvesttrap", TileHarvestTrap::new, set(UCBlocks.HARVEST_TRAP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDyeius>> DYEIUS = register("dyeius", TileDyeius::new, set(UCBlocks.DYEIUS_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileInvisibilia>> INVISIBILIA = register("invisibilia", TileInvisibilia::new, set(UCBlocks.INVISIBILIA_CROP));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileInvisibiliaGlass>> INVISIBILIA_GLASS = register("invisibilia_glass", TileInvisibiliaGlass::new, set(UCBlocks.INVISIBILIA_GLASS));

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<Block[]> blocks) {

        return TILES.register(id, () -> BlockEntityType.Builder.of(factory, blocks.get()).build(null));
    }

        private static Supplier<Block[]> set(net.neoforged.neoforge.registries.DeferredBlock<?> blockGetter) {
            return () -> new Block[] { blockGetter.get() };
        }
}
