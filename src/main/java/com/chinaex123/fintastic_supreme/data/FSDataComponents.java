package com.chinaex123.fintastic_supreme.data;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组数据组件注册类
 * <p>
 * 作用：用于注册自定义的物品数据组件
 */
public class FSDataComponents {

    /**
     * 数据组件注册表
     */
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FintasticSupreme.MOD_ID);

    /**
     * 鱼的重量数据组件
     * 用于存储钓到的鱼的重量信息
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> FISH_WEIGHT =
            DATA_COMPONENTS.register("fish_weight", () ->
                    DataComponentType.<Double>builder()
                            .persistent(Codec.DOUBLE)
                            .networkSynchronized(ByteBufCodecs.DOUBLE)
                            .build()
            );

    /**
     * 探鱼器数据组件
     * 用于存储探鱼器扫描的钓鱼点概率信息
     */
    public static final Supplier<DataComponentType<FishFinderStoredData>> FISH_FINDER_DATA =
            DATA_COMPONENTS.register("fish_finder_data", () ->
                    DataComponentType.<FishFinderStoredData>builder()
                            .persistent(FishFinderStoredData.CODEC)
                            .networkSynchronized(FishFinderStoredData.STREAM_CODEC)
                            .build()
            );
}
