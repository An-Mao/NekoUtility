package anmao.mc.nu.item;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.Blocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NU.MOD_ID);
    public static final RegistryObject<Item> EXPLORATION_STICK = ITEMS.register("exploration_stick",()->new BlockItem(Blocks.INDEX.get(),new Item.Properties()));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
