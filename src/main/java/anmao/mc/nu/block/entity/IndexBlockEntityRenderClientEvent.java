package anmao.mc.nu.block.entity;

import anmao.mc.nu.NU;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NU.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class IndexBlockEntityRenderClientEvent {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntities.INDEX.get(), IndexBlockEntityRender::new);
    }
}
