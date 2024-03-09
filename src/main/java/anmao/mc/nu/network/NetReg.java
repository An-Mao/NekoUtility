package anmao.mc.nu.network;

import anmao.mc.amlib.amlib.network.easy_net.EasyNet;
import anmao.mc.amlib.amlib.network.easy_net.EasyNetRegister;
import anmao.mc.nu.NU;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NetReg {
    public static final DeferredRegister<EasyNet> EASY_NET = DeferredRegister.create(EasyNetRegister.KEY, NU.MOD_ID);
    public static final RegistryObject<EasyNet> INDEX_BLOCK = EASY_NET.register("index_block",IndexBlockNet::new);
    public static final RegistryObject<EasyNet> EQUIPMENT_ENHANCER = EASY_NET.register("equipment_enhancer_block",EquipmentEnhancerNet::new);
    public static void reg(IEventBus eventBus){
        EASY_NET.register(eventBus);
    }
}
