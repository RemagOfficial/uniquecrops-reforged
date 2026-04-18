package com.remag.uniquecrops.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class UCConfig {

    public static final Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {

        public final ModConfigSpec.IntValue guiWidth;
        public final ModConfigSpec.IntValue guiHeight;

        public Client(ModConfigSpec.Builder builder) {

            guiWidth = builder
                    .comment("Adjust placement of Wildwood staff GUI on the x axis.")
                    .defineInRange("guiWidth", -191, -1000, 1000);
            guiHeight = builder
                    .comment("Adjust placement of Wildwood staff GUI on the y axis.")
                    .defineInRange("guiHeight", -50, -1000, 1000);
        }
    }

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        public final ModConfigSpec.IntValue millenniumTime;
        public final ModConfigSpec.IntValue cubeCooldown;
        public final ModConfigSpec.IntValue energyPerTick;
        public final ModConfigSpec.BooleanValue convertObsidian;

        public final ModConfigSpec.BooleanValue moonPhase;
        public final ModConfigSpec.BooleanValue hasTorch;
        public final ModConfigSpec.BooleanValue likesDarkness;
        public final ModConfigSpec.BooleanValue dryFarmland;
        public final ModConfigSpec.BooleanValue underFarmland;
        public final ModConfigSpec.BooleanValue burningPlayer;
        public final ModConfigSpec.BooleanValue hellWorld;
        public final ModConfigSpec.BooleanValue likesLilypads;
        public final ModConfigSpec.BooleanValue likesHeights;
        public final ModConfigSpec.BooleanValue thirstyPlant;
        public final ModConfigSpec.BooleanValue hungryPlant;
        public final ModConfigSpec.BooleanValue likesChicken;
        public final ModConfigSpec.BooleanValue likesRedstone;
        public final ModConfigSpec.BooleanValue vampirePlant;
        public final ModConfigSpec.BooleanValue fullBrightness;
        public final ModConfigSpec.BooleanValue likesWarts;
        public final ModConfigSpec.BooleanValue likesCooking;
        public final ModConfigSpec.BooleanValue likesBrewing;
        public final ModConfigSpec.BooleanValue likesCheckers;
        public final ModConfigSpec.BooleanValue dontBonemeal;
        public final ModConfigSpec.BooleanValue selfSacrifice;

        public Common(ModConfigSpec.Builder builder) {

            millenniumTime = builder
                    .comment("Minimum time (in minutes) for Millennium crop to advance a stage.")
                    .defineInRange("millenniumTime", 1, 1, Integer.MAX_VALUE);
            cubeCooldown = builder
                    .comment("Cooldown time (in ticks) for rubik's cube between successful teleports.")
                    .defineInRange("cubeCooldown", 3000, 30, Integer.MAX_VALUE);
            energyPerTick = builder
                    .comment("Amount of energy gained per tick while the Industria crop grows.")
                    .defineInRange("energyPerTick", 20, 1, 200);
            convertObsidian = builder
                    .comment("Lets the Petramia crop convert obsidian instead of bedrock. Use if there are no bedrock nearby to convert.")
                    .define("convertObsidian", false);

            builder.comment("At least 7 of these must be true, not counting selfSacrifice.");
            builder.comment("NOTE: likesBrewing ignores this config and is always false");
            moonPhase = builder.define("moonPhase", true);
            hasTorch = builder.define("hasTorch", true);
            likesDarkness = builder.define("likesDarkness", true);
            dryFarmland = builder.define("dryFarmland", true);
            underFarmland = builder.define("underFarmland", true);
            burningPlayer = builder.define("burningPlayer", true);
            hellWorld = builder.define("hellWorld", true);
            likesLilypads = builder.define("likesLilypads", true);
            likesHeights = builder.define("likesHeights", true);
            thirstyPlant = builder.define("thirstyPlant", true);
            hungryPlant = builder.define("hungryPlant", true);
            likesChicken = builder.define("likesChicken", true);
            likesRedstone = builder.define("likesRedstone", true);
            vampirePlant = builder.define("vampirePlant", true);
            fullBrightness = builder.define("fullBrightness", true);
            likesWarts = builder.define("likesWarts", true);
            likesCooking = builder.define("likesCooking", true);
            likesBrewing = builder.define("likesBrewing", false);
            likesCheckers = builder.define("likesCheckers", true);
            dontBonemeal = builder.define("dontBonemeal", true);
            selfSacrifice = builder.define("selfSacrifice", true);
        }
    }
}
