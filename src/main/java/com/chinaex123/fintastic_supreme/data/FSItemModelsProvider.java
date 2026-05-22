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
        basicItem(FSItems.FISH_FINDER.get());
    }
}
