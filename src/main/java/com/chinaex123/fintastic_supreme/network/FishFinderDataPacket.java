package com.chinaex123.fintastic_supreme.network;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 探鱼器数据网络包
 * <p>
 * 作用：用于将服务端获取的钓鱼概率数据同步到客户端
 */
public record FishFinderDataPacket(
        Optional<BlockPos> position,
        Optional<String> biome,
        List<FishEntry> fish,
        List<FishEntry> loot,
        List<FishEntry> crates
) implements CustomPacketPayload {

    public static final Type<FishFinderDataPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(FintasticSupreme.MOD_ID, "fish_finder_data")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishFinderDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
            FishFinderDataPacket::position,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            FishFinderDataPacket::biome,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderDataPacket::fish,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderDataPacket::loot,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderDataPacket::crates,
            FishFinderDataPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * 单个钓鱼条目数据
     */
    public record FishEntry(String name, double probability) {
        public static final StreamCodec<RegistryFriendlyByteBuf, FishEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                FishEntry::name,
                ByteBufCodecs.DOUBLE,
                FishEntry::probability,
                FishEntry::new
        );
    }
}
