package com.chinaex123.fintastic_supreme.init;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FintasticSupreme.MOD_ID);

    public static final Supplier<CreativeModeTab> FINTASTIC_SUPREME_TAB =
            CREATIVE_MODE_TAB.register("fintastic_supreme_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(FSItems.FISH_FINDER.get()))
                    .title(Component.translatable("itemGroup.fintastic_supreme_tab"))
                    .displayItems((parameters, output) -> {

                        // 探鱼器
                        output.accept(FSItems.FISH_FINDER.get());

                        // 鱼饵
                        output.accept(FSItems.ADVANCED_BAIT.get());
                        output.accept(FSItems.MASTER_BAIT.get());
                        // 幸运鱼饵
                        output.accept(FSItems.ADVANCED_LUCKY_BAIT.get());
                        output.accept(FSItems.MASTER_LUCKY_BAIT.get());
                        // 组合鱼饵
                        output.accept(FSItems.MIXED_BAIT.get());
                        output.accept(FSItems.ADVANCED_MIXED_BAIT.get());
                        output.accept(FSItems.MASTER_MIXED_BAIT.get());
                        // 磁力鱼饵
                        output.accept(FSItems.ADVANCED_MAGNETIC_BAIT.get());
                        output.accept(FSItems.MASTER_MAGNETIC_BAIT.get());

                        // 双倍钓钩
                        output.accept(FSItems.DOUBLE_CATCH_HOOK.get());
                        // 多重掉落钓钩
                        output.accept(FSItems.MULTI_DROP_HOOK.get());

                        // 幸运传导鱼线
                        output.accept(FSItems.LUCKY_LINE.get());
                        // 轻盈鱼线
                        output.accept(FSItems.LIGHTWEIGHT_LINE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
