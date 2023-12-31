package anmao.mc.nu.network.index;

import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet_Index_Core {
    private CompoundTag dat;
    public Packet_Index_Core(FriendlyByteBuf buf){
        dat = buf.readNbt();
        //
    }
    public void Encoder(FriendlyByteBuf buf){
        buf.writeNbt(dat);
    }
    public static Packet_Index_Core Decoder(FriendlyByteBuf buf){
        return new  Packet_Index_Core(buf);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){}
    public static void handle(_DataType_EnchantData msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            //ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
        });
        ctx.get().setPacketHandled(true);
    }
}
