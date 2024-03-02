package anmao.mc.nu.event;

import anmao.mc.nu.NU;
import anmao.mc.nu.network.index.NetCore;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = NU.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NUEvent {
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        NetCore.reg();
    }
}
