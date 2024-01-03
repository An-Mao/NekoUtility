package anmao.mc.nu.datagen;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.Blocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DataGen_BlockStateProvider extends BlockStateProvider {
    public DataGen_BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NU.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //simpleBlock(Blocks.INDEX.get(), new ModelFile.UncheckedModelFile(modLoc("block/index")));
        simpleBlockWithItem(Blocks.INDEX.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/index")));
    }











    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(),cubeAll(blockRegistryObject.get()));
    }
}
