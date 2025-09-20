package cybercat5555.faunus.common.network;

import cybercat5555.faunus.util.FaunusID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UseAirPacket() implements CustomPayload {

    public static final Identifier IDENTIFIER = FaunusID.content("use_air");
    public static final Id<UseAirPacket> ID = new Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, UseAirPacket> CODEC = PacketCodec.unit(new UseAirPacket());
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public UseAirPacket(){

    }
}
