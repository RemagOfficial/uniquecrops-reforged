package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.*;
import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.core.UCTab;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.items.*;
import com.remag.uniquecrops.items.base.*;
import com.remag.uniquecrops.items.curios.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class UCItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UniqueCrops.MOD_ID);

    /**
     * GENERAL
     */
    public static final DeferredItem<Item> BOOK_GUIDE = addToTab(ITEMS.register("book_guide", GuideBookItem::new));
    public static final DeferredItem<Item> BOOK_MULTIBLOCK = addToTab(ITEMS.register("book_multiblock", MultiblockBookItem::new));
    public static final DeferredItem<Item> BOOK_DISCOUNT = addToTab(ITEMS.register("book_discount", () -> new ItemBaseUC(unstackable())));
    public static final DeferredItem<Item> BOOK_UPGRADE = addToTab(ITEMS.register("book_upgrade", () -> new ItemBaseUC(unstackable())));
    public static final DeferredItem<Item> BOOK_EULA = addToTab(ITEMS.register("book_eula", EulaBookItem::new));
    public static final DeferredItem<Item> DIRIGIBLEPLUM = addToTab(ITEMS.register("dirigibleplum", DirigiblePlumItem::new));
    public static final DeferredItem<Item> CINDERLEAF = addToTab(ITEMS.register("cinderleaf", CinderleafItem::new));
    public static final DeferredItem<Item> TIMEDUST = addToTab(ITEMS.register("timedust", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> LILYTWINE = addToTab(ITEMS.register("lilytwine", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> GOLDENRODS = addToTab(ITEMS.register("goldenrods", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> PRENUGGET = addToTab(ITEMS.register("prenugget", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> PREGEM = addToTab(ITEMS.register("pregem", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> SAVAGEESSENCE = addToTab(ITEMS.register("savageessence", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> TIMEMEAL = addToTab(ITEMS.register("timemeal", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> INVISITWINE = addToTab(ITEMS.register("invisitwine", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> INVISIFEATHER = addToTab(ITEMS.register("invisifeather", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> SLIPPERGLASS = addToTab(ITEMS.register("slipperglass", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> WEEPINGTEAR = addToTab(ITEMS.register("weepingtear", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> WEEPINGEYE = addToTab(ITEMS.register("weepingeye", WeepingEyeItem::new));
    public static final DeferredItem<Item> MILLENNIUMEYE = addToTab(ITEMS.register("millenniumeye", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> EGGUPGRADE = addToTab(ITEMS.register("eggupgrade", EggUpgradeItem::new));
    public static final DeferredItem<Item> EASYBADGE = addToTab(ITEMS.register("easybadge", EasyBadgeItem::new));
    public static final DeferredItem<Item> DOGRESIDUE = addToTab(ITEMS.register("dogresidue", DogResidueItem::new));
    public static final DeferredItem<Item> ABSTRACT = addToTab(ITEMS.register("abstract", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> LEGALSTUFF = addToTab(ITEMS.register("legalstuff", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> PIXELS = addToTab(ITEMS.register("pixels", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> ESCAPEROPE = addToTab(ITEMS.register("escaperope", EscapeRopeItem::new));
    public static final DeferredItem<Item> CUBEYTHINGY = addToTab(ITEMS.register("cubeythingy", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> FERROMAGNETICIRON = addToTab(ITEMS.register("ferromagnetic_iron", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> SPIRITBAIT = addToTab(ITEMS.register("spirit_bait", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> ENDERSNOOKER = addToTab(ITEMS.register("endersnooker", EnderSnookerItem::new));
    public static final DeferredItem<Item> HANDMIRROR = addToTab(ITEMS.register("handmirror", HandMirrorItem::new));
    public static final DeferredItem<Item> BATSTAFF = addToTab(ITEMS.register("batstaff", StaffBatItem::new));
    public static final DeferredItem<Item> PHANTOMSTAFF = addToTab(ITEMS.register("phantomstaff", StaffPhantomItem::new));
    public static final DeferredItem<Item> BEAN_BATTERY = addToTab(ITEMS.register("bean_battery", BeanBatteryItem::new));
    public static final DeferredItem<Item> WILDWOOD_STAFF = addToTab(ITEMS.register("wildwood_staff", StaffWildwoodItem::new));
    public static final DeferredItem<Item> VAMPIRIC_OINTMENT = addToTab(ITEMS.register("vampiric_ointment", VampiricOintmentItem::new));
    public static final DeferredItem<Item> STEEL_DONUT = addToTab(ITEMS.register("steel_donut", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> ANKH = addToTab(ITEMS.register("ankh", AnkhItem::new));
    public static final DeferredItem<Item> GOODIE_BAG = addToTab(ITEMS.register("goodie_bag", GoodieBagItem::new));
    public static final DeferredItem<Item> EMERADIC_DIAMOND = addToTab(ITEMS.register("emeradic_diamond", EmeradicDiamondItem::new));
    public static final DeferredItem<Item> USELESS_LUMP = addToTab(ITEMS.register("useless_lump", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> DIAMONDS = addToTab(ITEMS.register("diamonds", DiamondBunchItem::new));
    public static final DeferredItem<Item> PIXEL_BRUSH = addToTab(ITEMS.register("pixel_brush", PixelBrushItem::new));
    public static final DeferredItem<Item> RUBIKS_CUBE = addToTab(ITEMS.register("rubiks_cube", RubiksCubeItem::new));
    public static final DeferredItem<Item> BOILED_MILK = addToTab(ITEMS.register("boiled_milk", () -> new ItemBaseUC()));
    public static final DeferredItem<Item> ITEM_MAGNET = addToTab(ITEMS.register("item_magnet", MagnetItem::new));
    public static final DeferredItem<Item> IMPREGNATED_LEATHER = addToTab(ITEMS.register("impregnated_leather", ImpregnatedLeatherItem::new));
    public static final DeferredItem<Item> ENCHANTED_LEATHER = addToTab(ITEMS.register("enchanted_leather", EnchantedLeatherItem::new));
    public static final DeferredItem<Item> ZOMBIE_SLURRY = addToTab(ITEMS.register("zombie_slurry", () -> new ItemBaseUC()));

    /**
     * FOOD & POTIONS
     */
    public static final DeferredItem<Item> LARGE_PLUM = registerFood("large_plum", UCFoods.LARGE_PLUM);
    public static final DeferredItem<Item> TERIYAKI = registerFood("teriyaki", UCFoods.TERIYAKI);
    public static final DeferredItem<Item> STEVE_HEART = registerFood("steveheart", UCFoods.STEVE_HEART);
    public static final DeferredItem<Item> GOLDEN_BREAD = registerFood("golden_bread", UCFoods.GOLDEN_BREAD);
    public static final DeferredItem<Item> DIET_PILLS = registerFood("diet_pills", UCFoods.DIET_PILLS);
    public static final DeferredItem<Item> UNCOOKEDWAFFLE = registerItem("uncookedwaffle", ItemBaseUC::new);
    public static final DeferredItem<Item> WAFFLE = registerFood("waffle", UCFoods.WAFFLE);
    public static final DeferredItem<Item> YOGURT = registerFood("yogurt", UCFoods.YOGURT);
    public static final DeferredItem<Item> EGGNOG = registerFood("eggnog", UCFoods.EGGNOG);
    public static final DeferredItem<Item> POTION_REVERSE = addToTab(ITEMS.register("potionreverse", () -> new ItemBaseUC(unstackable().food(UCFoods.REVERSE_POTION).craftRemainder(Items.GLASS_BOTTLE))));
    public static final DeferredItem<Item> POTION_ENNUI = addToTab(ITEMS.register("potionennui", () -> new ItemBaseUC(unstackable().food(UCFoods.ENNUI_POTION).craftRemainder(Items.GLASS_BOTTLE))));
    public static final DeferredItem<Item> POTION_IGNORANCE = addToTab(ITEMS.register("potionignorance", () -> new ItemBaseUC(unstackable().food(UCFoods.IGNORANCE_POTION).craftRemainder(Items.GLASS_BOTTLE))));
    public static final DeferredItem<Item> POTION_ZOMBIFICATION = addToTab(ITEMS.register("potionzombification", () -> new ItemBaseUC(unstackable().food(UCFoods.ZOMBIFICATION_POTION).craftRemainder(Items.GLASS_BOTTLE))));

    /**
     * GEAR
     */
    public static final DeferredItem<Item> GLASSES_3D = addToTab(ITEMS.register("glasses_3d", Glasses3DItem::new));
    public static final DeferredItem<Item> GLASSES_PIXELS = addToTab(ITEMS.register("glasses_pixels", GlassesPixelItem::new));
    public static final DeferredItem<Item> PONCHO = addToTab(ITEMS.register("poncho", PonchoItem::new));
    public static final DeferredItem<Item> GLASS_SLIPPERS = addToTab(ITEMS.register("slippers", () -> new ItemArmorUC(EnumArmorMaterial.SLIPPERS, ArmorItem.Type.BOOTS)));
    public static final DeferredItem<Item> THUNDERPANTZ = addToTab(ITEMS.register("thunderpantz", ThunderpantzItem::new));
    public static final DeferredItem<Item> CACTUS_HELM = addToTab(ITEMS.register("cactus_helm", () -> new ItemArmorUC(EnumArmorMaterial.CACTUS, ArmorItem.Type.HELMET)));
    public static final DeferredItem<Item> CACTUS_CHESTPLATE = addToTab(ITEMS.register("cactus_plate", () -> new ItemArmorUC(EnumArmorMaterial.CACTUS, ArmorItem.Type.CHESTPLATE)));
    public static final DeferredItem<Item> CACTUS_LEGGINGS = addToTab(ITEMS.register("cactus_leggings", () -> new ItemArmorUC(EnumArmorMaterial.CACTUS, ArmorItem.Type.LEGGINGS)));
    public static final DeferredItem<Item> CACTUS_BOOTS = addToTab(ITEMS.register("cactus_boots", () -> new ItemArmorUC(EnumArmorMaterial.CACTUS, ArmorItem.Type.BOOTS)));
    public static final DeferredItem<Item> SEVEN_LEAGUE_BOOTS = addToTab(ITEMS.register("seven_league_boots", LeagueBootsItem::new));
    public static final DeferredItem<Item> PRECISION_PICK = addToTab(ITEMS.register("precision_pick", PrecisionPickaxeItem::new));
    public static final DeferredItem<Item> PRECISION_AXE = addToTab(ITEMS.register("precision_axe", PrecisionAxeItem::new));
    public static final DeferredItem<Item> PRECISION_SHOVEL = addToTab(ITEMS.register("precision_shovel", PrecisionShovelItem::new));
    public static final DeferredItem<Item> PRECISION_SWORD = addToTab(ITEMS.register("precision_sword", PrecisionSwordItem::new));
    public static final DeferredItem<Item> PRECISION_HAMMER = addToTab(ITEMS.register("precision_hammer", PrecisionHammerItem::new));
    public static final DeferredItem<Item> IMPACT_SHIELD = addToTab(ITEMS.register("impact_shield", ImpactShieldItem::new));
    public static final DeferredItem<Item> BRASS_KNUCKLES = addToTab(ITEMS.register("brass_knuckles", BrassKnucklesItem::new));
    public static final DeferredItem<Item> ANCIENT_BOW = addToTab(ITEMS.register("oldbow", OldBow::new));

    /**
     * EMBLEMS
     */
    public static final DeferredItem<Item> EMBLEM_BLACKSMITH = addToTab(ITEMS.register("emblem_blacksmith", EmblemBlacksmith::new));
    public static final DeferredItem<Item> EMBLEM_BOOKWORM = addToTab(ITEMS.register("emblem_bookworm", EmblemBookworm::new));
    public static final DeferredItem<Item> EMBLEM_DEFENSE = addToTab(ITEMS.register("emblem_defense", EmblemDefense::new));
    public static final DeferredItem<Item> EMBLEM_FOOD = addToTab(ITEMS.register("emblem_food", EmblemFood::new));
    public static final DeferredItem<Item> EMBLEM_IRONSTOMACH = addToTab(ITEMS.register("emblem_ironstomach", EmblemIronStomach::new));
    public static final DeferredItem<Item> EMBLEM_LEAF = addToTab(ITEMS.register("emblem_leaf", EmblemLeaf::new));
    public static final DeferredItem<Item> EMBLEM_MELEE = addToTab(ITEMS.register("emblem_melee", EmblemMelee::new));
    public static final DeferredItem<Item> EMBLEM_PACIFISM = addToTab(ITEMS.register("emblem_pacifism", EmblemPacifism::new));
    public static final DeferredItem<Item> EMBLEM_POWERFIST = addToTab(ITEMS.register("emblem_powerfist", EmblemPowerfist::new));
    public static final DeferredItem<Item> EMBLEM_RAINBOW = addToTab(ITEMS.register("emblem_rainbow", EmblemRainbow::new));
    public static final DeferredItem<Item> EMBLEM_SCARAB = addToTab(ITEMS.register("emblem_scarab", EmblemScarab::new));
    public static final DeferredItem<Item> EMBLEM_TRANSFORMATION = addToTab(ITEMS.register("emblem_transformation", EmblemTransformation::new));
    public static final DeferredItem<Item> EMBLEM_WEIGHT = addToTab(ITEMS.register("emblem_weight", EmblemWeight::new));

    /**
     * MUSIC DISCS
     */
    public static final DeferredItem<Item> RECORD_FARAWAY = addToTab(ITEMS.register("record_faraway", () -> new ItemRecordUC(UCSounds.FAR_AWAY)));
    public static final DeferredItem<Item> RECORD_NEONSIGNS = addToTab(ITEMS.register("record_neonsigns", () -> new ItemRecordUC(UCSounds.NEON_SIGNS)));
    public static final DeferredItem<Item> RECORD_SIMPLY = addToTab(ITEMS.register("record_simply", () -> new ItemRecordUC(UCSounds.SIMPLY)));
    public static final DeferredItem<Item> RECORD_TAXI = addToTab(ITEMS.register("record_taxi", () -> new ItemRecordUC(UCSounds.TAXI)));

    /**
     * SEEDS
     */
    public static final DeferredItem<BlockItem> ABSTRACT_SEED = registerSeed("abstract", UCBlocks.ABSTRACT_CROP);
    public static final DeferredItem<BlockItem> ADVENTUS_SEED = registerSeed("adventus", UCBlocks.ADVENTUS_CROP);
    public static final DeferredItem<BlockItem> ARTISIA_SEED = registerSeed("artisia", UCBlocks.ARTISIA_CROP);
    public static final DeferredItem<BlockItem> BLESSED_SEED = registerSeed("blessed", UCBlocks.HOLY_CROP);
    public static final DeferredItem<BlockItem> CINDERBELLA_SEED = registerSeed("cinderbella", UCBlocks.CINDERBELLA_CROP);
    public static final DeferredItem<BlockItem> COLLIS_SEED = registerSeed("collis", UCBlocks.COLLIS_CROP);
    public static final DeferredItem<BlockItem> COBBLONIA_SEED = registerSeed("cobblonia", UCBlocks.COBBLONIA_CROP);
    public static final DeferredItem<BlockItem> DEVILSNARE_SEED = registerSeed("devilsnare", UCBlocks.DEVILSNARE_CROP);
    public static final DeferredItem<BlockItem> DIRIGIBLE_SEED = registerSeed("dirigible", UCBlocks.DIRIGIBLE_CROP);
    public static final DeferredItem<BlockItem> DONUTSTEEL_SEED = registerSeed("donutsteel", UCBlocks.DONUTSTEEL_CROP);
    public static final DeferredItem<BlockItem> DYEIUS_SEED = registerSeed("dyeius", UCBlocks.DYEIUS_CROP);
    public static final DeferredItem<BlockItem> ENDERLILY_SEED = registerSeed("enderlily", UCBlocks.ENDERLILY_CROP);
    public static final DeferredItem<BlockItem> EULA_SEED = registerSeed("eula", UCBlocks.EULA_CROP);
    public static final DeferredItem<BlockItem> FEROXIA_SEED = registerSeed("feroxia", UCBlocks.FEROXIA_CROP);
    public static final DeferredItem<BlockItem> HEXIS_SEED = registerSeed("hexis", UCBlocks.HEXIS_CROP);
    public static final DeferredItem<BlockItem> IMPERIA_SEED = registerSeed("imperia", UCBlocks.IMPERIA_CROP);
    public static final DeferredItem<BlockItem> INDUSTRIA_SEED = registerSeed("industria", UCBlocks.INDUSTRIA_CROP);
    public static final DeferredItem<BlockItem> INSTABILIS_SEED = registerSeed("instabilis", UCBlocks.INSTABILIS_CROP);
    public static final DeferredItem<BlockItem> INVISIBILIA_SEED = registerSeed("invisibilia", UCBlocks.INVISIBILIA_CROP);
    public static final DeferredItem<BlockItem> KNOWLEDGE_SEED = registerSeed("knowledge", UCBlocks.KNOWLEDGE_CROP);
    public static final DeferredItem<BlockItem> LACUSIA_SEED = registerSeed("lacusia", UCBlocks.LACUSIA_CROP);
    public static final DeferredItem<BlockItem> MAGNES_SEED = registerSeed("magnes", UCBlocks.MAGNES_CROP);
    public static final DeferredItem<BlockItem> MALLEATORIS_SEED = registerSeed("malleatoris", UCBlocks.MALLEATORIS_CROP);
    public static final DeferredItem<BlockItem> MARYJANE_SEED = registerSeed("maryjane", UCBlocks.MARYJANE_CROP);
    public static final DeferredItem<BlockItem> MERLINIA_SEED = registerSeed("merlinia", UCBlocks.MERLINIA_CROP);
    public static final DeferredItem<BlockItem> MILLENNIUM_SEED = registerSeed("millennium", UCBlocks.MILLENNIUM_CROP);
    public static final DeferredItem<BlockItem> MUSICA_SEED = registerSeed("musica", UCBlocks.MUSICA_CROP);
    public static final DeferredItem<BlockItem> NORMAL_SEED = registerSeed("normal", UCBlocks.NORMAL_CROP);
    public static final DeferredItem<BlockItem> PETRAMIA_SEED = registerSeed("petramia", UCBlocks.PETRAMIA_CROP);
    public static final DeferredItem<BlockItem> PIXELSIUS_SEED = registerSeed("pixelsius", UCBlocks.PIXELSIUS_CROP);
    public static final DeferredItem<BlockItem> PRECISION_SEED = registerSeed("precision", UCBlocks.PRECISION_CROP);
    public static final DeferredItem<BlockItem> QUARRY_SEED = registerSeed("quarry", UCBlocks.QUARRY_CROP);
    public static final DeferredItem<BlockItem> SUCCO_SEED = registerSeed("succo", UCBlocks.SUCCO_CROP);
    public static final DeferredItem<BlockItem> WAFFLONIA_SEED = registerSeed("wafflonia", UCBlocks.WAFFLONIA_CROP);
    public static final DeferredItem<BlockItem> WEEPINGBELLS_SEED = registerSeed("weepingbells", UCBlocks.WEEPINGBELLS_CROP);

    /**
     * DYED BONEMEALS
     */
    public static final DeferredItem<Item> WHITE_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.white", DyedBonemealItem::new));
    public static final DeferredItem<Item> ORANGE_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.orange", DyedBonemealItem::new));
    public static final DeferredItem<Item> MAGENTA_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.magenta", DyedBonemealItem::new));
    public static final DeferredItem<Item> LIGHTBLUE_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.light_blue", DyedBonemealItem::new));
    public static final DeferredItem<Item> YELLOW_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.yellow", DyedBonemealItem::new));
    public static final DeferredItem<Item> LIME_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.lime", DyedBonemealItem::new));
    public static final DeferredItem<Item> PINK_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.pink", DyedBonemealItem::new));
    public static final DeferredItem<Item> GRAY_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.gray", DyedBonemealItem::new));
    public static final DeferredItem<Item> SILVER_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.silver", DyedBonemealItem::new));
    public static final DeferredItem<Item> CYAN_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.cyan", DyedBonemealItem::new));
    public static final DeferredItem<Item> PURPLE_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.purple", DyedBonemealItem::new));
    public static final DeferredItem<Item> BLUE_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.blue", DyedBonemealItem::new));
    public static final DeferredItem<Item> BROWN_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.brown", DyedBonemealItem::new));
    public static final DeferredItem<Item> GREEN_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.green", DyedBonemealItem::new));
    public static final DeferredItem<Item> RED_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.red", DyedBonemealItem::new));
    public static final DeferredItem<Item> BLACK_BONEMEAL = addToTab(ITEMS.register("dyedbonemeal.black", DyedBonemealItem::new));

    /**
     * DUMMIES
     */
    public static final DeferredItem<Item> DUMMY_ARTISIA = addToTab(ITEMS.register("dummy_artisia", ItemDummyUC::new));
    public static final DeferredItem<Item> DUMMY_HEATER = addToTab(ITEMS.register("dummy_heater", ItemDummyUC::new));
    public static final DeferredItem<Item> DUMMY_FASCINO = addToTab(ITEMS.register("dummy_fascino", ItemRenderUC::new));

    public static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<I> supplier) {

        return addToTab(ITEMS.register(name, supplier));
    }

    public static DeferredItem<Item> registerFood(String name, FoodProperties food) {

        return addToTab(ITEMS.register(name, () -> new Item(new Item.Properties().food(food))));
    }

    @SuppressWarnings("unchecked")
    public static DeferredItem<BlockItem> registerSeed(String name, Object cropHolder) {

        return addToTab(ITEMS.register("seed" + name, () -> new ItemSeedsUC(((Supplier<? extends BaseCropsBlock>) cropHolder).get())));
    }

    private static <T extends ItemLike, H extends Supplier<T>> H addToTab(H itemLike) {

        UCTab.UNIQUECROPS_TABS.add(itemLike);
        return itemLike;
    }

    public static Item.Properties noTab() {

        return new Item.Properties();
    }

    public static Item.Properties defaultBuilder() {

        return new Item.Properties();
    }

    public static Item.Properties unstackable() {

        return defaultBuilder().stacksTo(1);
    }

    /** custom recipes start here */
    public static final RecipeType<IArtisiaRecipe> ARTISIA_TYPE = new ModRecipeType<>();
    public static final RecipeType<IHourglassRecipe> HOURGLASS_TYPE = new ModRecipeType<>();
    public static final RecipeType<IEnchanterRecipe> ENCHANTER_TYPE = new ModRecipeType<>();
    public static final RecipeType<IHeaterRecipe> HEATER_TYPE = new ModRecipeType<>();
    public static final RecipeType<IMultiblockRecipe> MULTIBLOCK_TYPE = new ModRecipeType<>();

    public static void registerItemsButNotReally(RegisterEvent event) {

        event.register(Registries.RECIPE_TYPE, helper -> {
            helper.register(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "artisia"), ARTISIA_TYPE);
            helper.register(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "hourglass"), HOURGLASS_TYPE);
            helper.register(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "enchanter"), ENCHANTER_TYPE);
            helper.register(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "heater"), HEATER_TYPE);
            helper.register(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "multiblock"), MULTIBLOCK_TYPE);
        });
    }

    public static void registerCompostables( ) {
        // 30% chance, seeds (leave out Easter Eggs, abstract and merlinia
        ComposterBlock.COMPOSTABLES.put(UCItems.ARTISIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.CINDERBELLA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.COLLIS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.COBBLONIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.DEVILSNARE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.DIRIGIBLE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.DONUTSTEEL_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.DYEIUS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.ENDERLILY_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.EULA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.FEROXIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.HEXIS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.IMPERIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.INDUSTRIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.INSTABILIS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.INVISIBILIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.KNOWLEDGE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.LACUSIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.MAGNES_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.MALLEATORIS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.MARYJANE_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.MILLENNIUM_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.MUSICA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.NORMAL_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.PETRAMIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.PIXELSIUS_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.PRECISION_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.QUARRY_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.SUCCO_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.WAFFLONIA_SEED.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(UCItems.WEEPINGBELLS_SEED.get(), 0.3F);

        // 50% chance, organic outputs
        ComposterBlock.COMPOSTABLES.put(UCItems.DIRIGIBLEPLUM.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(UCItems.GOLDENRODS.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(UCItems.UNCOOKEDWAFFLE.get(), 0.5F);
    }

    private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {

        @Override
        public String toString() {

            return Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(this)).toString();
        }
    }
}
