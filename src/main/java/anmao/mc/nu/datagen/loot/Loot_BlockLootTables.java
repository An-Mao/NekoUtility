package anmao.mc.nu.datagen.loot;

import anmao.mc.nu.block.NUBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class Loot_BlockLootTables extends BlockLootSubProvider {
    public Loot_BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(NUBlocks.INDEX.get());
        this.dropSelf(NUBlocks.THE_EIGHT_TRIGRAMS.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return NUBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
