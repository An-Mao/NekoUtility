package anmao.mc.nu.network.index.packet;

import anmao.mc.nu.block.entity.IndexBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet_Index_ClientToServer {
    private final CompoundTag dat;
    public Packet_Index_ClientToServer(CompoundTag dat){
        this.dat = dat;
    }
    public Packet_Index_ClientToServer(FriendlyByteBuf buf){
        this.dat = buf.readNbt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(dat);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            ServerPlayer sender = context.getSender();
            //System.out.println("player" + sender);
            if (sender != null){
                if (sender.level() instanceof ServerLevel serverLevel) {
                    System.out.println("Net:CTS:SL");
                    IndexBlockEntity blockEntity = (IndexBlockEntity) serverLevel.getBlockEntity(new BlockPos(dat.getInt("be.x"), dat.getInt("be.y"), dat.getInt("be.z")));
                    if (blockEntity != null) {
                        blockEntity.handlePacket(dat);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
        //return true;
    }
}
