package anmao.mc.nu.event;

import anmao.mc.nu.NU;
import anmao.mc.nu.screen.MenuTypes;
import anmao.mc.nu.screen.index.IndexScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid = NU.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(MenuTypes.INDEX_MENU.get(), IndexScreen::new);
    }
}
