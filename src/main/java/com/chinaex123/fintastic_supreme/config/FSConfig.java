package com.chinaex123.fintastic_supreme.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FSConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_FISH_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_LOOT_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_CRATE_PROBABILITY;
    public static final ModConfigSpec.BooleanValue SHOW_POSITION;
    public static final ModConfigSpec.BooleanValue SHOW_BIOME;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("fish_finder");

        SHOW_POSITION = builder
                .comment("是否显示钓点坐标")
                .define("show_position", true);

        SHOW_BIOME = builder
                .comment("是否显示钓点群系")
                .define("show_biome", true);

        SHOW_FISH_PROBABILITY = builder
                .comment("是否显示鱼类概率")
                .define("show_fish_probability", true);

        SHOW_LOOT_PROBABILITY = builder
                .comment("是否显示战利品概率")
                .define("show_loot_probability", true);

        SHOW_CRATE_PROBABILITY = builder
                .comment("是否显示箱子概率")
                .define("show_crate_probability", true);

        builder.pop();

        SPEC = builder.build();
    }
}
