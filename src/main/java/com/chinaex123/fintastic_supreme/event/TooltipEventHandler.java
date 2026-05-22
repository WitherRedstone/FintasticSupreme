package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class TooltipEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Double weight = stack.get(FSDataComponents.FISH_WEIGHT.get());

        if (weight != null && weight > 0) {
            String weightText;
            if (weight >= 1000.0) {
                weightText = String.format("%.2f t", weight / 1000);
            } else if (weight >= 1.0) {
                weightText = String.format("%.2f kg", weight);
            } else {
                weightText = String.format("%.2f g", weight * 1000);
            }

            event.getToolTip().add(Component.literal("§9" + weightText));
        }
    }
}
