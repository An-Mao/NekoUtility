package anmao.mc.nu.datagen;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.NUBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DataGen_BlockTag extends BlockTagsProvider {
    public DataGen_BlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(NUBlocks.INDEX.get(), NUBlocks.THE_EIGHT_TRIGRAMS.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(NUBlocks.INDEX.get(), NUBlocks.THE_EIGHT_TRIGRAMS.get());
    }
}
