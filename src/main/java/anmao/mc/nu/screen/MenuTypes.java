package anmao.mc.nu.screen;

import anmao.mc.nu.NU;
import anmao.mc.nu.screen.equipment_enhancer.EquipmentEnhancerMenu;
import anmao.mc.nu.screen.index.IndexMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, NU.MOD_ID);
    public static final RegistryObject<MenuType<IndexMenu>> INDEX_MENU = registryMenuType("index_menu", IndexMenu::new);
    public static final RegistryObject<MenuType<EquipmentEnhancerMenu>> EQUIPMENT_ENHANCER_MENU = registryMenuType("equipment_enhancer", EquipmentEnhancerMenu::new);
    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registryMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name,()-> IForgeMenuType.create(factory));
    }
    public static void reg(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
