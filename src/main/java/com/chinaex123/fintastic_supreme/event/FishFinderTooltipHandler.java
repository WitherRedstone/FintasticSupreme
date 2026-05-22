package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.config.FSConfig;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.chinaex123.fintastic_supreme.data.FishFinderStoredData;
import com.chinaex123.fintastic_supreme.init.FSItems;
import com.chinaex123.fintastic_supreme.network.FishFinderPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

/**
 * 探鱼器 Tooltip 事件处理器
 * <p>
 * 作用：当玩家持有探鱼器时，在物品提示中显示存储的钓鱼概率信息
 */
@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FishFinderTooltipHandler {

    /**
     * 监听物品 Tooltip 显示事件
     * @param event Tooltip 事件对象
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();

        if (player == null) {
            return;
        }

        if (!player.level().isClientSide()) {
            return;
        }

        if (!event.getItemStack().is(FSItems.FISH_FINDER.get())) {
            return;
        }

        // 优先从缓存读取
        FishFinderStoredData storedData = FishFinderPacketHandler.getCachedData(player.getUUID());

        // 如果缓存为空，尝试从物品栏读取
        if (storedData == null) {
            var mainHandItem = player.getMainHandItem();
            var offhandItem = player.getOffhandItem();

            if (mainHandItem.is(FSItems.FISH_FINDER.get())) {
                storedData = mainHandItem.get(FSDataComponents.FISH_FINDER_DATA.get());
            } else if (offhandItem.is(FSItems.FISH_FINDER.get())) {
                storedData = offhandItem.get(FSDataComponents.FISH_FINDER_DATA.get());
            }
        }

        if (storedData == null) {
            return;
        }

        List<FishFinderStoredData.FishEntry> fishData = storedData.fish();
        List<FishFinderStoredData.FishEntry> lootData = storedData.loot();
        List<FishFinderStoredData.FishEntry> crateData = storedData.crates();

        boolean showPosition = FSConfig.SHOW_POSITION.get();
        boolean showBiome = FSConfig.SHOW_BIOME.get();
        boolean showFish = FSConfig.SHOW_FISH_PROBABILITY.get();
        boolean showLoot = FSConfig.SHOW_LOOT_PROBABILITY.get();
        boolean showCrate = FSConfig.SHOW_CRATE_PROBABILITY.get();

        if (!showFish && !showLoot && !showCrate && !showPosition && !showBiome) {
            return;
        }

        boolean hasAnyData = !fishData.isEmpty() || !lootData.isEmpty() || !crateData.isEmpty()
                || (showPosition && storedData.position().isPresent())
                || (showBiome && storedData.biome().isPresent());

        if (!hasAnyData) {
            return;
        }

        event.getToolTip().add(Component.literal("──────────────").withStyle(ChatFormatting.GRAY));
        event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.tooltip_title").withStyle(ChatFormatting.GOLD));

        // 显示钓点信息
        if (showPosition) {
            storedData.position().ifPresent(pos -> {
                event.getToolTip().add(Component.literal(""));
                event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.position",
                        pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.AQUA));
            });
        }

        if (showBiome) {
            storedData.biome().ifPresent(biome -> {
                if (!showPosition) {
                    event.getToolTip().add(Component.literal(""));
                }
                event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.biome", biome).withStyle(ChatFormatting.AQUA));
            });
        }

        // 显示鱼类概率
        if (showFish && !fishData.isEmpty()) {
            event.getToolTip().add(Component.literal(""));
            event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.fish_title").withStyle(ChatFormatting.YELLOW));
            displayResults(event.getToolTip(), fishData);
        }

        // 显示战利品概率
        if (showLoot && !lootData.isEmpty()) {
            event.getToolTip().add(Component.literal(""));
            event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.loot_title").withStyle(ChatFormatting.YELLOW));
            displayResults(event.getToolTip(), lootData);
        }

        // 显示箱子概率
        if (showCrate && !crateData.isEmpty()) {
            event.getToolTip().add(Component.literal(""));
            event.getToolTip().add(Component.translatable("fintastic_supreme.fish_finder.crate_title").withStyle(ChatFormatting.YELLOW));
            displayResults(event.getToolTip(), crateData);
        }
    }

    /**
     * 格式化并显示结果到 Tooltip
     * @param toolTip Tooltip 列表
     * @param entries 数据条目列表
     */
    private static void displayResults(List<Component> toolTip, List<FishFinderStoredData.FishEntry> entries) {
        entries.forEach(entry -> {
            String percent = String.format("%.1f", entry.probability()) + "%";

            Component entryText = Component.literal("  • ")
                    .append(Component.literal(entry.name()).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(" - ").withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(percent).withStyle(ChatFormatting.GREEN));

            toolTip.add(entryText);
        });
    }
}
