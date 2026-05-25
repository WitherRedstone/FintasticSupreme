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
import com.chinaex123.fintastic_supreme.util.FSBaitConfig;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.ArrayList;

/**
 * 钓鱼钩实体 Mixin 类
 * <p>
 * 用于处理自定义钓钩在钓鱼结算时的特殊逻辑
 */
@Mixin(TideFishingHook.class)
public abstract class FishingHookMixin {

    @Shadow(remap = false)
    protected List<ItemStack> hookedItems;

    @Shadow(remap = false)
    protected ItemStack rod;

    @Shadow(remap = false)
    protected TideFishingHook.CatchType catchType;

    @Shadow(remap = false)
    private int nibble;

    /**
     * 修改 nibble 的值，延长或缩短挣扎时间
     * 在设置 nibble 之后立即修改
     */
    @Inject(
            method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void modifyStruggleTimeAtEnd(CallbackInfo ci) {
        ItemStack lineItem = CustomRodManager.getLine(this.rod);

        if (lineItem.is(FSItems.LIGHTWEIGHT_LINE.get())) {
            int extendedNibble = (int)(this.nibble * FSBaitConfig.LIGHTWEIGHT_LINE_ESCAPE_MULTIPLIER);
            this.nibble = Math.min(extendedNibble, 100);
        }
    }

    /**
     * 在钓鱼结算获取玩家对象之前注入逻辑
     * 主要功能：检测鱼竿上是否装备了“双倍掉落钓钩”，并根据配置概率复制钓到的物品
     *
     * @param perfectCatch 是否为完美收杆
     * @param ci           回调信息
     */
    @Inject(
            method = "retrieve(Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/li64/tide/registries/entities/misc/fishing/TideFishingHook;getPlayerOwner()Lnet/minecraft/world/entity/player/Player;",
                    ordinal = 0
            ),
            remap = false
    )
    private void onRetrieveBeforeGetPlayer(boolean perfectCatch, CallbackInfo ci) {
        // 仅处理钓到鱼的情况，忽略杂物或板条箱
        if (this.catchType != TideFishingHook.CatchType.FISH) return;
        // 如果没有钓到任何物品，直接返回
        if (this.hookedItems == null || this.hookedItems.isEmpty()) return;

        ItemStack hookItem = CustomRodManager.getHook(this.rod);
        ItemStack lineItem = CustomRodManager.getLine(this.rod);

        TideFishingHook tideHook = (TideFishingHook)(Object)this;
        List<ItemStack> currentDrops = new ArrayList<>(this.hookedItems);

        // 处理双倍掉落钓钩
        if (hookItem.is(FSItems.DOUBLE_CATCH_HOOK.get())) {
            if (tideHook.level().random.nextFloat() < FSBaitConfig.DOUBLE_CATCH_CHANCE) {
                for (ItemStack stack : this.hookedItems) {
                    currentDrops.add(stack.copy());
                }
            }
        }

        // 处理多重掉落钓钩
        if (hookItem.is(FSItems.MULTI_DROP_HOOK.get())) {
            if (tideHook.level().random.nextFloat() < FSBaitConfig.MULTI_DROP_CHANCE) {
                int extraCount = tideHook.level().random.nextInt(FSBaitConfig.MULTI_DROP_MAX_EXTRA) + 1;
                for (int i = 0; i < extraCount; i++) {
                    for (ItemStack stack : this.hookedItems) {
                        currentDrops.add(stack.copy());
                    }
                }
            }
        }

        // 处理幸运传导鱼线：根据玩家幸运值提供额外的掉落机会
        if (lineItem.is(FSItems.LUCKY_LINE.get())) {
            Player player = tideHook.getPlayerOwner();
            if (player != null) {
                float luck = player.getLuck();
                // 幸运值越高，触发额外掉落的概率越大
                if (luck > 0 && tideHook.level().random.nextFloat() < (luck * (float) FSBaitConfig.LUCKY_LINE_LUCK_TO_CHANCE)) {
                    for (ItemStack stack : this.hookedItems) {
                        currentDrops.add(stack.copy());
                    }
                }
            }
        }

        // 如果列表发生了变化，则更新 hookedItems
        if (currentDrops.size() != this.hookedItems.size()) {
            this.hookedItems = currentDrops;
        }
    }
}
