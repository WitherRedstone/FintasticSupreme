package com.chinaex123.fintastic_supreme.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.Optional;

/**
 * 探鱼器存储数据结构
 * <p>
 * 作用：用于将钓鱼概率数据保存到物品数据组件中
 */
public record FishFinderStoredData(
        Optional<BlockPos> position,
        Optional<String> biome,
        List<FishEntry> fish,
        List<FishEntry> loot,
        List<FishEntry> crates
) {

    /**
     * 数据编解码器，用于持久化存储
     */
    public static final Codec<FishFinderStoredData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.optionalFieldOf("position").forGetter(FishFinderStoredData::position),
                    Codec.STRING.optionalFieldOf("biome").forGetter(FishFinderStoredData::biome),
                    FishEntry.CODEC.listOf().fieldOf("fish").forGetter(FishFinderStoredData::fish),
                    FishEntry.CODEC.listOf().fieldOf("loot").forGetter(FishFinderStoredData::loot),
                    FishEntry.CODEC.listOf().fieldOf("crates").forGetter(FishFinderStoredData::crates)
            ).apply(instance, FishFinderStoredData::new)
    );

    /**
     * 网络流编解码器，用于网络同步
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FishFinderStoredData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
            FishFinderStoredData::position,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            FishFinderStoredData::biome,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderStoredData::fish,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderStoredData::loot,
            FishEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FishFinderStoredData::crates,
            FishFinderStoredData::new
    );

    /**
     * 单个钓鱼条目数据
     * @param name 名称
     * @param probability 概率百分比
     */
    public record FishEntry(String name, double probability) {
        public static final Codec<FishEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(FishEntry::name),
                        Codec.DOUBLE.fieldOf("probability").forGetter(FishEntry::probability)
                ).apply(instance, FishEntry::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FishEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                FishEntry::name,
                ByteBufCodecs.DOUBLE,
                FishEntry::probability,
                FishEntry::new
        );
    }
}
