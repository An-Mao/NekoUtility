package anmao.mc.nu.block;

import anmao.mc.nu.NU;
import anmao.mc.nu.item.Items;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NU.MOD_ID);
    public static final RegistryObject<Block> INDEX = registryBlock("index",()->new Block_Index(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK).noOcclusion()));
    public void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registryBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registryBlockItem(String name, RegistryObject<T> block) {
        return Items.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties()));
    }
    public static void reg(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
