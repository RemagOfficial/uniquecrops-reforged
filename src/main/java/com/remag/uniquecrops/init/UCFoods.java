package com.remag.uniquecrops.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class UCFoods {

    public static final FoodProperties LARGE_PLUM = new FoodProperties.Builder().nutrition(1).saturationModifier(0.6F).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 40), 1F).build();
    public static final FoodProperties TERIYAKI = new FoodProperties.Builder().nutrition(16).saturationModifier(1.0F).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 400), 1F).build();
    public static final FoodProperties STEVE_HEART = new FoodProperties.Builder().nutrition(0).saturationModifier(0F).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 4), 1F).build();
    public static final FoodProperties GOLDEN_BREAD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).effect(() -> new MobEffectInstance(MobEffects.LUCK, 2400), 1F).build();
    public static final FoodProperties DIET_PILLS = new FoodProperties.Builder().nutrition(-4).saturationModifier(0F).alwaysEdible().build();
    public static final FoodProperties WAFFLE = new FoodProperties.Builder().nutrition(8).saturationModifier(1.0F).alwaysEdible().build();
    public static final FoodProperties YOGURT = new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).build();
    public static final FoodProperties EGGNOG = new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
    public static final FoodProperties IGNORANCE_POTION = new FoodProperties.Builder().nutrition(0).saturationModifier(0F).alwaysEdible().effect(() -> new MobEffectInstance(UCPotions.IGNORANCE, 6000), 1F).build();
    public static final FoodProperties ENNUI_POTION = new FoodProperties.Builder().nutrition(0).saturationModifier(0F).alwaysEdible().effect(() -> new MobEffectInstance(UCPotions.ENNUI, 600), 1F).build();
    public static final FoodProperties REVERSE_POTION = new FoodProperties.Builder().nutrition(0).saturationModifier(0F).alwaysEdible().effect(() -> new MobEffectInstance(UCPotions.REVERSE, 1), 1F).build();
    public static final FoodProperties ZOMBIFICATION_POTION = new FoodProperties.Builder().nutrition(0).saturationModifier(0F).alwaysEdible().effect(() -> new MobEffectInstance(UCPotions.ZOMBIFICATION, 6000), 1F).build();

    public static final FoodProperties EDIBLE_NUGGET = new FoodProperties.Builder().nutrition(2).saturationModifier(1.2F).build();
    public static final FoodProperties EDIBLE_INGOT = new FoodProperties.Builder().nutrition(10).saturationModifier(0.65F).build();
    public static final FoodProperties EDIBLE_GEM = new FoodProperties.Builder().nutrition(14).saturationModifier(0.65F).build();
    public static final FoodProperties EDIBLE_DIAMOND = new FoodProperties.Builder().nutrition(14).saturationModifier(0.65F).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5000, 1), 1F).build();
    public static final FoodProperties EDIBLE_GOLD = new FoodProperties.Builder().nutrition(10).saturationModifier(0.65F).effect(() -> new MobEffectInstance(MobEffects.LUCK, 5000, 1), 1F).build();
    public static final FoodProperties EDIBLE_EMERALD = new FoodProperties.Builder().nutrition(14).saturationModifier(0.65F).effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 5000, 0), 1F).build();
}
