package anmao.mc.nu.network.index.packet;

import anmao.mc.nu.screen.IndexScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet_Index_ServerToClient {
    private final CompoundTag index;
    public Packet_Index_ServerToClient(CompoundTag dat){
        index = dat;
    }

    public Packet_Index_ServerToClient(FriendlyByteBuf buf){
        index = buf.readNbt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(index);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () ->
                () -> {
                    if (Minecraft.getInstance().screen instanceof IndexScreen screen) {
                        screen.handlePacket(index);
                    }
                });
        });
        ctx.get().setPacketHandled(true);
        //return true;
    }
}
