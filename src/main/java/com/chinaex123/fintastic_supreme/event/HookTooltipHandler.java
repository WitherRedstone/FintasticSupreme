package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.init.FSItems;
import com.chinaex123.fintastic_supreme.util.FSBaitConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = "fintastic_supreme")
public class HookTooltipHandler {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (event.getItemStack().is(FSItems.DOUBLE_CATCH_HOOK.get())) {
            double percent = FSBaitConfig.DOUBLE_CATCH_CHANCE * 100;

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.double_catch_hook.tooltip", percent)
                    .withStyle(ChatFormatting.BLUE));
        }

        if (stack.is(FSItems.MULTI_DROP_HOOK.get())) {
            double percent = FSBaitConfig.MULTI_DROP_CHANCE * 100;
            int maxExtra = FSBaitConfig.MULTI_DROP_MAX_EXTRA;

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.multi_drop_hook.tooltip", percent, maxExtra)
                    .withStyle(ChatFormatting.BLUE));
        }

        if (stack.is(FSItems.LUCKY_LINE.get())) {
            double percent = FSBaitConfig.LUCKY_LINE_LUCK_TO_CHANCE * 100;

            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.lucky_line.tooltip", String.format("%.0f", percent))
                    .withStyle(ChatFormatting.BLUE));
        }

        if (stack.is(FSItems.LIGHTWEIGHT_LINE.get())) {
            event.getToolTip().add(Component.empty());
            event.getToolTip().add(Component.translatable("text.tide.accessory_tooltip.prefix").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("item.fintastic_supreme.lightweight_line.tooltip")
                    .withStyle(ChatFormatting.BLUE));
        }
    }
}
