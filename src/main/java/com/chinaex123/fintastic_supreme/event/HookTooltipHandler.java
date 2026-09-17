package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.init.FSItems;
import com.chinaex123.fintastic_supreme.config.FSConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * 物品提示信息处理器。
 * <p>
 * 监听物品提示信息事件，为各类鱼钩与鱼线物品
 * 动态添加基于配置数值的提示信息。
 */
@EventBusSubscriber(modid = "fintastic_supreme")
public class HookTooltipHandler {

    /**
     * 处理物品提示信息事件。
     * <p>
     * 根据物品类型分别添加对应的提示：
     * 双倍捕获鱼钩（捕获概率）、多掉落鱼钩（概率与最大额外数量）、
     * 幸运鱼线（幸运值转概率）、轻量化鱼线（无动态数值）。
     *
     * @param event 物品提示信息事件
     */
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // 双倍捕获鱼钩：显示捕获概率百分比
        if (event.getItemStack().is(FSItems.DOUBLE_CATCH_HOOK.get())) {
            double percent = FSConfig.DOUBLE_CATCH_CHANCE.get() * 100;

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.double_catch_hook.tooltip", percent)
                    .withStyle(ChatFormatting.BLUE));
        }

        // 多掉落鱼钩：显示触发概率与最大额外掉落数量
        if (stack.is(FSItems.MULTI_DROP_HOOK.get())) {
            double percent = FSConfig.MULTI_DROP_CHANCE.get() * 100;
            int maxExtra = FSConfig.MULTI_DROP_MAX_EXTRA.get();

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.multi_drop_hook.tooltip", percent, maxExtra)
                    .withStyle(ChatFormatting.BLUE));
        }

        // 幸运鱼线：将幸运值换算为百分比显示
        if (stack.is(FSItems.LUCKY_LINE.get())) {
            double percent = FSConfig.LUCKY_LINE_LUCK_TO_CHANCE.get() * 100;

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.lucky_line.tooltip", String.format("%.0f", percent))
                    .withStyle(ChatFormatting.BLUE));
        }

        // 轻量化鱼线：仅显示固定说明文本，无数值
        if (stack.is(FSItems.LIGHTWEIGHT_LINE.get())) {
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.lightweight_line.tooltip")
                    .withStyle(ChatFormatting.BLUE));
        }
    }
}