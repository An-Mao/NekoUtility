package anmao.mc.nu.network.index;

import anmao.mc.nu.NU;
import anmao.mc.nu.network.index.packet.Packet_Index_ClientToServer;
import anmao.mc.nu.network.index.packet.Packet_Index_ServerToClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Net_Index_Core {
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

        INSTANCE.messageBuilder(Packet_Index_ClientToServer.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Packet_Index_ClientToServer::new)
                .encoder(Packet_Index_ClientToServer::toBytes)
                .consumerMainThread(Packet_Index_ClientToServer::handle).add();
        INSTANCE.messageBuilder(Packet_Index_ServerToClient.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Packet_Index_ServerToClient::new)
                .encoder(Packet_Index_ServerToClient::toBytes)
                .consumerMainThread(Packet_Index_ServerToClient::handle).add();


        //INSTANCE.messageBuilder(Packet_Index_Core.class,id()).decoder(Packet_Index_Core::Decoder).encoder(Packet_Index_Core::Encoder).consumerMainThread(Packet_Index_Core::handle).add();

    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }
    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer serverPlayer){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->serverPlayer), msg);
    }
}
