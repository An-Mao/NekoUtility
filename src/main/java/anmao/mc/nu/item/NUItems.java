package anmao.mc.nu.item;

import anmao.mc.nu.NU;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NUItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NU.MOD_ID);
    public static final RegistryObject<Item> YIN = ITEMS.register("yin",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> YANG = ITEMS.register("yang",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> QIAN = ITEMS.register("qian",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> KUN = ITEMS.register("kun",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> DUI = ITEMS.register("dui",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEN = ITEMS.register("gen",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> KAN = ITEMS.register("kan",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> LI = ITEMS.register("li",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> XUN = ITEMS.register("xun",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> ZHEN = ITEMS.register("zhen",()->new Item(new Item.Properties()));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
