package anmao.mc.nu;

import anmao.mc.nu.block.Blocks;
import anmao.mc.nu.block.entity.BlockEntities;
import anmao.mc.nu.item.CreativeTabs_NekoUtility;
import anmao.mc.nu.item.Items;
import anmao.mc.nu.network.index.Net_Index_Core;
import anmao.mc.nu.screen.IndexScreen;
import anmao.mc.nu.screen.MenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NU.MOD_ID)
public class NU
{
    public static final String MOD_ID = "nu";
    public static final Logger LOG = LogUtils.getLogger();

    public NU()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        CreativeTabs_NekoUtility.reg(modEventBus);
        Items.register(modEventBus);
        Blocks.reg(modEventBus);
        BlockEntities.reg(modEventBus);
        MenuTypes.reg(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        //modEventBus.addListener(this::addCreative);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        Net_Index_Core.reg();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(MenuTypes.INDEX_MENU.get(), IndexScreen::new);
        }
    }
}
