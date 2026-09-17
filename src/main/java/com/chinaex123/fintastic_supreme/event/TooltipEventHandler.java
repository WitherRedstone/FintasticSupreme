package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * 物品提示信息处理器。
 * <p>
 * 监听物品提示信息事件，为带有重量数据组件的物品
 * 在提示中显示格式化后的重量信息。
 */
@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class TooltipEventHandler {

    /**
     * 处理物品提示信息事件。
     * <p>
     * 读取物品的重量数据组件，若存在且大于 0，
     * 则根据数值大小自动选择单位（吨、千克、克）并格式化显示。
     *
     * @param event 物品提示信息事件
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Double weight = stack.get(FSDataComponents.FISH_WEIGHT.get());

        if (weight != null && weight > 0) {
            String weightText;
            // 根据重量大小选择合适的单位
            if (weight >= 1000000.0) {
                weightText = String.format("%.2f t", weight / 1000000);
            } else if (weight >= 1000.0) {
                weightText = String.format("%.2f kg", weight / 1000);
            } else {
                weightText = String.format("%.2f g", weight);
            }

            // 以蓝色文本添加到提示末尾
            event.getToolTip().add(Component.literal("§9" + weightText));
        }
    }
}