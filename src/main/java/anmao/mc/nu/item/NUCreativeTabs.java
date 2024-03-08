package anmao.mc.nu.item;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.NUBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NUCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NU.MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("nu_tab",()-> CreativeModeTab.builder().icon(()->new ItemStack(NUBlocks.INDEX.get()))
            .title(Component.translatable("create_tab.nu_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(NUBlocks.INDEX.get());
                pOutput.accept(NUBlocks.THE_EIGHT_TRIGRAMS.get());
                pOutput.accept(NUBlocks.EQUIPMENT_ENHANCER.get());

                pOutput.accept(NUItems.YIN.get());
                pOutput.accept(NUItems.YANG.get());
                pOutput.accept(NUItems.QIAN.get());
                pOutput.accept(NUItems.KUN.get());
                pOutput.accept(NUItems.DUI.get());
                pOutput.accept(NUItems.GEN.get());
                pOutput.accept(NUItems.KAN.get());
                pOutput.accept(NUItems.LI.get());
                pOutput.accept(NUItems.XUN.get());
                pOutput.accept(NUItems.ZHEN.get());
            })
            .build());

    public static void reg(IEventBus eventBus){
        TABS.register(eventBus);
    }
}
