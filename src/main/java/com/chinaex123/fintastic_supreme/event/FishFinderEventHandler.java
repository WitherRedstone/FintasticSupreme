package com.chinaex123.fintastic_supreme.event;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.chinaex123.fintastic_supreme.data.FishFinderStoredData;
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

        // 如果主手持探鱼器，发送清除包
        if (mainHandHasFinder) {
            PacketDistributor.sendToPlayer(serverPlayer, new FishFinderDataPacket(
                    Optional.empty(),
                    Optional.empty(),
                    List.of(), List.of(), List.of()
            ));
            event.setCanceled(true);
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

            BlockPos hookPos = serverPlayer.blockPosition();
            var biomeHolder = serverPlayer.level().getBiome(hookPos);

            String biomeName = biomeHolder.unwrapKey()
                    .flatMap(key -> {
                        var biomeResourceLocation = key.location();
                        String translationKey = "biome." + biomeResourceLocation.getNamespace() + "." + biomeResourceLocation.getPath();
                        var component = Component.translatable(translationKey);
                        if (component.getString().equals(translationKey)) {
                            return Optional.empty();
                        }
                        return Optional.of(component.getString());
                    })
                    .orElseGet(() -> {
                        return biomeHolder.unwrapKey()
                                .map(key -> key.location().toString())
                                .orElse("unknown");
                    });

            // 先在服务端设置数据到物品组件
            var offhandItem = serverPlayer.getOffhandItem();
            FishFinderStoredData storedData = new FishFinderStoredData(
                    Optional.of(hookPos),
                    Optional.of(biomeName),
                    fishData.stream().map(e -> new FishFinderStoredData.FishEntry(e.name(), e.probability())).collect(Collectors.toList()),
                    lootData.stream().map(e -> new FishFinderStoredData.FishEntry(e.name(), e.probability())).collect(Collectors.toList()),
                    crateData.stream().map(e -> new FishFinderStoredData.FishEntry(e.name(), e.probability())).collect(Collectors.toList())
            );
            offhandItem.set(FSDataComponents.FISH_FINDER_DATA.get(), storedData);

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
