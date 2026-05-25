package com.chinaex123.fintastic_supreme.network;

import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.chinaex123.fintastic_supreme.data.FishFinderStoredData;
import com.chinaex123.fintastic_supreme.init.FSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 探鱼器网络包处理器
 * <p>
 * 作用：处理服务端发送的钓鱼数据到客户端，并将数据存储到物品组件中
 */
public class FishFinderPacketHandler {
    /**
     * 客户端数据缓存，按玩家UUID存储
     * 用于解决物品切换时数据丢失的问题
     */
    private static final Map<UUID, FishFinderStoredData> clientCache = new HashMap<>();

    /**
     * 处理探鱼器数据包
     * @param packet 数据包
     * @param context 上下文
     */
    public static void handle(FishFinderDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            boolean hasData = !packet.fish().isEmpty() || !packet.loot().isEmpty() || !packet.crates().isEmpty();

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) {
                return;
            }

            // 播放使用动作
            minecraft.player.swing(InteractionHand.MAIN_HAND);

            if (hasData) {
                // 显示存储提示
                minecraft.player.displayClientMessage(Component.translatable("fintastic_supreme.fish_finder.stored")
                        .withStyle(ChatFormatting.GREEN), true);

                // 转换数据格式
                List<FishFinderStoredData.FishEntry> fishEntries = convertToStoredData(packet.fish());
                List<FishFinderStoredData.FishEntry> lootEntries = convertToStoredData(packet.loot());
                List<FishFinderStoredData.FishEntry> crateEntries = convertToStoredData(packet.crates());

                FishFinderStoredData data = new FishFinderStoredData(
                        packet.position(), packet.biome(),
                        fishEntries, lootEntries, crateEntries
                );

                // 保存到缓存
                clientCache.put(minecraft.player.getUUID(), data);

                // 同时保存到当前持有的物品
                if (minecraft.player.getMainHandItem().is(FSItems.FISH_FINDER.get())) {
                    var item = minecraft.player.getMainHandItem();
                    item.set(FSDataComponents.FISH_FINDER_DATA.get(), data);
                } else if (minecraft.player.getOffhandItem().is(FSItems.FISH_FINDER.get())) {
                    var item = minecraft.player.getOffhandItem();
                    item.set(FSDataComponents.FISH_FINDER_DATA.get(), data);
                }
            } else {
                minecraft.player.displayClientMessage(Component.translatable("fintastic_supreme.fish_finder.cleared")
                        .withStyle(ChatFormatting.RED), true);

                // 清除缓存
                clientCache.remove(minecraft.player.getUUID());

                // 从物品中移除数据
                if (minecraft.player.getMainHandItem().is(FSItems.FISH_FINDER.get())) {
                    var item = minecraft.player.getMainHandItem();
                    item.remove(FSDataComponents.FISH_FINDER_DATA.get());
                } else if (minecraft.player.getOffhandItem().is(FSItems.FISH_FINDER.get())) {
                    var item = minecraft.player.getOffhandItem();
                    item.remove(FSDataComponents.FISH_FINDER_DATA.get());
                }
            }
        });
    }

    /**
     * 获取玩家的缓存数据
     * @param playerId 玩家UUID
     * @return 存储的数据，如果不存在返回null
     */
    public static FishFinderStoredData getCachedData(UUID playerId) {
        return clientCache.get(playerId);
    }

    /**
     * 转换网络包数据为存储数据格式
     * @param entries 网络包数据列表
     * @return 存储数据列表
     */
    private static List<FishFinderStoredData.FishEntry> convertToStoredData(List<FishFinderDataPacket.FishEntry> entries) {
        return entries.stream()
                .map(e -> new FishFinderStoredData.FishEntry(e.name(), e.probability()))
                .collect(Collectors.toList());
    }
}
