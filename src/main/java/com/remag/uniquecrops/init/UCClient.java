package com.remag.uniquecrops.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.gui.GuiBarrel;
import com.remag.uniquecrops.gui.GuiCraftyPlant;
import com.remag.uniquecrops.render.entity.RenderBattleCropEntity;
import com.remag.uniquecrops.render.entity.RenderLayerPants;
import com.remag.uniquecrops.render.entity.RenderNone;
import com.remag.uniquecrops.render.model.ModelBattleCrop;
import com.remag.uniquecrops.render.model.ModelCubeyThingy;
import com.remag.uniquecrops.render.model.ModelExedo;
import com.remag.uniquecrops.render.model.ModelSundial;
import com.remag.uniquecrops.render.particle.SparkFX;
import com.remag.uniquecrops.render.tile.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = UniqueCrops.MOD_ID, value = Dist.CLIENT)
public class UCClient {

    public static KeyMapping PIXEL_GLASSES;

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(UCClient::registerPropertyGetters);
        event.enqueueWork(() -> Minecraft.getInstance().particleEngine.register(
                UCParticles.SPARK.get(),
                SparkFX.Factory::new
        ));
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        PIXEL_GLASSES = new KeyMapping(
                "key.uniquecrops.pixelglasses",
                KeyConflictContext.IN_GAME,
                InputConstants.getKey(GLFW.GLFW_KEY_V, 0),
                "key.categories.uc"
        );
        event.register(PIXEL_GLASSES);
    }

    @SubscribeEvent
    public static void registerRenderTypes(RegisterNamedRenderTypesEvent event) {
        for (Block block : UCBlocks.CROPS)
            event.register(block.builtInRegistryHolder().key().location(), RenderType.cutout(), Sheets.cutoutBlockSheet());

        registerCutout(event,
                UCBlocks.INVISIBILIA_GLASS.get(),
                UCBlocks.HOURGLASS.get(),
                UCBlocks.FLYWOOD_SAPLING.get(),
                UCBlocks.LILY_ENDER.get(),
                UCBlocks.LILY_ICE.get(),
                UCBlocks.LILY_JUNGLE.get(),
                UCBlocks.LILY_LAVA.get(),
                UCBlocks.SUN_BLOCK.get(),
                UCBlocks.DEMO_CORD.get(),
                UCBlocks.ITERO.get(),
                UCBlocks.SANALIGHT.get(),
                UCBlocks.FLYWOOD_TRAPDOOR.get(),
                UCBlocks.ROSEWOOD_TRAPDOOR.get(),
                UCBlocks.DREAMCATCHER.get()
        );
    }

    private static void registerCutout(RegisterNamedRenderTypesEvent event, Block... blocks) {
        for (Block block : blocks)
            event.register(block.builtInRegistryHolder().key().location(), RenderType.cutout(), Sheets.cutoutBlockSheet());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(UCScreens.BARREL.get(), GuiBarrel::new);
        event.register(UCScreens.CRAFTYPLANT.get(), GuiCraftyPlant::new);
    }

    private static void registerPropertyGetters() {
        registerPropertyGetter(UCItems.DIAMONDS.get(), ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "diamonds"),
                (stack, world, entity, seed) -> UCDataUtils.getInt(stack, UCStrings.TAG_DIAMONDS, 0));
        registerPropertyGetter(UCItems.IMPACT_SHIELD.get(), ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "blocking"),
                (stack, world, entity, seed) -> (entity != null && entity.getUseItem() == stack) ? 1.0F : 0.0F);
    }

    private static void registerPropertyGetter(ItemLike item, ResourceLocation id, @SuppressWarnings("deprecation") ItemPropertyFunction prop) {
        ItemProperties.register(item.asItem(), id, prop);
    }

    /*@SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        UniqueCrops.LOGGER.info("Registering item colors");
        event.register((stack, tintIndex) -> {
            UniqueCrops.LOGGER.info("Tint index: " + tintIndex + " for item: " + stack.getItem());
            return tintIndex == 1 ? 0x845c28 : -1;
        }, UCItems.POTION_REVERSE.get());
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0xeef442 : -1, UCItems.POTION_ENNUI.get());
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0x00ccff : -1, UCItems.POTION_IGNORANCE.get());
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0x93C47D : -1, UCItems.POTION_ZOMBIFICATION.get());
        UniqueCrops.LOGGER.info("Registered item colors");
    }*/

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(UCTiles.ARTISIA.get(), RenderItemTile.Artisia::new);
        event.registerBlockEntityRenderer(UCTiles.WEATHERFLESIA.get(), RenderItemTile.Weatherflesia::new);
        event.registerBlockEntityRenderer(UCTiles.LACUSIA.get(), RenderItemTile.Lacusia::new);
        event.registerBlockEntityRenderer(UCTiles.SUNTILE.get(), RenderSunBlock::new);
        event.registerBlockEntityRenderer(UCTiles.SUNDIAL.get(), RenderSundial::new);
        event.registerBlockEntityRenderer(UCTiles.FASCINO.get(), RenderFascino::new);
        event.registerBlockEntityRenderer(UCTiles.ITERO.get(), RenderItero::new);
        event.registerBlockEntityRenderer(UCTiles.EXEDO.get(), RenderExedo::new);
        event.registerBlockEntityRenderer(UCTiles.SUCCO.get(), RenderSucco::new);
        event.registerBlockEntityRenderer(UCTiles.DYEIUS.get(), RenderDyeius::new);
        event.registerBlockEntityRenderer(UCTiles.INVISIBILIA.get(), RenderInvisibilia::new);
        event.registerBlockEntityRenderer(UCTiles.INVISIBILIA_GLASS.get(), RenderInvisibiliaGlass::new);

        event.registerEntityRenderer(UCEntities.BATTLE_CROP.get(), RenderBattleCropEntity::new);
        event.registerEntityRenderer(UCEntities.MOVING_CROP.get(), RenderNone::new);
        event.registerEntityRenderer(UCEntities.WEEPING_EYE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(UCEntities.THROWABLE_BOOK.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelBattleCrop.LAYER_LOCATION, () -> LayerDefinition.create(ModelBattleCrop.createBodyLayer(), 32, 32));
        event.registerLayerDefinition(ModelCubeyThingy.LAYER_LOCATION, () -> LayerDefinition.create(ModelCubeyThingy.createBodyLayer(), 16, 16));
        event.registerLayerDefinition(ModelExedo.LAYER_LOCATION, () -> LayerDefinition.create(ModelExedo.createBodyLayer(), 64, 64));
        event.registerLayerDefinition(ModelSundial.LAYER_LOCATION, () -> LayerDefinition.create(ModelSundial.createBodyLayer(), 64, 32));
    }

    @SubscribeEvent
    public static void registerExtraLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(s -> {
            if (event.getSkin(s) instanceof PlayerRenderer renderer)
                renderer.addLayer(new RenderLayerPants(renderer));
        });
    }
}
