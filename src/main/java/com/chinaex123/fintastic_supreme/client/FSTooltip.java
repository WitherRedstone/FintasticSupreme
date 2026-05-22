package com.chinaex123.fintastic_supreme.client;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.init.FSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FSTooltip {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        // 松露
        if (stack.getItem() == FSItems.FISH_FINDER.get()) {
            tooltip.add(Component.translatable("item.fintastic_supreme.fish_finder.tooltip.1").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("item.fintastic_supreme.fish_finder.tooltip.2").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
}
