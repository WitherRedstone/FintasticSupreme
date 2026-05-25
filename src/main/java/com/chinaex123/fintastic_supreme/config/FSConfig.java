package com.chinaex123.fintastic_supreme.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FSConfig {

    public static final ModConfigSpec SPEC;

    // 显示相关配置
    public static final ModConfigSpec.BooleanValue SHOW_FISH_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_LOOT_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_CRATE_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_POSITION;
    public static final ModConfigSpec.BooleanValue SHOW_BIOME;

    // 钓鱼机制配置
    public static final ModConfigSpec.DoubleValue DOUBLE_CATCH_CHANCE;
    public static final ModConfigSpec.IntValue MULTI_DROP_MAX_EXTRA;
    public static final ModConfigSpec.DoubleValue MULTI_DROP_CHANCE;
    public static final ModConfigSpec.DoubleValue LUCKY_LINE_LUCK_TO_CHANCE;
    public static final ModConfigSpec.DoubleValue LIGHTWEIGHT_LINE_ESCAPE_MULTIPLIER;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("fish_finder");
        SHOW_POSITION = builder
                .comment("Whether to show the fishing spot coordinates")
                .define("show_position", true);
        SHOW_BIOME = builder
                .comment("Whether to show the fishing spot biome")
                .define("show_biome", true);
        SHOW_FISH_PROBABILITY = builder
                .comment("Whether to show fish probability")
                .define("show_fish_probability", true);
        SHOW_LOOT_PROBABILITY = builder
                .comment("Whether to show loot probability")
                .define("show_loot_probability", true);
        SHOW_CRATE_PROBABILITY = builder
                .comment("Whether to show crate probability")
                .define("show_crate_probability", true);
        builder.pop();

        builder.push("fishing_mechanics");
        DOUBLE_CATCH_CHANCE = builder
                .comment("Trigger chance of Double Catch Hook")
                .defineInRange("double_catch_chance", 0.15, 0.0, 1.0);
        MULTI_DROP_CHANCE = builder
                .comment("Trigger chance of Multi Drop Hook")
                .defineInRange("multi_drop_chance", 0.25, 0.0, 1.0);
        MULTI_DROP_MAX_EXTRA = builder
                .comment("Maximum extra drops of Multi Drop Hook")
                .defineInRange("multi_drop_max_extra", 3, 1, 10);
        LUCKY_LINE_LUCK_TO_CHANCE = builder
                .comment("Trigger chance per luck point for Lucky Line")
                .defineInRange("lucky_line_luck_to_chance", 0.1, 0.0, 1.0);
        LIGHTWEIGHT_LINE_ESCAPE_MULTIPLIER = builder
                .comment("Multiplier for fish escape time ( >1 = longer escape time)")
                .defineInRange("lightweight_line_escape_multiplier", 2.0, 0.1, 10.0);
        builder.pop();

        SPEC = builder.build();
    }
}