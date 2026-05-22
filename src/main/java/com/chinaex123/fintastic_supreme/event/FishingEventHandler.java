package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import java.util.List;
import java.util.Random;

/**
 * 钓鱼事件处理器
 * <p>
 * 作用：监听玩家钓鱼事件，当钓到带有重量数据的鱼时，获取鱼的长度并发送包含详细信息的捕获消息
 */
@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FishingEventHandler {
    private static final Random RANDOM = new Random();

    /**
     * 监听钓鱼完成事件
     * @param event 钓鱼事件对象，包含钓到的物品列表和玩家信息
     */
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        // 获取钓到的所有物品堆列表
        List<ItemStack> drops = event.getDrops();

        // 遍历所有钓到的物品
        for (ItemStack stack : drops) {
            // 跳过空物品堆
            if (stack.isEmpty()) {
                continue;
            }

            // 从物品堆中获取重量数据组件
            Double weight = stack.get(FSDataComponents.FISH_WEIGHT.get());

            // 如果重量数据存在且大于0，说明是有效鱼类
            if (weight != null && weight > 0) {
                // 获取鱼的长度
                double length = getFishLength(stack);

                // 如果长度有效（大于0），发送捕获消息
                if (length > 0) {
                    sendCatchMessage(player, stack, length, weight);
                }
            }
        }
    }

    /**
     * 从物品堆中获取鱼的长度数据
     * @param stack 物品堆对象
     * @return 鱼的长度（厘米），如果获取失败返回0.0
     */
    private static double getFishLength(ItemStack stack) {
        try {
            // 从注册表中获取 Tide 模组的鱼长度数据组件类型
            var tideComponent = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.parse("tide:fish_length"));

            // 如果组件类型存在
            if (tideComponent != null) {
                // 从物品堆中获取长度数据
                Object length = stack.get(tideComponent);

                // 如果长度数据是数字类型，转换为 double 并返回
                if (length instanceof Number number) {
                    return number.doubleValue();
                }
            }
        } catch (Exception e) {
            // 记录获取长度时的错误日志
            FintasticSupreme.LOGGER.error("[FishingEventHandler] 获取鱼的长度时发生错误", e);
        }
        // 获取失败返回0.0
        return 0.0;
    }

    /**
     * 发送钓鱼捕获消息给玩家
     * @param player 玩家对象
     * @param stack 钓到的鱼物品堆
     * @param length 鱼的长度（厘米）
     * @param weight 鱼的重量（千克）
     */
    private static void sendCatchMessage(Player player, ItemStack stack, double length, double weight) {
        String fishName = stack.getHoverName().getString();
        String playerName = player.getName().getString();

        String weightText;
        if (weight >= 1000.0) {
            weightText = String.format("%.2f t", weight / 1000);
        } else if (weight >= 1.0) {
            weightText = String.format("%.2f kg", weight);
        } else {
            weightText = String.format("%.2f g", weight * 1000);
        }

        String template = Component.translatable("fintastic_supreme.fishing.caught",
                playerName, fishName, String.format("%.2f", length), weightText).getString();

        String message = "§b" + template;
        message = message.replace(playerName, "§b" + playerName + "§7").replace(fishName, "§b" + fishName + "§7");
        message = message.replace(String.format("%.2f", length), "§e" + String.format("%.2f", length) + "§7");
        message = message.replace(weightText, "§e" + weightText);

        player.sendSystemMessage(Component.literal(message));
    }
}
