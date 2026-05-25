package com.chinaex123.fintastic_supreme.data;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.init.FSItems;
import com.li64.tide.data.TideTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class FSItemTagsProvider extends ItemTagsProvider {
    public FSItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FintasticSupreme.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // 钓钩
        tag(TideTags.Items.HOOKS)
                .add(FSItems.DOUBLE_CATCH_HOOK.get())
                .add(FSItems.MULTI_DROP_HOOK.get());
        // 鱼线
        tag(TideTags.Items.LINES)
                .add(FSItems.LUCKY_LINE.get())
                .add(FSItems.LIGHTWEIGHT_LINE.get());
    }
}
