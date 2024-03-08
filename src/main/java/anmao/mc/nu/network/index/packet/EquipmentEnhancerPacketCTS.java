package anmao.mc.nu.network.index.packet;

import anmao.mc.nu.block.equipment_enhancer.EquipmentEnhancerBlockEntity;
import anmao.mc.nu.block.index.IndexBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EquipmentEnhancerPacketCTS {
    private final CompoundTag data;
    public EquipmentEnhancerPacketCTS(CompoundTag data){
        this.data = data;
    }
    public EquipmentEnhancerPacketCTS(FriendlyByteBuf buf){
        this.data = buf.readNbt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(data);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            ServerPlayer sender = context.getSender();
            if (sender != null && data != null){
                if (sender.level() instanceof ServerLevel serverLevel) {
                    int x = data.getInt("block_entity.x");
                    int y = data.getInt("block_entity.y");
                    int z = data.getInt("block_entity.z");
                    EquipmentEnhancerBlockEntity blockEntity = (EquipmentEnhancerBlockEntity) serverLevel.getBlockEntity(new BlockPos(x,y,z));
                    if (blockEntity != null) {
                        ResourceLocation resourceLocation = new ResourceLocation(data.getString("attribute.name"),data.getString("attribute.path"));
                        blockEntity.handlePacket(resourceLocation,data.getString("effective_slot"));
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
