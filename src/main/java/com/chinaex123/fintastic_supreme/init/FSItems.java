package com.chinaex123.fintastic_supreme.init;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface FSItems {
    DeferredRegister.Items ITEMS_REGISTER = DeferredRegister.createItems(FintasticSupreme.MOD_ID);

    // 探鱼器
    DeferredItem<Item> FISH_FINDER = ITEMS_REGISTER.register("fish_finder", () ->
            new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // 鱼饵
    DeferredItem<Item> ADVANCED_BAIT = ITEMS_REGISTER.register("advanced_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
    DeferredItem<Item> MASTER_BAIT = ITEMS_REGISTER.register("master_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    // 幸运鱼饵
    DeferredItem<Item> ADVANCED_LUCKY_BAIT = ITEMS_REGISTER.register("advanced_lucky_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
    DeferredItem<Item> MASTER_LUCKY_BAIT = ITEMS_REGISTER.register("master_lucky_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    // 组合鱼饵
    DeferredItem<Item> MIXED_BAIT = ITEMS_REGISTER.register("mixed_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));
    DeferredItem<Item> ADVANCED_MIXED_BAIT = ITEMS_REGISTER.register("advanced_mixed_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
    DeferredItem<Item> MASTER_MIXED_BAIT = ITEMS_REGISTER.register("master_mixed_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    // 磁力鱼饵
    DeferredItem<Item> ADVANCED_MAGNETIC_BAIT = ITEMS_REGISTER.register("advanced_magnetic_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    DeferredItem<Item> MASTER_MAGNETIC_BAIT = ITEMS_REGISTER.register("master_magnetic_bait", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

    // 双倍钓钩
    DeferredItem<Item> DOUBLE_CATCH_HOOK = ITEMS_REGISTER.register("double_catch_hook", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    // 多重掉落钓钩
    DeferredItem<Item> MULTI_DROP_HOOK = ITEMS_REGISTER.register("multi_drop_hook", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

    // 幸运传导鱼线
    DeferredItem<Item> LUCKY_LINE = ITEMS_REGISTER.register("lucky_line", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));
    // 轻盈鱼线
    DeferredItem<Item> LIGHTWEIGHT_LINE = ITEMS_REGISTER.register("lightweight_line", () ->
            new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));





    static void register(IEventBus eventBus) {
        ITEMS_REGISTER.register(eventBus);
    }
}
