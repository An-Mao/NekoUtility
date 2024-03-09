package anmao.mc.nu.network;

import anmao.mc.amlib.amlib.network.easy_net.EasyNet;
import anmao.mc.nu.block.equipment_enhancer.EquipmentEnhancerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EquipmentEnhancerNet extends EasyNet {
    @Override
    public void server(Supplier<NetworkEvent.Context> contextSupplier, CompoundTag dat) {
        ServerPlayer sender = contextSupplier.get().getSender();
        if (sender != null && dat != null){
            if (sender.level() instanceof ServerLevel serverLevel) {
                int x = dat.getInt("block_entity.x");
                int y = dat.getInt("block_entity.y");
                int z = dat.getInt("block_entity.z");
                EquipmentEnhancerBlockEntity blockEntity = (EquipmentEnhancerBlockEntity) serverLevel.getBlockEntity(new BlockPos(x,y,z));
                if (blockEntity != null) {
                    ResourceLocation resourceLocation = new ResourceLocation(dat.getString("attribute.name"),dat.getString("attribute.path"));
                    blockEntity.handlePacket(resourceLocation,dat.getString("effective_slot"));
                    super.server(contextSupplier, dat);
                }
            }
        }
    }
}
