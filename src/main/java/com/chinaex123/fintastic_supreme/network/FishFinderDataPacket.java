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
 * 探鱼器数据网络包。
 * <p>
 * 作用：用于将服务端获取的钓鱼概率数据同步到客户端。
 * 包含探鱼器位置、生物群系以及鱼类、战利品、板条箱三类条目列表。
 */
public record FishFinderDataPacket(
        /* 探鱼器所在位置，可能为空 */
        Optional<BlockPos> position,
        /* 所在生物群系标识，可能为空 */
        Optional<String> biome,
        /* 鱼类条目列表 */
        List<FishEntry> fish,
        /* 战利品条目列表 */
        List<FishEntry> loot,
        /* 板条箱条目列表 */
        List<FishEntry> crates
) implements CustomPacketPayload {

    /** 网络包类型标识 */
    public static final Type<FishFinderDataPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(FintasticSupreme.MOD_ID, "fish_finder_data")
    );

    /** 网络包编解码器，按字段顺序组合各部分的编解码逻辑 */
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

    /**
     * 获取网络包类型。
     *
     * @return 网络包类型标识
     */
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * 单个钓鱼条目数据。
     * <p>
     * 记录条目名称与对应概率。
     */
    public record FishEntry(
            /* 条目名称 */
            String name,
            /* 出现概率 */
            double probability
    ) {
        /** 单条目的编解码器 */
        public static final StreamCodec<RegistryFriendlyByteBuf, FishEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                FishEntry::name,
                ByteBufCodecs.DOUBLE,
                FishEntry::probability,
                FishEntry::new
        );
    }
}