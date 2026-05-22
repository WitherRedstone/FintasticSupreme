package com.chinaex123.fintastic_supreme.init;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface FSItems {
    DeferredRegister.Items ITEMS_REGISTER = DeferredRegister.createItems(FintasticSupreme.MOD_ID);

    DeferredItem<Item> FISH_FINDER = ITEMS_REGISTER.register("fish_finder", () ->
            new Item(new Item.Properties().rarity(Rarity.EPIC)));

    static void register(IEventBus eventBus) {
        ITEMS_REGISTER.register(eventBus);
    }
}
