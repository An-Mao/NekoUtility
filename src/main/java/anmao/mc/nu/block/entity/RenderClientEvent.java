package anmao.mc.nu.block.entity;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.entity.index.IndexBlockEntityRender;
import anmao.mc.nu.block.entity.the_eight_trigrams.TheEightTrigramsBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NU.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class RenderClientEvent {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(NUBlockEntities.INDEX.get(), IndexBlockEntityRender::new);
        event.registerBlockEntityRenderer(NUBlockEntities.THE_EIGHT_TRIGRAMS.get(), TheEightTrigramsBlockEntityRenderer::new);
    }
}
