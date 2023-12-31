package anmao.mc.nu.network.index.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet_Index_ClientToServer {
    public Packet_Index_ClientToServer(){}
    public Packet_Index_ClientToServer(FriendlyByteBuf buf){}
    public void toBytes(FriendlyByteBuf buf){}
    public void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            //
        });
        ctx.get().setPacketHandled(true);
        //return true;
    }
}
