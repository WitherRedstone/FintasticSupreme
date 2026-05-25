/*
 * SPDX-License-Identifier: MPL-2.0
 * SPDX-FileCopyrightText: 2026 [ChinaEX123]
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on/modified from [Tide] (Copyright [Lightning64])
 */
package com.chinaex123.fintastic_supreme.mixin;

import com.chinaex123.fintastic_supreme.init.FSItems;
import com.li64.tide.data.rods.BaitData;
import com.li64.tide.util.BaitUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

/**
 * 鱼饵工具类 Mixin
 * 用于向 Tide 模组注册自定义鱼饵及其属性（速度、幸运等）
 */
@Mixin(BaitUtils.class)
public class BaitUtilsMixin {

    // 预定义鱼饵属性映射
    @Unique
    private static final Map<Item, BaitConfig> BAIT_CONFIGS = new HashMap<>();

    static {
        // 鱼饵
        BAIT_CONFIGS.put(FSItems.ADVANCED_BAIT.get(), new BaitConfig("fintastic_supreme:advanced_bait", 5, 0, 0));
        BAIT_CONFIGS.put(FSItems.MASTER_BAIT.get(), new BaitConfig("fintastic_supreme:master_bait", 10, 0, 0));
        // 幸运鱼饵
        BAIT_CONFIGS.put(FSItems.ADVANCED_LUCKY_BAIT.get(), new BaitConfig("fintastic_supreme:advanced_lucky_bait", 0, 5, 0));
        BAIT_CONFIGS.put(FSItems.MASTER_LUCKY_BAIT.get(), new BaitConfig("fintastic_supreme:master_lucky_bait", 0, 10, 0));
        // 组合鱼饵
        BAIT_CONFIGS.put(FSItems.MIXED_BAIT.get(), new BaitConfig("fintastic_supreme:mixed_bait", 2, 2, 0));
        BAIT_CONFIGS.put(FSItems.ADVANCED_MIXED_BAIT.get(), new BaitConfig("fintastic_supreme:advanced_mixed_bait", 5, 5, 0));
        BAIT_CONFIGS.put(FSItems.MASTER_MIXED_BAIT.get(), new BaitConfig("fintastic_supreme:master_mixed_bait", 10, 10, 0));
        // 磁力鱼饵
        BAIT_CONFIGS.put(FSItems.ADVANCED_MAGNETIC_BAIT.get(), new BaitConfig("fintastic_supreme:advanced_magnetic_bait", 2, 0, 50));
        BAIT_CONFIGS.put(FSItems.MASTER_MAGNETIC_BAIT.get(), new BaitConfig("fintastic_supreme:master_magnetic_bait", 5, 0, 75));
    }

    private record BaitConfig(String id, int speed, int luck, int crateChance) {}

    /**
     * 注入 isBait 方法，让 Tide 识别我们的自定义物品为合法鱼饵
     */
    @Inject(method = "isBait", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectIsBait(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (BAIT_CONFIGS.containsKey(stack.getItem())) {
            cir.setReturnValue(true);
        }
    }

    /**
     * 注入 getBaitData 方法，提供鱼饵的速度和幸运加成数据
     */
    @Inject(method = "getBaitData", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectGetBaitData(ItemStack stack, CallbackInfoReturnable<Optional<BaitData>> cir) {
        BaitConfig config = BAIT_CONFIGS.get(stack.getItem());
        if (config != null) {
            BaitData customBait = new BaitData(config.id(), config.speed(), config.luck());
            cir.setReturnValue(Optional.of(customBait));
        }
    }

    /**
     * 注入 getCrateChance 方法，处理增加板条箱掉落概率的逻辑
     */
    @Inject(method = "getCrateChance", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectGetCrateChance(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        BaitConfig config = BAIT_CONFIGS.get(stack.getItem());
        if (config != null && config.crateChance() > 0) {
            cir.setReturnValue(config.crateChance());
        }
    }

//    @Inject(method = "getDescriptionLines", at = @At("RETURN"), cancellable = true, remap = false)
//    private static void injectDescriptionLines(ItemStack bait, CallbackInfoReturnable<List<Component>> cir) {
//        if (bait.is(FSItems.DOUBLE_CATCH_HOOK.get())) {
//            List<Component> newLines = new ArrayList<>();
//            newLines.add(Component.empty());
//            newLines.add(Component.translatable("text.tide.bait_tooltip.prefix").withStyle(ChatFormatting.GRAY));
//
//            // 直接从配置类读取数值并转换为百分比
//            int percent = (int) (FSBaitConfig.DOUBLE_CATCH_CHANCE * 100);
//            newLines.add(Component.translatable("item.fintastic_supreme.double_catch_bait.tooltip", percent)
//                    .withStyle(ChatFormatting.BLUE));
//
//            cir.setReturnValue(newLines);
//        }
//    }
}
