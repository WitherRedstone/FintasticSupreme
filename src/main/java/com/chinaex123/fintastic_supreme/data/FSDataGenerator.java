package com.chinaex123.fintastic_supreme.data;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FintasticSupreme.MOD_ID)
public class FSDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new FSItemModelsProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new FSItemTagsProvider(packOutput, lookupProvider,
                CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModRecipesProvider(packOutput, lookupProvider));
    }
}
