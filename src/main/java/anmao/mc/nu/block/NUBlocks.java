package anmao.mc.nu.block;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.index.IndexBlock;
import anmao.mc.nu.block.the_eight_trigrams.TheEightTrigramsBlock;
import anmao.mc.nu.item.NUItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class NUBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NU.MOD_ID);

    public static final RegistryObject<Block> INDEX = registryBlock("index", IndexBlock::new);
    public static final RegistryObject<Block> THE_EIGHT_TRIGRAMS = registryBlock("the_eight_trigrams", TheEightTrigramsBlock::new);



    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registryBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registryBlockItem(String name, RegistryObject<T> block) {
        return NUItems.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties()));
    }
    public static void reg(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
