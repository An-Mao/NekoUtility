package anmao.mc.nu.network.index;

import anmao.mc.nu.datatype.EnchantDataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IndexPacketCore {
    private CompoundTag dat;
    public IndexPacketCore(FriendlyByteBuf buf){
        dat = buf.readNbt();
        //
    }
    public void Encoder(FriendlyByteBuf buf){
        buf.writeNbt(dat);
    }
    public static IndexPacketCore Decoder(FriendlyByteBuf buf){
        return new IndexPacketCore(buf);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){}
    public static void handle(EnchantDataType msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            //ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
        });
        ctx.get().setPacketHandled(true);
    }
}
