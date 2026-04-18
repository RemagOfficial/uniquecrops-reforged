package com.remag.uniquecrops.data.recipes;

import com.google.common.collect.Sets;
import com.google.gson.*;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.StalkBlock;
import com.remag.uniquecrops.blocks.supercrops.Weatherflesia;
import com.remag.uniquecrops.core.DyeUtils;
import com.remag.uniquecrops.core.enums.EnumDirectional;
import com.remag.uniquecrops.crafting.RecipeArtisia;
import com.remag.uniquecrops.crafting.RecipeEnchanter;
import com.remag.uniquecrops.crafting.RecipeHeater;
import com.remag.uniquecrops.crafting.RecipeHourglass;
import com.remag.uniquecrops.crafting.RecipeMultiblock;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class UCRecipeProvider extends RecipeProvider {
    private HolderLookup.Provider registries;

    public UCRecipeProvider(DataGenerator gen, CompletableFuture<HolderLookup.Provider> lookupProvider) {

        super(gen.getPackOutput(), lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer, HolderLookup.Provider holderLookup) {
        this.registries = holderLookup;
        buildRecipes(consumer);
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        // specialRecipe(consumer, UCRecipes.DISCOUNTBOOK_SERIALIZER.get());
        slabs(RecipeCategory.MISC, UCBlocks.FLYWOOD_SLAB.get(), UCBlocks.FLYWOOD_PLANKS.get()).save(consumer);
        slabs(RecipeCategory.MISC, UCBlocks.ROSEWOOD_SLAB.get(), UCBlocks.ROSEWOOD_PLANKS.get()).save(consumer);
        slabs(RecipeCategory.MISC, UCBlocks.RUINEDBRICKS_SLAB.get(), UCBlocks.RUINEDBRICKS.get()).save(consumer);
        slabs(RecipeCategory.MISC, UCBlocks.RUINEDBRICKSCARVED_SLAB.get(), UCBlocks.RUINEDBRICKSCARVED.get()).save(consumer);
        stairs(RecipeCategory.MISC, UCBlocks.FLYWOOD_STAIRS.get(), UCBlocks.FLYWOOD_PLANKS.get()).save(consumer);
        stairs(RecipeCategory.MISC, UCBlocks.ROSEWOOD_STAIRS.get(), UCBlocks.ROSEWOOD_PLANKS.get()).save(consumer);
        stairs(RecipeCategory.MISC, UCBlocks.RUINEDBRICKS_STAIRS.get(), UCBlocks.RUINEDBRICKS.get()).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.GLASSES_3D.get(), 1)
                .define('I', Items.IRON_INGOT)
                .define('B', Blocks.BLUE_STAINED_GLASS_PANE)
                .define('R', Blocks.RED_STAINED_GLASS_PANE)
                .define('W', Blocks.WHITE_WOOL)
                .pattern("I I")
                .pattern("I I")
                .pattern("BWR")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.ABSTRACT_BARREL.get(), 1)
                .define('A', UCItems.ABSTRACT.get())
                .define('C', Tags.Items.CHESTS)
                .pattern("AAA")
                .pattern("ACA")
                .pattern("AAA")
                .unlockedBy("has_item", has(UCItems.ABSTRACT.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.ANKH.get(), 1)
                .define('E', UCBlocks.SUN_BLOCK.get())
                .define('O', Blocks.OBSIDIAN)
                .pattern(" E ")
                .pattern("OOO")
                .pattern(" O ")
                .unlockedBy("has_item", has(UCBlocks.SUN_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.BATSTAFF.get(), 1)
                .define('L', Items.LEATHER)
                .define('S', Items.STICK)
                .define('E', UCItems.MILLENNIUMEYE.get())
                .pattern("LEL")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.BOOK_MULTIBLOCK.get(), 1)
                .define('D', UCItems.DYEIUS_SEED.get())
                .define('B', UCItems.BOOK_GUIDE.get())
                .pattern(" D ")
                .pattern("DBD")
                .pattern(" D ")
                .unlockedBy("has_item", has(UCItems.DYEIUS_SEED.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.SEVEN_LEAGUE_BOOTS.get(), 1)
                .define('S', Items.STRING)
                .define('E', UCItems.EMERADIC_DIAMOND.get())
                .define('L', UCItems.ENCHANTED_LEATHER.get())
                .define('B', Items.LEATHER_BOOTS)
                .pattern("SES")
                .pattern("LBL")
                .pattern("SLS")
                .unlockedBy("has_item", has(Items.LEATHER_BOOTS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.BUCKET_ROPE.get(), 1)
                .define('F', Tags.Items.FENCE_GATES_WOODEN)
                .define('R', UCItems.ESCAPEROPE.get())
                .define('B', Items.BUCKET)
                .pattern("FFF")
                .pattern(" R ")
                .pattern(" B ")
                .unlockedBy("has_item", has(UCItems.ESCAPEROPE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.CACTUS_BOOTS.get(), 1)
                .define('C', Blocks.CACTUS)
                .define('I', Items.IRON_BOOTS)
                .pattern("CIC")
                .pattern("C C")
                .unlockedBy("has_item", has(Blocks.CACTUS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.CACTUS_CHESTPLATE.get(), 1)
                .define('C', Blocks.CACTUS)
                .define('I', Items.IRON_CHESTPLATE)
                .pattern("C C")
                .pattern("CIC")
                .pattern("CCC")
                .unlockedBy("has_item", has(Blocks.CACTUS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.CACTUS_HELM.get(), 1)
                .define('C', Blocks.CACTUS)
                .define('I', Items.IRON_HELMET)
                .pattern("CCC")
                .pattern("CIC")
                .unlockedBy("has_item", has(Blocks.CACTUS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.CACTUS_LEGGINGS.get(), 1)
                .define('C', Blocks.CACTUS)
                .define('I', Items.IRON_LEGGINGS)
                .pattern("CCC")
                .pattern("CIC")
                .pattern("C C")
                .unlockedBy("has_item", has(Blocks.CACTUS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.CINDER_TORCH.get(), 4)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('X', UCItems.CINDERLEAF.get())
                .pattern("S")
                .pattern("X")
                .pattern("S")
                .unlockedBy("has_item", has(UCItems.CINDERLEAF.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.DRIED_THATCH.get(), 4)
                .define('H', Blocks.HAY_BLOCK)
                .pattern("HH")
                .pattern("HH")
                .unlockedBy("has_item", has(Blocks.HAY_BLOCK))
                .save(consumer);
        storage(RecipeCategory.MISC, UCBlocks.EGG_BASKET.get(), Items.EGG).save(consumer);
        shapeless(RecipeCategory.MISC, Items.EGG, UCBlocks.EGG_BASKET.get(), 9).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_BLACKSMITH.get(), 1)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('A', Blocks.ANVIL)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("IAI")
                .pattern("IDI")
                .pattern("IBI")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_BOOKWORM.get(), 1)
                .define('P', Items.POTATO)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .define('B', Items.ENCHANTED_BOOK)
                .pattern(" B ")
                .pattern("PDP")
                .pattern(" B ")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_DEFENSE.get(), 1)
                .define('A', Items.ARROW)
                .define('B', Items.BOW)
                .define('S', Items.SHIELD)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("ABA")
                .pattern("SDS")
                .pattern("ABA")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_FOOD.get(), 1)
                .define('P', Items.POISONOUS_POTATO)
                .pattern("PPP")
                .pattern("P P")
                .pattern("PPP")
                .unlockedBy("has_item", has(Items.POISONOUS_POTATO))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_IRONSTOMACH.get(), 1)
                .define('R', Items.COOKED_RABBIT)
                .define('C', Items.COOKED_CHICKEN)
                .define('S', Items.COOKED_BEEF)
                .define('P', Items.COOKED_PORKCHOP)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('e', Tags.Items.GEMS_EMERALD)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("SgP")
                .pattern("iDe")
                .pattern("RdC")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_LEAF.get(), 1)
                .define('L', ItemTags.LOGS)
                .define('l', ItemTags.LEAVES)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("lLl")
                .pattern("LDL")
                .pattern("lLl")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_MELEE.get(), 1)
                .define('d', Items.DIAMOND_SWORD)
                .define('g', Items.GOLDEN_SWORD)
                .define('i', Items.IRON_SWORD)
                .define('w', Items.WOODEN_SWORD)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern(" d ")
                .pattern("gDi")
                .pattern(" w ")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UCItems.EMBLEM_PACIFISM.get(), 1)
                .requires(UCItems.EMBLEM_DEFENSE.get())
                .requires(UCItems.EMBLEM_BLACKSMITH.get())
                .requires(UCItems.EMBLEM_IRONSTOMACH.get())
                .requires(UCItems.EMBLEM_LEAF.get())
                .requires(UCItems.EMBLEM_MELEE.get())
                .requires(UCItems.EMBLEM_POWERFIST.get())
                .requires(UCItems.EMBLEM_RAINBOW.get())
                .requires(UCItems.EMBLEM_SCARAB.get())
                .requires(UCItems.EMBLEM_TRANSFORMATION.get())
                .unlockedBy("has_item", has(UCItems.EMBLEM_DEFENSE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_POWERFIST.get(), 1)
                .define('p', Items.DIAMOND_PICKAXE)
                .define('b', Items.BLAZE_POWDER)
                .define('B', Items.BLAZE_ROD)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern(" p ")
                .pattern("BDB")
                .pattern(" b ")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_RAINBOW.get(), 1)
                .define('W', ItemTags.WOOL)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_SCARAB.get(), 1)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern("gGg")
                .pattern("GDG")
                .pattern("gGg")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_TRANSFORMATION.get(), 1)
                .define('p', UCItems.INVISIFEATHER.get())
                .define('f', Items.FEATHER)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .pattern(" p ")
                .pattern("fDf")
                .pattern(" f ")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMBLEM_WEIGHT.get(), 1)
                .define('S', Items.SNOWBALL)
                .define('O', Blocks.OBSIDIAN)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .define('B', Items.FIRE_CHARGE)
                .pattern("BSB")
                .pattern("ODO")
                .pattern("OOO")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        allAround(RecipeCategory.MISC, Items.ENDER_PEARL, Items.SNOWBALL, UCItems.LILYTWINE.get()).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.ENDERSNOOKER.get(), 1)
                .define('P', Items.ENDER_PEARL)
                .define('S', Items.STICK)
                .define('E', UCItems.LILYTWINE.get())
                .pattern("EPE")
                .pattern("PSP")
                .pattern("EPE")
                .unlockedBy("has_item", has(Items.ENDER_PEARL))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.BOOK_GUIDE.get(), 1)
                .define('P', Items.PUMPKIN_SEEDS)
                .define('B', Items.BOOK)
                .define('W', Items.WHEAT_SEEDS)
                .define('M', Items.MELON_SEEDS)
                .define('N', UCItems.NORMAL_SEED.get())
                .pattern(" N ")
                .pattern("WBM")
                .pattern(" P ")
                .unlockedBy("has_item", has(UCItems.ARTISIA_SEED.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PREGEM.get(), 1)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('N', UCItems.PRENUGGET.get())
                .pattern(" N ")
                .pattern("NDN")
                .pattern(" N ")
                .unlockedBy("has_item", has(UCItems.PRENUGGET.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.TIMEMEAL.get(), 4)
                .define('B', Items.BONE_MEAL)
                .define('C', Items.CLOCK)
                .pattern(" B ")
                .pattern("BCB")
                .pattern(" B ")
                .unlockedBy("has_item", has(Items.BONE_MEAL))
                .save(consumer);
        allAround(RecipeCategory.MISC, UCItems.INVISIFEATHER.get(), Items.FEATHER, UCItems.INVISITWINE.get()).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.WEEPINGEYE.get(), 1)
                .define('T', UCItems.WEEPINGTEAR.get())
                .define('E', Items.ENDER_EYE)
                .pattern(" T ")
                .pattern("TET")
                .pattern(" T ")
                .unlockedBy("has_item", has(UCItems.WEEPINGTEAR.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.BOOK_UPGRADE.get(), 1)
                .define('B', UCItems.BOOK_DISCOUNT.get())
                .define('F', UCItems.INVISIFEATHER.get())
                .pattern(" F ")
                .pattern("FBF")
                .pattern(" F ")
                .unlockedBy("has_item", has(UCItems.BOOK_DISCOUNT.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EGGUPGRADE.get(), 1)
                .define('E', Items.EGG)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('M', UCItems.MILLENNIUMEYE.get())
                .pattern("IEI")
                .pattern("EME")
                .pattern("IEI")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EASYBADGE.get(), 1)
                .define('Q', Blocks.QUARTZ_BLOCK)
                .define('E', UCItems.MILLENNIUMEYE.get())
                .define('G', Tags.Items.INGOTS_GOLD)
                .pattern("GEG")
                .pattern("EQE")
                .pattern("GEG")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.BOOK_EULA.get(), 1)
                .define('B', Items.BOOK)
                .define('L', UCItems.LEGALSTUFF.get())
                .pattern(" L ")
                .pattern("LBL")
                .pattern(" L ")
                .unlockedBy("has_item", has(UCItems.LEGALSTUFF.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.ESCAPEROPE.get(), 2)
                .define('E', UCItems.LILYTWINE.get())
                .define('I', UCItems.INVISITWINE.get())
                .pattern("EI")
                .pattern("IE")
                .pattern("EI")
                .unlockedBy("has_item", has(UCItems.LILYTWINE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.SUN_DIAL.get(), 1)
                .define('R', Items.REDSTONE)
                .define('C', Tags.Items.COBBLESTONES)
                .pattern("RCR")
                .pattern("CCC")
                .unlockedBy("has_item", has(Items.REDSTONE))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, UCItems.DIET_PILLS.get())
                .requires(UCItems.ABSTRACT.get())
                .requires(UCItems.ABSTRACT.get())
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy("has_item", has(UCItems.ABSTRACT.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, UCItems.EGGNOG.get())
                .requires(Items.EGG)
                .requires(Items.MILK_BUCKET)
                .requires(Items.SUGAR)
                .requires(Items.PAPER)
                .unlockedBy("has_item", has(Items.MILK_BUCKET))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.GOLDEN_BREAD.get(), 1)
                .define('R', UCItems.GOLDENRODS.get())
                .pattern("RRR")
                .unlockedBy("has_item", has(UCItems.GOLDENRODS.get()))
                .save(consumer);
        storage(RecipeCategory.MISC, UCItems.LARGE_PLUM.get(), UCItems.DIRIGIBLEPLUM.get()).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, UCItems.YOGURT.get())
                .requires(UCItems.BOILED_MILK.get())
                .requires(Items.BOWL)
                .requires(Items.BOWL)
                .requires(UCItems.DYEIUS_SEED.get())
                .unlockedBy("has_item", has(UCItems.BOILED_MILK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.GOBLET.get(), 1)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('R', Blocks.REDSTONE_BLOCK)
                .define('P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .pattern("g g")
                .pattern("gRg")
                .pattern(" P ")
                .unlockedBy("has_item", has(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.HANDMIRROR.get(), 1)
                .define('S', Items.STICK)
                .define('G', Tags.Items.GLASS_PANES)
                .define('E', UCItems.MILLENNIUMEYE.get())
                .pattern("  E")
                .pattern(" G ")
                .pattern("S  ")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.EMERADIC_DIAMOND.get(), 1)
                .define('D', UCItems.PREGEM.get())
                .define('E', Tags.Items.GEMS_EMERALD)
                .pattern("DED")
                .unlockedBy("has_item", has(UCItems.PREGEM.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.HARVEST_TRAP.get(), 1)
                .define('P', ItemTags.LOGS)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('D', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Blocks.IRON_BARS)
                .pattern("IDI")
                .pattern("SPS")
                .pattern("SPS")
                .unlockedBy("has_item", has(ItemTags.LOGS))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.HOURGLASS.get(), 1)
                .define('P', Tags.Items.GLASS_PANES)
                .define('D', UCItems.TIMEDUST.get())
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .pattern("DGD")
                .pattern("PDP")
                .pattern("DGD")
                .unlockedBy("has_item", has(UCItems.TIMEDUST.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.LILY_ICE.get(), 1)
                .define('P', Blocks.PACKED_ICE)
                .define('L', Blocks.LILY_PAD)
                .pattern(" P ")
                .pattern("PLP")
                .pattern(" P ")
                .unlockedBy("has_item", has(Blocks.LILY_PAD))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.IMPACT_SHIELD.get(), 1)
                .define('N', Items.NETHER_STAR)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('S', Items.SHIELD)
                .define('D', Blocks.ANCIENT_DEBRIS)
                .pattern("DSI")
                .pattern("SNS")
                .pattern("ISD")
                .unlockedBy("has_item", has(Items.SHIELD))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.IMPREGNATED_LEATHER.get(), 1)
                .define('C', Items.COAL)
                .define('L', Items.LEATHER)
                .pattern(" C ")
                .pattern("CLC")
                .pattern(" C ")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.INVISIBILIA_GLASS.get(), 4)
                .define('T', UCItems.INVISITWINE.get())
                .define('G', Tags.Items.GLASS_BLOCKS)
                .pattern("TGT")
                .pattern("GTG")
                .pattern("TGT")
                .unlockedBy("has_item", has(UCItems.INVISITWINE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.ITEM_MAGNET.get(), 1)
                .define('E', UCItems.FERROMAGNETICIRON.get())
                .pattern("EEE")
                .pattern("E E")
                .pattern("E E")
                .unlockedBy("has_item", has(UCItems.FERROMAGNETICIRON.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.LILY_JUNGLE.get(), 1)
                .define('P', Blocks.JUNGLE_LEAVES)
                .define('L', Blocks.LILY_PAD)
                .pattern(" P ")
                .pattern("PLP")
                .pattern(" P ")
                .unlockedBy("has_item", has(Blocks.LILY_PAD))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.LILY_LAVA.get(), 1)
                .define('C', UCItems.CINDERLEAF.get())
                .define('L', Blocks.LILY_PAD)
                .pattern(" C ")
                .pattern("CLC")
                .pattern(" C ")
                .unlockedBy("has_item", has(Blocks.LILY_PAD))
                .save(consumer);
        storage(RecipeCategory.MISC, UCBlocks.NORMIECRATE.get(), UCItems.NORMAL_SEED.get()).save(consumer);
        shapeless(RecipeCategory.MISC, UCItems.NORMAL_SEED.get(), UCBlocks.NORMIECRATE.get(), 9).save(consumer);
        shapeless(RecipeCategory.MISC, Items.DIAMOND, UCBlocks.OLDDIAMOND.get(), 9).save(consumer, "uniquecrops:old_diamonds");
        shapeless(RecipeCategory.MISC, Items.GOLD_INGOT, UCBlocks.OLDGOLD.get(), 9).save(consumer, "uniquecrops:old_gold_ingots");
        shapeless(RecipeCategory.MISC, Items.IRON_INGOT, UCBlocks.OLDIRON.get(), 9).save(consumer, "uniquecrops:old_iron_ingots");
        recipe(RecipeCategory.MISC, UCItems.PIXEL_BRUSH.get(), 1)
                .define('P', UCItems.PIXELS.get())
                .define('I', UCItems.INVISITWINE.get())
                .define('S', Items.STICK)
                .pattern(" S ")
                .pattern("ISI")
                .pattern("PPP")
                .unlockedBy("has_item", has(UCItems.PIXELS.get()))
                .save(consumer);
        allAround(RecipeCategory.MISC, UCItems.GLASSES_PIXELS.get(), UCItems.GLASSES_3D.get(), UCItems.PIXELS.get()).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PONCHO.get(), 1)
                .define('P', UCItems.INVISIFEATHER.get())
                .pattern("P P")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy("has_item", has(UCItems.INVISIFEATHER.get()))
                .save(consumer);
        shapeless(RecipeCategory.MISC, UCItems.ABSTRACT_SEED.get(), UCItems.ABSTRACT.get(), 1).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.ARTISIA_SEED.get(), 1)
                .define('S', UCItems.NORMAL_SEED.get())
                .define('C', Blocks.CRAFTING_TABLE)
                .pattern(" S ")
                .pattern("SCS")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.NORMAL_SEED.get()))
                .save(consumer);
        shapeless(RecipeCategory.MISC, UCItems.DIRIGIBLE_SEED.get(), UCItems.LARGE_PLUM.get(), 2).save(consumer);
        recipe(RecipeCategory.MISC, UCItems.SPIRITBAIT.get(), 1)
                .define('V', Blocks.VINE)
                .pattern("V V")
                .pattern(" V ")
                .pattern("V V")
                .unlockedBy("has_item", has(Blocks.VINE))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.SUN_BLOCK.get(), 1)
                .define('E', UCItems.SAVAGEESSENCE.get())
                .define('G', UCBlocks.INVISIBILIA_GLASS.get())
                .pattern("EGE")
                .pattern("GEG")
                .pattern("EGE")
                .unlockedBy("has_item", has(UCItems.SAVAGEESSENCE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.LILY_ENDER.get(), 1)
                .define('P', UCItems.LILYTWINE.get())
                .define('L', Blocks.LILY_PAD)
                .define('E', Items.ENDER_EYE)
                .pattern(" E ")
                .pattern("PLP")
                .pattern(" P ")
                .unlockedBy("has_item", has(Blocks.LILY_PAD))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.THUNDERPANTZ.get(), 1)
                .define('W', Blocks.BLUE_WOOL)
                .define('P', Items.LEATHER_LEGGINGS)
                .define('F', UCItems.INVISIFEATHER.get())
                .pattern("WWW")
                .pattern("WPW")
                .pattern("WFW")
                .unlockedBy("has_item", has(UCItems.INVISIFEATHER.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.TOTEMHEAD.get(), 1)
                .define('S', Items.STICK)
                .define('L', Blocks.LAPIS_BLOCK)
                .define('M', UCItems.MILLENNIUMEYE.get())
                .pattern("LLL")
                .pattern("LML")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.WILDWOOD_STAFF.get(), 1)
                .define('S', Items.STICK)
                .define('N', UCItems.ENDERSNOOKER.get())
                .define('E', UCItems.SAVAGEESSENCE.get())
                .pattern(" SE")
                .pattern(" NS")
                .pattern("S  ")
                .unlockedBy("has_item", has(UCItems.SAVAGEESSENCE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.RUINEDBRICKSCARVED.get(), 1)
                .define('R', UCBlocks.RUINEDBRICKS_SLAB.get())
                .pattern("R")
                .pattern("R")
                .unlockedBy("has_item", has(UCBlocks.RUINEDBRICKS_SLAB.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.OBTUSE_PLATFORM.get(), 1)
                .define('D', UCBlocks.DARK_BLOCK.get())
                .define('G', UCBlocks.INVISIBILIA_GLASS.get())
                .define('T', UCItems.LILYTWINE.get())
                .pattern("TGT")
                .pattern("GDG")
                .pattern("TGT")
                .unlockedBy("has_item", has(UCBlocks.DARK_BLOCK.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.FLYWOOD_TRAPDOOR.get(), 2)
                .define('P', UCBlocks.FLYWOOD_PLANKS.get())
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy("has_item", has(UCBlocks.FLYWOOD_PLANKS.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.ROSEWOOD_TRAPDOOR.get(), 2)
                .define('P', UCBlocks.ROSEWOOD_PLANKS.get())
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy("has_item", has(UCBlocks.ROSEWOOD_PLANKS.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PRECISION_PICK.get(), 1)
                .define('P', UCItems.PREGEM.get())
                .define('S', Items.STICK)
                .pattern("PPP")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.PREGEM.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PRECISION_AXE.get(), 1)
                .define('P', UCItems.PREGEM.get())
                .define('S', Items.STICK)
                .pattern("PP ")
                .pattern("PS ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.PREGEM.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PRECISION_SHOVEL.get(), 1)
                .define('P', UCItems.PREGEM.get())
                .define('S', Items.STICK)
                .pattern(" P ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.PREGEM.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PRECISION_SWORD.get(), 1)
                .define('P', UCItems.PREGEM.get())
                .define('S', Items.STICK)
                .pattern(" P ")
                .pattern(" P ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.PREGEM.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.PHANTOMSTAFF.get(), 1)
                .define('L', Items.LEATHER)
                .define('E', UCItems.MILLENNIUMEYE.get())
                .define('S', Items.STICK)
                .pattern("LEL")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy("has_item", has(UCItems.MILLENNIUMEYE.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCBlocks.DREAMCATCHER.get(), 1)
                .define('E', Items.EMERALD)
                .define('W', Items.COBWEB)
                .define('S', UCItems.PHANTOMSTAFF.get())
                .define('L', Items.LEATHER)
                .pattern("SLS")
                .pattern("LWL")
                .pattern("ELE")
                .unlockedBy("has_item", has(UCItems.PHANTOMSTAFF.get()))
                .save(consumer);
        recipe(RecipeCategory.MISC, UCItems.RUBIKS_CUBE.get(), 1)
                .define('C', UCItems.CUBEYTHINGY.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .unlockedBy("has_item", has(UCItems.CUBEYTHINGY.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UCItems.ZOMBIE_SLURRY.get())
                .requires(UCItems.PIXELS.get())
                .requires(Items.ROTTEN_FLESH)
                .unlockedBy("has_item", has(UCItems.PIXELS.get()))
                .save(consumer);

        // stonecutting

        stonecutting(consumer, UCBlocks.FLYWOOD_PLANKS.get(), UCBlocks.FLYWOOD_STAIRS.get(), 1);
        stonecutting(consumer, UCBlocks.ROSEWOOD_PLANKS.get(), UCBlocks.ROSEWOOD_STAIRS.get(), 1);
        stonecutting(consumer, UCBlocks.RUINEDBRICKS.get(), UCBlocks.RUINEDBRICKS_STAIRS.get(), 1);
        stonecutting(consumer, UCBlocks.ROSEWOOD_PLANKS.get(), UCBlocks.ROSEWOOD_SLAB.get(), 2);
        stonecutting(consumer, UCBlocks.FLYWOOD_PLANKS.get(), UCBlocks.FLYWOOD_SLAB.get(), 2);
        stonecutting(consumer, UCBlocks.RUINEDBRICKS.get(), UCBlocks.RUINEDBRICKS_SLAB.get(), 2);
        stonecutting(consumer, UCBlocks.RUINEDBRICKSCARVED.get(), UCBlocks.RUINEDBRICKSCARVED_SLAB.get(), 2);

        // smelting

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(UCItems.UNCOOKEDWAFFLE.get()), RecipeCategory.FOOD, UCItems.UNCOOKEDWAFFLE.get(), 0.35F, 200)
                .unlockedBy("hasItem", has(UCItems.UNCOOKEDWAFFLE.get()))
                .save(consumer, "uniquecrops:smelting/waffles");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.MILK_BUCKET), RecipeCategory.FOOD, UCItems.BOILED_MILK.get(), 0.1F, 200)
                .unlockedBy("hasItem", has(Items.MILK_BUCKET))
                .save(consumer, "uniquecrops:smelting/boiled_mlk");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(UCItems.UNCOOKEDWAFFLE.get()), RecipeCategory.FOOD, UCItems.UNCOOKEDWAFFLE.get(), 0.35F, 200)
                .unlockedBy("hasItem", has(UCItems.UNCOOKEDWAFFLE.get()))
                .save(consumer, "uniquecrops:campfire/waffles");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.MILK_BUCKET), RecipeCategory.FOOD, UCItems.BOILED_MILK.get(), 0.1F, 200)
                .unlockedBy("hasItem", has(Items.MILK_BUCKET))
                .save(consumer, "uniquecrops:campfire/boiled_mlk");

        // Artisia

        for (DyeColor dye : DyeColor.values())
            save(consumer, createArtisia(getDyeCraftingResult(dye.ordinal()), DyeUtils.DYE_BY_COLOR.get(DyeColor.byId(dye.ordinal())), UCItems.SAVAGEESSENCE.get(), UCItems.SAVAGEESSENCE.get()));

        DyeUtils.BONEMEAL_DYE.entrySet().forEach(dye -> {
            Item item = dye.getValue().asItem();
            Item dyeItem = DyeUtils.DYE_BY_COLOR.get(DyeColor.byId(dye.getKey().ordinal())).asItem();
            save(consumer, createArtisia(new ItemStack(item, 8), dyeItem, Items.BONE_MEAL, Items.BONE_MEAL));
        });

        save(consumer, createArtisia(UCItems.CINDERBELLA_SEED.get(), Items.SUGAR, Items.WHEAT_SEEDS, UCItems.NORMAL_SEED.get()));
        save(consumer, createArtisia(UCItems.COLLIS_SEED.get(), Items.SUGAR, UCItems.NORMAL_SEED.get(), UCItems.CINDERBELLA_SEED.get()));
        save(consumer, createArtisia(UCItems.DIRIGIBLE_SEED.get(), Items.SUGAR, Items.PUMPKIN_SEEDS, UCItems.COLLIS_SEED.get()));
        save(consumer, createArtisia(UCItems.ENDERLILY_SEED.get(), Items.ENDER_EYE, Items.ENDER_PEARL, UCItems.DIRIGIBLE_SEED.get()));
        save(consumer, createArtisia(UCItems.INVISIBILIA_SEED.get(), Items.SUGAR, Blocks.GLASS, UCItems.CINDERBELLA_SEED.get()));
        save(consumer, createArtisia(UCItems.KNOWLEDGE_SEED.get(), Items.SUGAR, Items.ENCHANTED_BOOK, UCItems.INVISIBILIA_SEED.get()));
        save(consumer, createArtisia(UCItems.MARYJANE_SEED.get(), Items.BLAZE_ROD, Items.BLAZE_POWDER, UCItems.COLLIS_SEED.get()));
        save(consumer, createArtisia(UCItems.MERLINIA_SEED.get(), Items.PUMPKIN_SEEDS, UCItems.TIMEMEAL.get(), UCItems.ENDERLILY_SEED.get()));
        save(consumer, createArtisia(UCItems.MILLENNIUM_SEED.get(), Items.CLOCK, Items.PUMPKIN_SEEDS, UCItems.MERLINIA_SEED.get()));
        save(consumer, createArtisia(UCItems.MUSICA_SEED.get(), Blocks.JUKEBOX, UCItems.NORMAL_SEED.get(), UCItems.MARYJANE_SEED.get()));
        save(consumer, createArtisia(UCItems.PRECISION_SEED.get(), Items.GOLD_NUGGET, UCItems.COLLIS_SEED.get(), UCItems.INVISIBILIA_SEED.get()));
        save(consumer, createArtisia(UCItems.WEEPINGBELLS_SEED.get(), Items.GHAST_TEAR, Items.MELON_SEEDS, UCItems.ENDERLILY_SEED.get()));
        save(consumer, createArtisia(UCItems.ABSTRACT_SEED.get(), Ingredient.of(Items.SUGAR_CANE), Ingredient.of(Blocks.TERRACOTTA), Ingredient.of(ItemTags.WOOL)));
        save(consumer, createArtisia(UCItems.COBBLONIA_SEED.get(), Blocks.COBBLESTONE, Blocks.STONE_BRICKS, UCItems.NORMAL_SEED.get()));
        save(consumer, createArtisia(UCItems.DYEIUS_SEED.get(), Ingredient.of(ItemTags.WOOL), Ingredient.of(Tags.Items.DYES), Ingredient.of(UCItems.ABSTRACT_SEED.get())));
        save(consumer, createArtisia(UCItems.EULA_SEED.get(), Items.PAPER, Items.BOOK, UCItems.COBBLONIA_SEED.get()));
        save(consumer, createArtisia(UCItems.FEROXIA_SEED.get(), Items.CLAY_BALL, UCItems.KNOWLEDGE_SEED.get(), UCItems.WEEPINGBELLS_SEED.get()));
        save(consumer, createArtisia(UCItems.WAFFLONIA_SEED.get(), Items.WHEAT_SEEDS, Items.BREAD, Items.SUGAR));
        save(consumer, createArtisia(UCItems.PIXELSIUS_SEED.get(), UCItems.WAFFLONIA_SEED.get(), Items.BLACK_DYE, Items.PAINTING));
        save(consumer, createArtisia(UCItems.DEVILSNARE_SEED.get(), UCItems.PIXELSIUS_SEED.get(), Blocks.DEAD_BUSH, Items.SWEET_BERRIES));
        save(consumer, createArtisia(UCItems.MALLEATORIS_SEED.get(), UCItems.PRECISION_SEED.get(), Blocks.ANVIL, Items.IRON_INGOT));
        save(consumer, createArtisia(UCItems.PETRAMIA_SEED.get(), UCItems.COBBLONIA_SEED.get(), Blocks.OBSIDIAN, Blocks.COBBLESTONE));
        save(consumer, createArtisia(UCItems.IMPERIA_SEED.get(), UCItems.PETRAMIA_SEED.get(), Blocks.END_ROD, Blocks.GLOWSTONE));
        save(consumer, createArtisia(UCItems.LACUSIA_SEED.get(), Blocks.HOPPER, UCItems.NORMAL_SEED.get(), Items.REDSTONE));
        save(consumer, createArtisia(UCItems.HEXIS_SEED.get(), UCItems.MALLEATORIS_SEED.get(), Items.WOODEN_SWORD, Items.LAPIS_LAZULI));
        save(consumer, createArtisia(UCItems.QUARRY_SEED.get(), Items.DIAMOND_PICKAXE, UCItems.PETRAMIA_SEED.get(), UCItems.HEXIS_SEED.get()));
        save(consumer, createArtisia(UCItems.INSTABILIS_SEED.get(), UCItems.PRECISION_SEED.get(), Blocks.TNT, Items.REDSTONE));
        save(consumer, createArtisia(UCItems.INDUSTRIA_SEED.get(), UCItems.INSTABILIS_SEED.get(), Blocks.SUNFLOWER, Items.POTATO));
        save(consumer, createArtisia(UCItems.SUCCO_SEED.get(), Items.POTION, Items.GHAST_TEAR, UCItems.INVISIBILIA_SEED.get()));
        save(consumer, createArtisia(UCItems.DONUTSTEEL_SEED.get(), Items.CAKE, Items.PURPLE_DYE, Blocks.WHITE_GLAZED_TERRACOTTA));
        save(consumer, createArtisia(UCItems.MAGNES_SEED.get(), UCItems.INDUSTRIA_SEED.get(), UCItems.BEAN_BATTERY.get(), Items.IRON_INGOT));

        // hourglass

        save(consumer, createHourglass("oldgrass", Blocks.GRASS_BLOCK, UCBlocks.OLDGRASS.get()));
        save(consumer, createHourglass("oldcobble", Blocks.COBBLESTONE, UCBlocks.OLDCOBBLE.get()));
        save(consumer, createHourglass("oldcobblemoss", Blocks.MOSSY_COBBLESTONE, UCBlocks.OLDCOBBLEMOSS.get()));
        save(consumer, createHourglass("oldgravel", Blocks.GRAVEL, UCBlocks.OLDGRAVEL.get()));
        save(consumer, createHourglass("oldbrick", Blocks.BRICKS, UCBlocks.OLDBRICK.get()));
        save(consumer, createHourglass("olddiamond", Blocks.DIAMOND_BLOCK, UCBlocks.OLDDIAMOND.get()));
        save(consumer, createHourglass("oldgold", Blocks.GOLD_BLOCK, UCBlocks.OLDGOLD.get()));
        save(consumer, createHourglass("oldiron", Blocks.IRON_BLOCK, UCBlocks.OLDIRON.get()));
        save(consumer, createHourglass("ruinedbricks", Blocks.STONE_BRICKS, UCBlocks.RUINEDBRICKS.get()));
        save(consumer, createHourglass("ruinedbrickscarved", Blocks.CHISELED_STONE_BRICKS, UCBlocks.RUINEDBRICKSCARVED.get()));
        save(consumer, createHourglass("flywood_sapling", Blocks.BIRCH_SAPLING, UCBlocks.FLYWOOD_SAPLING.get()));

        // enchanter

        save(consumer, createEnchanter(Enchantments.PROTECTION, 80, UCBlocks.DARK_BLOCK.get(), UCBlocks.DARK_BLOCK.get(), Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK));
        save(consumer, createEnchanter(Enchantments.FIRE_PROTECTION, 40, UCItems.CINDERLEAF.get(), Items.LAVA_BUCKET, UCItems.CINDERLEAF.get(), Blocks.BRICKS));
        save(consumer, createEnchanter(Enchantments.FEATHER_FALLING, 40, UCItems.INVISIFEATHER.get(), Items.FEATHER, UCItems.CACTUS_BOOTS.get()));
        save(consumer, createEnchanter(Enchantments.BLAST_PROTECTION, 40, Blocks.TNT, Items.GUNPOWDER, Items.FLINT_AND_STEEL, Blocks.OBSIDIAN));
        save(consumer, createEnchanter(Enchantments.PROJECTILE_PROTECTION, 60, Items.SHIELD, Items.ARROW, Items.ARMOR_STAND));
        save(consumer, createEnchanter(Enchantments.RESPIRATION, 20, Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS, Blocks.PRISMARINE));
        save(consumer, createEnchanter(Enchantments.AQUA_AFFINITY, 30, Items.DIAMOND_PICKAXE, Items.WATER_BUCKET, UCItems.TIMEDUST.get(), UCItems.TIMEDUST.get()));
        save(consumer, createEnchanter(Enchantments.THORNS, 20, UCItems.CACTUS_BOOTS.get(), UCItems.CACTUS_CHESTPLATE.get(), UCItems.CACTUS_HELM.get(), UCItems.CACTUS_LEGGINGS.get()));
        save(consumer, createEnchanter(Enchantments.DEPTH_STRIDER, 40, Blocks.SPONGE, Items.PRISMARINE_SHARD, Items.WATER_BUCKET));
        save(consumer, createEnchanter(Enchantments.FROST_WALKER, 40, Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE));
        save(consumer, createEnchanter(Enchantments.SHARPNESS, 90, Items.WOODEN_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD));
        save(consumer, createEnchanter(Enchantments.SMITE, 30, Ingredient.of(UCItems.ENCHANTED_LEATHER.get()), Ingredient.of(Items.LEATHER_CHESTPLATE), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Items.WATER_BUCKET)));
        save(consumer, createEnchanter(Enchantments.BANE_OF_ARTHROPODS, 30, Items.IRON_SWORD, Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE, Items.FERMENTED_SPIDER_EYE));
        save(consumer, createEnchanter(Enchantments.KNOCKBACK, 40, Blocks.PISTON, Blocks.PISTON, Items.IRON_SWORD));
        save(consumer, createEnchanter(Enchantments.FIRE_ASPECT, 50, Items.IRON_SWORD, UCItems.MARYJANE_SEED.get(), Items.BLAZE_POWDER, Items.BLAZE_ROD));
        save(consumer, createEnchanter(Enchantments.LOOTING, 70, UCItems.EMERADIC_DIAMOND.get(), Blocks.BONE_BLOCK, Blocks.TNT, Blocks.COBWEB, Blocks.SOUL_SOIL));
        save(consumer, createEnchanter(Enchantments.SWEEPING_EDGE, 70, Items.IRON_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD));
        save(consumer, createEnchanter(Enchantments.EFFICIENCY, 50, Blocks.REDSTONE_BLOCK, Items.REDSTONE, Blocks.REDSTONE_BLOCK, Blocks.REDSTONE_TORCH, Items.BEETROOT));
        save(consumer, createEnchanter(Enchantments.SILK_TOUCH, 40, Blocks.COBWEB, UCItems.PREGEM.get(), UCItems.PREGEM.get(), Items.SHEARS));
        save(consumer, createEnchanter(Enchantments.UNBREAKING, 50, Blocks.OBSIDIAN, Blocks.OBSIDIAN, UCItems.TIMEDUST.get(), Blocks.NETHER_BRICKS, Blocks.NETHER_BRICKS));
        save(consumer, createEnchanter(Enchantments.FORTUNE, 80, UCItems.SAVAGEESSENCE.get(), Items.DIAMOND_PICKAXE, UCItems.PREGEM.get()));
        save(consumer, createEnchanter(Enchantments.POWER, 60, Items.BOW, Items.ARROW, Items.CROSSBOW, Items.ARROW, UCItems.WEEPINGTEAR.get()));
        save(consumer, createEnchanter(Enchantments.PUNCH, 40, Blocks.PISTON, Blocks.STICKY_PISTON, Items.BOW));
        save(consumer, createEnchanter(Enchantments.FLAME, 40, Items.BOW, UCItems.MARYJANE_SEED.get(), Items.BLAZE_POWDER, Items.BLAZE_ROD, UCItems.MARYJANE_SEED.get()));
        save(consumer, createEnchanter(Enchantments.INFINITY, 10, Items.BOW, UCItems.DOGRESIDUE.get(), UCItems.DOGRESIDUE.get(), UCItems.DOGRESIDUE.get(), UCItems.DOGRESIDUE.get()));
        save(consumer, createEnchanter(Enchantments.MENDING, 90, Items.EXPERIENCE_BOTTLE, UCItems.MALLEATORIS_SEED.get()));

        // heater

        save(consumer, createHeater(Blocks.ICE, Blocks.PACKED_ICE));
        save(consumer, createHeater(UCItems.TERIYAKI.get(), Items.COOKED_CHICKEN));
        save(consumer, createHeater(UCBlocks.ROSEWOOD_PLANKS.get(), UCBlocks.FLYWOOD_PLANKS.get()));

        // multiblock

        save(consumer, createMultiblock("craftyplant",
                UCItems.WILDWOOD_STAFF.get(),
                20,
                new String[] {
                        "AAA",
                        "A0A",
                        "AAA"
                },
                new String[] {
                        "HNP",
                        "WCE",
                        "DSG"
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('A', new RecipeMultiblock.Slot(UCBlocks.ARTISIA_CROP.get()));
                    put('0', new RecipeMultiblock.Slot(UCBlocks.ARTISIA_CROP.get()));
                }},
                new HashMap<>() {{
                    put('C', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.DOWN)));
                    put('N', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.NORTH)));
                    put('S', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.SOUTH)));
                    put('W', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.WEST)));
                    put('E', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.EAST)));
                    put('H', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.NORTHWEST)));
                    put('P', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.NORTHEAST)));
                    put('D', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.SOUTHWEST)));
                    put('G', new RecipeMultiblock.Slot(UCBlocks.STALK.get().defaultBlockState().setValue(StalkBlock.STALKS, EnumDirectional.SOUTHEAST)));
                }}
        ));
        save(consumer, createMultiblock("fascino",
                UCItems.WILDWOOD_STAFF.get(),
                75,
                new String[] {
                        "L L",
                        " 0 ",
                        "L L"
                },
                new String[] {
                        "   ",
                        " D ",
                        "   "
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('0', new RecipeMultiblock.Slot(Blocks.ENCHANTING_TABLE));
                    put('L', new RecipeMultiblock.Slot(Blocks.LAPIS_BLOCK));
                }},
                new HashMap<>() {{
                    put('D', new RecipeMultiblock.Slot(UCBlocks.FASCINO.get()));
                }}
        ));
        save(consumer, createMultiblock("weatherflesia",
                UCItems.WILDWOOD_STAFF.get(),
                45,
                new String[] {
                        "TKT",
                        "K0K",
                        "TKT"
                },
                new String[] {
                        "HNP",
                        "WCE",
                        "DSG"
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('T', new RecipeMultiblock.Slot(UCBlocks.INDUSTRIA_CROP.get()));
                    put('K', new RecipeMultiblock.Slot(UCBlocks.KNOWLEDGE_CROP.get()));
                    put('0', new RecipeMultiblock.Slot(Blocks.BEACON));
                }},
                new HashMap<>() {{
                    put('C', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.UP)));
                    put('N', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.NORTH)));
                    put('S', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.SOUTH)));
                    put('W', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.WEST)));
                    put('E', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.EAST)));
                    put('H', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.NORTHWEST)));
                    put('P', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.NORTHEAST)));
                    put('D', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.SOUTHWEST)));
                    put('G', new RecipeMultiblock.Slot(UCBlocks.WEATHERFLESIA.get().defaultBlockState().setValue(Weatherflesia.RAFFLESIA, EnumDirectional.SOUTHEAST)));
                }}
        ));
        /*
        consumer.accept(createMultiblock("cropworldportal",
                UCItems.WILDWOOD_STAFF.get(),
                50,
                new String[] {
                        "CRRRC",
                        "R   R",
                        "R 0 R",
                        "R   R",
                        "CRRRC"
                },
                new String[] {
                        "CRRRC",
                        "RPPPR",
                        "RPPPR",
                        "RPPPR",
                        "CRRRC"
                },
                new Point(2, 2),
                new HashMap<>() {{
                    put('R', new RecipeMultiblock.Slot(UCBlocks.RUINEDBRICKS.get()));
                    put('C', new RecipeMultiblock.Slot(UCBlocks.RUINEDBRICKSCARVED.get()));
                    put('0', new RecipeMultiblock.Slot(Blocks.EMERALD_BLOCK));
                }},
                new HashMap<>() {{
                    put('R', new RecipeMultiblock.Slot(UCBlocks.RUINEDBRICKS.get()));
                    put('C', new RecipeMultiblock.Slot(UCBlocks.RUINEDBRICKSCARVED.get()));
                    put('P', new RecipeMultiblock.Slot(UCBlocks.CROP_PORTAL.get()));
                }}
        ));
        */
        save(consumer, createMultiblock("lignator",
                UCItems.EMERADIC_DIAMOND.get(),
                0,
                new String[] {
                        " I ",
                        "I0I",
                        " I "
                },
                new String[] {
                        "   ",
                        " L ",
                        "   "
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('0', new RecipeMultiblock.Slot(Blocks.MELON));
                    put('I', new RecipeMultiblock.Slot(Blocks.IRON_BARS));
                }},
                new HashMap<>() {{
                    put('L', new RecipeMultiblock.Slot(UCBlocks.LIGNATOR.get()));
                }}
        ));
        save(consumer, createMultiblock("exedo",
                UCItems.WILDWOOD_STAFF.get(),
                40,
                new String[] {
                        " D ",
                        "D0D",
                        " D "
                },
                new String[] {
                        "   ",
                        " E ",
                        "   "
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('D', new RecipeMultiblock.Slot(UCBlocks.DEVILSNARE_CROP.get()));
                    put('0', new RecipeMultiblock.Slot(UCBlocks.DEVILSNARE_CROP.get()));
                    put('W', new RecipeMultiblock.Slot(UCBlocks.WEEPINGBELLS_CROP.get()));
                }},
                new HashMap<>() {{
                    put('E', new RecipeMultiblock.Slot(UCBlocks.EXEDO.get()));
                }}
        ));
        save(consumer, createMultiblock("cocito",
                UCItems.WILDWOOD_STAFF.get(),
                40,
                new String[] {
                        "MDM",
                        "D0D",
                        "MDM"
                },
                new String[] {
                        "   ",
                        " C ",
                        "   "
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('D', new RecipeMultiblock.Slot(UCBlocks.INDUSTRIA_CROP.get()));
                    put('M', new RecipeMultiblock.Slot(UCBlocks.MARYJANE_CROP.get()));
                    put('0', new RecipeMultiblock.Slot(UCBlocks.MARYJANE_CROP.get()));
                }},
                new HashMap<>() {{
                    put('C', new RecipeMultiblock.Slot(UCBlocks.COCITO.get()));
                }}
        ));
        save(consumer, createMultiblock("itero",
                UCItems.WILDWOOD_STAFF.get(),
                80,
                new String[] {
                        "PRRRP",
                        "R   R",
                        "R 0 R",
                        "R   R",
                        "PRRRP"
                },
                new String[] {
                        "P   P",
                        "     ",
                        "  I  ",
                        "     ",
                        "P   P"
                },
                new Point(2, 2),
                new HashMap<>() {{
                    put('P', new RecipeMultiblock.Slot(Blocks.STONE_PRESSURE_PLATE));
                    put('0', new RecipeMultiblock.Slot(UCBlocks.PIXELSIUS_CROP.get()));
                    put('R', new RecipeMultiblock.Slot(Blocks.REDSTONE_WIRE));
                }},
                new HashMap<>() {{
                    put('P', new RecipeMultiblock.Slot(Blocks.STONE_PRESSURE_PLATE));
                    put('I', new RecipeMultiblock.Slot(UCBlocks.ITERO.get()));
                }}
        ));
        save(consumer, createMultiblock("sanalight",
                UCItems.EMERADIC_DIAMOND.get(),
                0,
                new String[] {
                        "GMG",
                        "M0M",
                        "GMG"
                },
                new String[] {
                        "   ",
                        " S ",
                        "   "
                },
                new Point(1, 1),
                new HashMap<>() {{
                    put('G', new RecipeMultiblock.Slot(Blocks.TORCH));
                    put('M', new RecipeMultiblock.Slot(Blocks.MOSS_CARPET));
                    put('0', new RecipeMultiblock.Slot(Blocks.SWEET_BERRY_BUSH));
                }},
                new HashMap<>() {{
                    put('S', new RecipeMultiblock.Slot(UCBlocks.SANALIGHT.get()));
                }}
        ));
    }
            private static void save(RecipeOutput consumer, GeneratedRecipe recipe) {

                consumer.accept(recipe.id(), recipe.recipe(), null);
            }

            private record GeneratedRecipe(ResourceLocation id, Recipe<?> recipe) {}

    /* private void specialRecipe(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends CraftingRecipe> serializer) {

        ResourceLocation name = ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer);
        if (name != null)
            SpecialRecipeBuilder.special(serializer).save(consumer, UniqueCrops.MOD_ID + ":dynamic/" + name.getPath());
    } */

    private ShapedRecipeBuilder recipe(RecipeCategory recipeCategory, ItemLike output, int count) {

        return ShapedRecipeBuilder.shaped(recipeCategory, output, count);
    }

    private ShapelessRecipeBuilder shapeless(RecipeCategory recipeCategory,ItemLike output, ItemLike input, int count) {

        return ShapelessRecipeBuilder.shapeless(recipeCategory, output, count).requires(input).unlockedBy("has_item", has(input));
    }

    private ShapedRecipeBuilder allAround(RecipeCategory recipeCategory, ItemLike output, ItemLike center, ItemLike surrounding) {

        return ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .define('A', center)
                .define('B', surrounding)
                .pattern("BBB").pattern("BAB").pattern("BBB")
                .unlockedBy("has_item", has(center));
    }

    private ShapedRecipeBuilder storage(RecipeCategory recipeCategory, ItemLike output, ItemLike input) {

        return ShapedRecipeBuilder.shaped(recipeCategory, output)
                .define('S', input)
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .unlockedBy("has_item", has(input));
    }

    private ShapedRecipeBuilder slabs(RecipeCategory recipeCategory, ItemLike output, ItemLike input) {

        return ShapedRecipeBuilder.shaped(recipeCategory, output, 6)
                .unlockedBy("has_item", has(input))
                .define('S', input)
                .pattern("SSS");
    }

    private ShapedRecipeBuilder stairs(RecipeCategory recipeCategory,ItemLike output, ItemLike input) {

        return ShapedRecipeBuilder.shaped(recipeCategory, output, 4)
                .unlockedBy("has_item", has(input))
                .define('S', input)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS");
    }

    private static ResourceLocation idFor(ItemLike a, ItemLike b) {

        ResourceLocation id1 = BuiltInRegistries.ITEM.getKey(a.asItem());
        ResourceLocation id2 = BuiltInRegistries.ITEM.getKey(b.asItem());
        return ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "stonecutting/" + id1.getPath() + "_to_" + id2.getPath());
    }

    private void stonecutting(RecipeOutput consumer, ItemLike input, ItemLike output, int count) {

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.MISC, output, count)
                .unlockedBy("has_item", has(input))
                .save(consumer, idFor(input, output));
    }

    private static ItemStack getDyeCraftingResult(int meta) {

        switch (meta) {
            default: return new ItemStack(Items.GHAST_TEAR);
            case 1: return new ItemStack(Items.FIRE_CHARGE, 8);
            case 2: return new ItemStack(Items.DRAGON_BREATH);
            case 3: return new ItemStack(Items.DIAMOND);
            case 4: return new ItemStack(Items.GOLD_INGOT);
            case 5: return new ItemStack(Items.SLIME_BALL, 4);
            case 6: return new ItemStack(Items.SADDLE);
            case 7: return new ItemStack(Blocks.CLAY, 8);
            case 8: return new ItemStack(Items.IRON_INGOT, 2);
            case 9: return new ItemStack(Blocks.CYAN_TERRACOTTA, 16);
            case 10: return new ItemStack(Items.CHORUS_FRUIT);
            case 11: return new ItemStack(Blocks.PRISMARINE, 8);
            case 12: return new ItemStack(Blocks.DIRT, 3);
            case 13: return new ItemStack(Items.EMERALD);
            case 14: return new ItemStack(Items.NETHER_WART);
            case 15: return new ItemStack(Items.WITHER_SKELETON_SKULL);
        }
    }

    private static ResourceLocation idFor(ResourceLocation name) {

        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "artisia/" + name.getPath());
    }

    private static GeneratedRecipe createArtisia(ItemStack output, ItemLike center, ItemLike edge, ItemLike corner) {

        Ingredient fromCenter = Ingredient.of(center);
        Ingredient fromEdge = Ingredient.of(edge);
        Ingredient fromCorner = Ingredient.of(corner);

        ResourceLocation id = idFor(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(output.getItem())));
        return new GeneratedRecipe(id, new RecipeArtisia(id, output, fromCorner, fromEdge, fromCorner, fromEdge, fromCenter, fromEdge, fromCorner, fromEdge, fromCorner));
    }

    private static GeneratedRecipe createArtisia(ItemLike item, ItemLike center, ItemLike edge, ItemLike corner) {

        Ingredient fromCenter = Ingredient.of(center);
        Ingredient fromEdge = Ingredient.of(edge);
        Ingredient fromCorner = Ingredient.of(corner);

        return createArtisia(item, fromCenter, fromEdge, fromCorner);
    }

    private static GeneratedRecipe createArtisia(ItemLike item, Ingredient center, Ingredient edge, Ingredient corner) {

        ResourceLocation id = idFor(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())));
        return new GeneratedRecipe(id, new RecipeArtisia(id, new ItemStack(item), corner, edge, corner, edge, center, edge, corner, edge, corner));
    }

    private static GeneratedRecipe createHourglass(String name, BlockState input, BlockState output) {

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "hourglass/" + name);
        return new GeneratedRecipe(id, new RecipeHourglass(id, input, output));
    }

    private static GeneratedRecipe createHourglass(String name, Block input, Block output) {

        return createHourglass(name, input.defaultBlockState(), output.defaultBlockState());
    }

    private GeneratedRecipe createEnchanter(ResourceKey<Enchantment> enchantmentKey, int cost, ItemLike... items) {

        return createEnchanter(enchantmentKey, cost, Ingredient.of(items));
    }

    private GeneratedRecipe createEnchanter(ResourceKey<Enchantment> enchantmentKey, int cost, Ingredient... ingredients) {
        var enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantmentKey);
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "enchanter/" + enchantmentKey.location().getPath());
        return new GeneratedRecipe(id, new RecipeEnchanter(id, enchantment, cost, ingredients));
    }

    private static GeneratedRecipe createHeater(ItemLike output, ItemLike input) {

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "heater/" + Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(output.asItem())).getPath());
        return new GeneratedRecipe(id, new RecipeHeater(id, new ItemStack(output.asItem()), new ItemStack(input.asItem())));
    }

    private static GeneratedRecipe createMultiblock(String name, Item item, int power, String[] shape, String[] shapeResult, Point origin, Map<Character, RecipeMultiblock.Slot> definition, Map<Character, RecipeMultiblock.Slot> definitionResult) {

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "multiblocks/" + name);
        return new GeneratedRecipe(id, new RecipeMultiblock(id, new ItemStack(item), power, shape, shapeResult, origin, definition, definitionResult));
    }

    public static class SerializerBlockState implements JsonDeserializer<Set<BlockState>>, JsonSerializer<Set<BlockState>> {

        @Override
        public Set<BlockState> deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {

            Set<BlockState> states = Sets.newHashSet();
            for (JsonElement entry : element.getAsJsonArray()) {
                String state = entry.getAsJsonPrimitive().getAsString();
                if (state.contains("[")) {
                    String[] split = state.split("\\[");
                    split[1] = split[1].substring(0, split[1].lastIndexOf("]")); // Make sure brackets are removed from state

                    Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(split[0]));
                    if (block == Blocks.AIR)
                        return Collections.singleton(block.defaultBlockState());

                    StateDefinition blockState = block.getStateDefinition();
                    BlockState returnState = block.defaultBlockState();

                    // Force our values into the state
                    String[] stateValues = split[1].split(","); // Splits up each value
                    for (String value : stateValues) {
//                        String[] valueSplit = value.split("=");
                        String[] valueSplit = value.split("-"); // split setValue - instead of = because minecraft's GSON escapes html characters
                        Property property = blockState.getProperty(valueSplit[0]);
                        if (property != null)
                            returnState = returnState.setValue(property, (Comparable) property.getValue(valueSplit[1]).get());
                    }
                    states.add(returnState);
                } else {
                    states.addAll(BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(state)).getStateDefinition().getPossibleStates());
                }
            }
            return states;
        }

        @Override
        public JsonElement serialize(Set<BlockState> src, Type type, JsonSerializationContext ctx) {

            JsonArray arr = new JsonArray();
            for (BlockState state : src)
                arr.add(BlockStateParser.serialize(state).replace("=", "-"));

            return ctx.serialize(arr);
        }
    }
}

