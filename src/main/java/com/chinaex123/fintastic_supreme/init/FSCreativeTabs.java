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

                        output.accept(FSItems.FISH_FINDER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
