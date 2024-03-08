package anmao.mc.nu.network;

import anmao.mc.amlib.amlib.network.easy_net.EasyNet;
import anmao.mc.nu.block.index.IndexBlockEntity;
import anmao.mc.nu.screen.index.IndexScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IndexBlockNet extends EasyNet {
    @Override
    public void client(Supplier<NetworkEvent.Context> contextSupplier, CompoundTag dat) {
        if (Minecraft.getInstance().screen instanceof IndexScreen screen) {
            screen.handlePacket(dat);
            super.client(contextSupplier, dat);
        }
    }

    @Override
    public void server(Supplier<NetworkEvent.Context> contextSupplier, CompoundTag dat) {
        ServerPlayer sender = contextSupplier.get().getSender();
        if (sender != null){
            if (sender.level() instanceof ServerLevel serverLevel) {
                IndexBlockEntity blockEntity = (IndexBlockEntity) serverLevel.getBlockEntity(new BlockPos(dat.getInt("be.x"), dat.getInt("be.y"), dat.getInt("be.z")));
                if (blockEntity != null) {
                    blockEntity.handlePacket(dat);
                    super.server(contextSupplier, dat);
                }
            }
        }
    }
}
