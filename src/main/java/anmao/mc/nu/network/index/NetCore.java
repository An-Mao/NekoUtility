package anmao.mc.nu.network.index;

import anmao.mc.nu.NU;
import anmao.mc.nu.network.index.packet.IndexPacketCTS;
import anmao.mc.nu.network.index.packet.IndexPacketSTC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetCore {
    private static final String PROTOCOL_VERSION = "1";
    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(NU.MOD_ID, "block_entity.index"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void  reg(){
        //INSTANCE.registerMessage(id(), Packet_Index_ServerToClient.class,);

        INSTANCE.messageBuilder(IndexPacketCTS.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(IndexPacketCTS::new)
                .encoder(IndexPacketCTS::toBytes)
                .consumerMainThread(IndexPacketCTS::handle).add();
        INSTANCE.messageBuilder(IndexPacketSTC.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(IndexPacketSTC::new)
                .encoder(IndexPacketSTC::toBytes)
                .consumerMainThread(IndexPacketSTC::handle).add();


        //INSTANCE.messageBuilder(Packet_Index_Core.class,id()).decoder(Packet_Index_Core::Decoder).encoder(Packet_Index_Core::Encoder).consumerMainThread(Packet_Index_Core::handle).add();

    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }
    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer serverPlayer){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->serverPlayer), msg);
    }
}
