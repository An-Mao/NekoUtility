package anmao.mc.nu.item;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.Blocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NUCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NU.MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("nu_tab",()-> CreativeModeTab.builder().icon(()->new ItemStack(Blocks.INDEX.get()))
            .title(Component.translatable("create_tab.nu_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(Blocks.INDEX.get());
            })
            .build());

    public static void reg(IEventBus eventBus){
        TABS.register(eventBus);
    }
}
