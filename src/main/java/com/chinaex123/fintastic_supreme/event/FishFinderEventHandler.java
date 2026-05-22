package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.chinaex123.fintastic_supreme.init.FSItems;
import com.chinaex123.fintastic_supreme.network.FishFinderDataPacket;
import com.li64.tide.Tide;
import com.li64.tide.data.commands.TestType;
import com.li64.tide.data.fishing.selector.FishingEntry;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 探鱼器事件处理器
 * <p>
 * 作用：监听玩家右键事件，当持有探鱼器且正在钓鱼时，发送钓点概率数据到客户端
 */
@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FishFinderEventHandler {

    /**
     * 监听玩家右键点击事件
     * @param event 右键事件对象
     */
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();

        // 只在服务端处理
        if (player.level().isClientSide()) {
            return;
        }

        // 检查是否是服务器玩家
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        // 检查是否正在潜行
        if (!serverPlayer.isShiftKeyDown()) {
            return;
        }

        // 检查主手或副手是否持有探鱼器
        boolean mainHandHasFinder = serverPlayer.getMainHandItem().is(FSItems.FISH_FINDER.get());
        boolean offHandHasFinder = serverPlayer.getOffhandItem().is(FSItems.FISH_FINDER.get());

        // 如果主手和副手都没有探鱼器，不处理
        if (!mainHandHasFinder && !offHandHasFinder) {
            return;
        }

        // 获取玩家的钓鱼钩
        TideFishingHook hook = HookAccessor.getHook(serverPlayer);

        // 如果主手持探鱼器，且有数据时清除
        if (mainHandHasFinder) {
            // 检查当前是否有数据（从玩家物品栏检查）
            var mainHandItem = serverPlayer.getMainHandItem();
            var offhandItem = serverPlayer.getOffhandItem();

            boolean hasData = false;
            if (mainHandItem.is(FSItems.FISH_FINDER.get())) {
                hasData = mainHandItem.has(FSDataComponents.FISH_FINDER_DATA.get());
            } else if (offhandItem.is(FSItems.FISH_FINDER.get())) {
                hasData = offhandItem.has(FSDataComponents.FISH_FINDER_DATA.get());
            }

            // 只有在有数据时才发送清除包
            if (hasData) {
                PacketDistributor.sendToPlayer(serverPlayer, new FishFinderDataPacket(
                        Optional.empty(),
                        Optional.empty(),
                        List.of(), List.of(), List.of()
                ));
                event.setCanceled(true);
            }
            return;
        }

        // 如果有钓鱼钩，且副手持探鱼器，获取并更新数据
        if (hook != null && offHandHasFinder) {
            Map<FishingEntry, Double> fishResults = Tide.FISHING_MANAGER.test(hook.getContext(), TestType.FISH);
            Map<FishingEntry, Double> lootResults = Tide.FISHING_MANAGER.test(hook.getContext(), TestType.LOOT);
            Map<FishingEntry, Double> crateResults = Tide.FISHING_MANAGER.test(hook.getContext(), TestType.CRATES);

            List<FishFinderDataPacket.FishEntry> fishData = convertResults(fishResults);
            List<FishFinderDataPacket.FishEntry> lootData = convertResults(lootResults);
            List<FishFinderDataPacket.FishEntry> crateData = convertResults(crateResults);

            // 获取钓点坐标和群系
            BlockPos hookPos = serverPlayer.blockPosition();
            var biomeHolder = serverPlayer.level().getBiome(hookPos);

            // 尝试获取群系的本地化名称
            String biomeName = biomeHolder.unwrapKey()
                    .flatMap(key -> {
                        // 尝试从注册表获取本地化名称
                        var biomeResourceLocation = key.location();
                        String translationKey = "biome." + biomeResourceLocation.getNamespace() + "." + biomeResourceLocation.getPath();
                        var component = Component.translatable(translationKey);
                        // 检查是否有翻译，如果没有则返回空
                        if (component.getString().equals(translationKey)) {
                            return Optional.empty();
                        }
                        return Optional.of(component.getString());
                    })
                    .orElseGet(() -> {
                        // 如果没有本地化，使用注册名
                        return biomeHolder.unwrapKey()
                                .map(key -> key.location().toString())
                                .orElse("unknown");
                    });

            PacketDistributor.sendToPlayer(serverPlayer, new FishFinderDataPacket(
                    Optional.of(hookPos),
                    Optional.of(biomeName),
                    fishData, lootData, crateData
            ));
            event.setCanceled(true);
        }
    }

    /**
     * 转换结果为网络包格式
     * @param results 原始结果
     * @return 转换后的列表
     */
    private static List<FishFinderDataPacket.FishEntry> convertResults(Map<FishingEntry, Double> results) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        double total = results.values().stream().mapToDouble(d -> d).sum();

        return results.entrySet().stream()
                .sorted(Comparator.comparing(entry -> -entry.getValue()))
                .limit(10)
                .map(entry -> {
                    double percent = (entry.getValue() / total) * 100.0;
                    String name = entry.getKey().getTestKey().getString();
                    return new FishFinderDataPacket.FishEntry(name, percent);
                })
                .collect(Collectors.toList());
    }
}
