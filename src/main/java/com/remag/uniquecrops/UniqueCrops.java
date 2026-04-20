package com.remag.uniquecrops;

import com.mojang.logging.LogUtils;
import com.remag.uniquecrops.core.*;
import com.remag.uniquecrops.data.DataGenerators;
import com.remag.uniquecrops.events.UCEventHandlerCommon;
import com.remag.uniquecrops.init.*;
import com.remag.uniquecrops.items.DyedBonemealItem;
import com.remag.uniquecrops.items.curios.EmblemIronStomach;
import com.remag.uniquecrops.items.curios.EmblemScarab;
import com.remag.uniquecrops.network.UCPacketHandler;
import com.remag.uniquecrops.proxies.ClientProxy;
import com.remag.uniquecrops.proxies.CommonProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(UniqueCrops.MOD_ID)
public class UniqueCrops {

    public static final String MOD_ID = "uniquecrops";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static CommonProxy proxy = FMLEnvironment.dist == Dist.CLIENT ? new ClientProxy() : new CommonProxy();

    public UniqueCrops(IEventBus bus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.CLIENT, UCConfig.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, UCConfig.COMMON_SPEC);

        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        UCSounds.SOUNDS.register(bus);
        UCItems.ITEMS.register(bus);
        // bus.addGenericListener(Item.class, UCItems::registerItemsButNotReally);
        UCBlocks.BLOCKS.register(bus);
        UCTiles.TILES.register(bus);
        UCScreens.CONTAINERS.register(bus);
        UCEntities.ENTITIES.register(bus);
        bus.addListener(UCEntities::registerAttributes);
        UCParticles.PARTICLE_TYPES.register(bus);
        UCFeatures.FEATURE.register(bus);
        UCPotions.POTIONS.register(bus);
        UCRecipes.RECIPE_SERIALIZERS.register(bus);
        UCRecipes.RECIPE_TYPES.register(bus);
        UCTab.CREATIVE_MODE_TABS.register(bus);
        UCDataComponents.register(bus);
        bus.addListener(DataGenerators::gatherData);

        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(UCEventHandlerCommon::onBlockInteract);
		// forgeBus.addListener(UCEventHandlerCommon::attachItemCaps); // Removed: method does not exist
        forgeBus.addListener(UCEventHandlerCommon::updateAnvilCost);
        forgeBus.addListener(UCEventHandlerCommon::onBonemealEvent);
        forgeBus.addListener(UCEventHandlerCommon::jumpTele);
        forgeBus.addListener(UCEventHandlerCommon::addSeed);
        forgeBus.addListener(UCEventHandlerCommon::injectLoot);
        forgeBus.addListener(this::onServerStarting);
        forgeBus.addListener(this::registerCommands);
        bus.addListener(UCPacketHandler::register);
    }

    private void setup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            UCFeatures.registerOre();
            UCItems.registerCompostables();
        });

        DyeUtils.BONEMEAL_DYE.forEach((color, dbItem) -> {
            DispenserBlock.registerBehavior(dbItem, new OptionalDispenseItemBehavior() {
                protected ItemStack execute(BlockSource pBlockSource, ItemStack pItemStack) {
                    this.setSuccess(true);
                    Level level = pBlockSource.level();
                    BlockPos blockpos = pBlockSource.pos().relative(pBlockSource.state().getValue(DispenserBlock.FACING));
                    if (!DyedBonemealItem.dispenseOn(pItemStack, level, blockpos)) {
                        this.setSuccess(false);
                    }
                    return pItemStack;
                }
            });
        });

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

        event.getIMCStream().filter(msg -> msg.method().equals(UCStrings.BLACKLIST_EFFECT) && msg.messageSupplier().get() instanceof String)
                .forEach(s -> {
                    String value = s.messageSupplier().get().toString();
                    EmblemScarab.blacklistPotionEffect(value);
                });
    }

    private void onServerStarting(final ServerStartingEvent event) {
        UCWorldData.getInstance(event.getServer().overworld()).setDirty();
        EmblemIronStomach.init();
    }

    private void registerCommands(final RegisterCommandsEvent event) {

        UCCommands.register(event.getDispatcher());
    }

    private void registerBrews(final RegisterBrewingRecipesEvent event) {

        UCRecipes.registerBrews(event);
    }
}
