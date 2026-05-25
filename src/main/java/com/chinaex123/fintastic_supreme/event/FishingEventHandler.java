package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 钓鱼事件处理器
 * 作用：监听玩家钓鱼事件，当钓到带有重量数据的鱼时，获取鱼的长度并发送包含详细信息的捕获消息
 */
@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FishingEventHandler {
    private static final Random RANDOM = new Random();

    // 用于记录最近一次发送消息的玩家和物品，防止双倍掉落时重复刷屏
    private static final Set<String> lastSentMessages = new HashSet<>();

    /**
     * 监听钓鱼完成事件
     * @param event 钓鱼事件对象，包含钓到的物品列表和玩家信息
     */
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        List<ItemStack> drops = event.getDrops();

        // 用于记录本次事件中已经发送过的消息，防止同一次钓鱼触发多次
        Set<String> currentSessionMessages = new HashSet<>();

        for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;

            Double weight = stack.get(FSDataComponents.FISH_WEIGHT.get());
            if (weight == null || weight <= 0) continue;

            double length = getFishLength(stack);
            if (length <= 0) continue;

            String playerName = player.getName().getString();
            String fishName = stack.getHoverName().getString();

            // 构造消息字符串用于去重判断
            String messageKey = String.format("%s|%s|%.2f|%.2f", playerName, fishName, length, weight);

            // 全局去重 + 本次会话去重
            if (lastSentMessages.contains(messageKey) || currentSessionMessages.contains(messageKey)) {
                continue;
            }

            // 调用 sendCatchMessage 发送消息
            sendCatchMessage(player, stack, length, weight);

            // 加入记录
            currentSessionMessages.add(messageKey);
            lastSentMessages.add(messageKey);
        }

        // 简单的清理机制，防止内存泄漏
        if (lastSentMessages.size() > 20) {
            lastSentMessages.clear();
        }
    }

    /**
     * 监听生物掉落事件
     * 当带有长度数据的鱼类实体死亡时，如果掉落物没有重量数据，则根据长度计算并添加重量
     * @param event 生物掉落事件
     */
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();

        if (entity.level().isClientSide()) {
            return;
        }

        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack stack = itemEntity.getItem();

            ensureFishWeight(stack);
        }
    }

    /**
     * 监听物品 Tooltip 显示事件
     * 在客户端显示 Tooltip 时检查并添加重量数据，确保所有来源的鱼都有重量
     * @param event Tooltip 事件对象
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!stack.isEmpty() && event.getEntity() != null) {
            if (!event.getEntity().level().isClientSide()) {
                return;
            }

            ensureFishWeight(stack);
        }
    }

    /**
     * 确保鱼类物品有重量数据
     * 如果物品有长度但没有重量，则根据长度计算并添加重量
     * @param stack 物品堆对象
     */
    public static void ensureFishWeight(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        Double existingWeight = stack.get(FSDataComponents.FISH_WEIGHT.get());

        if (existingWeight == null || existingWeight <= 0) {
            double length = getFishLength(stack);

            if (length > 0) {
                /* 基础密度系数 */
                double baseDensity = 0.001;
                /* 计算体积：(长度/10)³ */
                double volume = Math.pow(length / 10.0, 3.0);
                /* 计算基础重量：体积 × 密度 × 1000 */
                double weight = volume * baseDensity * 1000;
                /* 生成随机系数：0.75-1.25 之间的随机值 */
                double randomFactor = 0.75 + (RANDOM.nextDouble() * 0.5);
                /* 计算最终重量：基础重量 × 随机系数 */
                double finalWeight = weight * randomFactor;

                stack.set(FSDataComponents.FISH_WEIGHT.get(), finalWeight);
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
            var tideComponent = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.parse("tide:fish_length"));

            if (tideComponent != null) {
                Object length = stack.get(tideComponent);

                if (length instanceof Number number) {
                    return number.doubleValue();
                }
            }
        } catch (Exception e) {
            FintasticSupreme.LOGGER.error("[FishingEventHandler] 获取鱼的长度时发生错误", e);
        }
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

        ServerLevel serverLevel = (ServerLevel) player.level();
        serverLevel.getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(message),
                false
        );
    }
}
