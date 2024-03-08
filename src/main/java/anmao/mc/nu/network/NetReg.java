package anmao.mc.nu.network;

import anmao.mc.amlib.amlib.network.easy_net.EasyNet;
import anmao.mc.amlib.amlib.network.easy_net.EasyNetRegister;
import net.minecraftforge.registries.RegistryObject;

public class NetReg {
    public static final RegistryObject<EasyNet> INDEX_BLOCK = EasyNetRegister.EASY_NET.register("index_block",IndexBlockNet::new);
}
