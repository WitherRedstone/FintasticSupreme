package com.chinaex123.fintastic_supreme.data;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.init.FSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class FSItemModelsProvider extends ItemModelProvider {
    public FSItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FintasticSupreme.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // 探鱼器
        basicItem(FSItems.FISH_FINDER.get());

        // 鱼饵
        basicItem(FSItems.ADVANCED_BAIT.get());
        basicItem(FSItems.MASTER_BAIT.get());
        // 幸运鱼饵
        basicItem(FSItems.ADVANCED_LUCKY_BAIT.get());
        basicItem(FSItems.MASTER_LUCKY_BAIT.get());
        // 组合鱼饵
        basicItem(FSItems.MIXED_BAIT.get());
        basicItem(FSItems.ADVANCED_MIXED_BAIT.get());
        basicItem(FSItems.MASTER_MIXED_BAIT.get());
        // 磁力鱼饵
        basicItem(FSItems.ADVANCED_MAGNETIC_BAIT.get());
        basicItem(FSItems.MASTER_MAGNETIC_BAIT.get());

        // 双倍鱼饵
        basicItem(FSItems.DOUBLE_CATCH_HOOK.get());
        // 多重掉落钓钩
        basicItem(FSItems.MULTI_DROP_HOOK.get());

        // 幸运传导鱼线
        basicItem(FSItems.LUCKY_LINE.get());
        // 轻盈鱼线
        basicItem(FSItems.LIGHTWEIGHT_LINE.get());
    }
}
