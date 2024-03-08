package anmao.mc.nu.network.index.packet;

import anmao.mc.nu.block.index.IndexBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IndexPacketCTS {
    private final CompoundTag dat;
    public IndexPacketCTS(CompoundTag dat){
        this.dat = dat;
    }
    public IndexPacketCTS(FriendlyByteBuf buf){
        this.dat = buf.readNbt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(dat);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            ServerPlayer sender = context.getSender();
            if (sender != null){
                if (sender.level() instanceof ServerLevel serverLevel) {
                    IndexBlockEntity blockEntity = (IndexBlockEntity) serverLevel.getBlockEntity(new BlockPos(dat.getInt("be.x"), dat.getInt("be.y"), dat.getInt("be.z")));
                    if (blockEntity != null) {
                        blockEntity.handlePacket(dat);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
