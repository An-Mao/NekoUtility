package anmao.mc.nu;

import anmao.mc.amlib.system._File;
import anmao.mc.nu.block.NUBlocks;
import anmao.mc.nu.block.NUBlockEntities;
import anmao.mc.nu.config.Configs;
import anmao.mc.nu.config.equipment_enhancer.EquipmentEnhancerConfigs;
import anmao.mc.nu.item.NUCreativeTabs;
import anmao.mc.nu.item.NUItems;
import anmao.mc.nu.network.NetReg;
import anmao.mc.nu.screen.MenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NU.MOD_ID)
public class NU
{
    public static final String MOD_ID = "nu";
    public static final Logger LOG = LogUtils.getLogger();
    public static final String ConfigDir = _File.getFileFullPathWithRun("/config/NekoUtility/");
    public NU()
    {
        _File.checkAndCreateDir(ConfigDir);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NUCreativeTabs.reg(modEventBus);
        NUItems.register(modEventBus);
        NUBlocks.reg(modEventBus);
        NUBlockEntities.reg(modEventBus);
        MenuTypes.reg(modEventBus);
        NetReg.reg(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configs.SPEC);
        EquipmentEnhancerConfigs.init();
    }
}
