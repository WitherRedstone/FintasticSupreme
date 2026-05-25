package com.chinaex123.fintastic_supreme.data;

import com.chinaex123.fintastic_supreme.init.FSItems;
import com.li64.tide.registries.TideItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.FISH_FINDER.get())
                .pattern("CDC")
                .pattern("BAB")
                .pattern("CDC")
                .define('A', Items.CLOCK)
                .define('B', Tags.Items.GEMS_DIAMOND)
                .define('C', Tags.Items.GEMS_AMETHYST)
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_fish_finder_clock", has(Items.CLOCK))
                .unlockedBy("has_fish_finder_diamond", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_fish_finder_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_fish_finder_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.ADVANCED_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', TideItems.BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_advanced_bait_bait", has(TideItems.BAIT))
                .unlockedBy("has_advanced_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MASTER_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', FSItems.ADVANCED_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_master_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_master_bait_advanced_bait", has(FSItems.ADVANCED_BAIT))
                .unlockedBy("has_master_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.ADVANCED_LUCKY_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', TideItems.LUCKY_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_lucky_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_advanced_lucky_bait_lucky_bait", has(TideItems.LUCKY_BAIT))
                .unlockedBy("has_advanced_lucky_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MASTER_LUCKY_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', FSItems.ADVANCED_LUCKY_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_master_lucky_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_master_lucky_bait_advanced_lucky_bait", has(FSItems.ADVANCED_LUCKY_BAIT))
                .unlockedBy("has_master_lucky_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MIXED_BAIT.get())
                .pattern("AB ")
                .pattern("   ")
                .pattern("   ")
                .define('A', TideItems.BAIT)
                .define('B', TideItems.LUCKY_BAIT)
                .unlockedBy("has_mixed_bait_bait", has(TideItems.BAIT))
                .unlockedBy("has_mixed_bait_lucky_bait", has(TideItems.LUCKY_BAIT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.ADVANCED_MIXED_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', FSItems.MIXED_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_mixed_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_advanced_mixed_bait_mixed_bait", has(FSItems.MIXED_BAIT))
                .unlockedBy("has_advanced_mixed_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MASTER_MIXED_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', FSItems.ADVANCED_MIXED_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_master_mixed_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_master_mixed_bait_advanced_mixed_bait", has(FSItems.ADVANCED_MIXED_BAIT))
                .unlockedBy("has_master_mixed_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.ADVANCED_MAGNETIC_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', TideItems.MAGNETIC_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_magnetic_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_advanced_magnetic_bait_magnetic_bait", has(TideItems.MAGNETIC_BAIT))
                .unlockedBy("has_advanced_magnetic_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MASTER_MAGNETIC_BAIT.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('B', FSItems.ADVANCED_MAGNETIC_BAIT)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_master_magnetic_bait_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_master_magnetic_bait_advanced_magnetic_bait", has(FSItems.ADVANCED_MAGNETIC_BAIT))
                .unlockedBy("has_master_magnetic_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.DOUBLE_CATCH_HOOK.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.INGOTS_NETHERITE)
                .define('B', TideItems.FISHING_HOOK)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_magnetic_bait_netherite", has(Tags.Items.INGOTS_NETHERITE))
                .unlockedBy("has_advanced_magnetic_bait_fishing_hook", has(TideItems.FISHING_HOOK))
                .unlockedBy("has_advanced_magnetic_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.MULTI_DROP_HOOK.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Tags.Items.GEMS_EMERALD)
                .define('B', FSItems.DOUBLE_CATCH_HOOK)
                .define('C', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_advanced_magnetic_bait_emerald", has(Tags.Items.GEMS_EMERALD))
                .unlockedBy("has_advanced_magnetic_bait_multi_drop_hook", has(FSItems.MULTI_DROP_HOOK))
                .unlockedBy("has_advanced_magnetic_bait_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.LUCKY_LINE.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TideItems.FISHING_LINE)
                .define('B', Tags.Items.GEMS_EMERALD)
                .unlockedBy("has_lucky_line_fishing_line", has(TideItems.FISHING_LINE))
                .unlockedBy("has_lucky_line_emerald", has(Tags.Items.GEMS_EMERALD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FSItems.LIGHTWEIGHT_LINE.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TideItems.FISHING_LINE)
                .define('B', Items.FEATHER)
                .unlockedBy("has_lightweight_line_fishing_line", has(TideItems.FISHING_LINE))
                .unlockedBy("has_lightweight_line_feather", has(Items.FEATHER))
                .save(recipeOutput);
    }
}
