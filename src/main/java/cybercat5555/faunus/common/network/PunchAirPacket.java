package cybercat5555.faunus.common.network;

import com.mojang.serialization.Codec;
import cybercat5555.faunus.util.FaunusID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

public record PunchAirPacket() implements CustomPayload {

    public static final Identifier IDENTIFIER = FaunusID.content("punch_air");
    public static final CustomPayload.Id<PunchAirPacket> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, PunchAirPacket> CODEC = PacketCodec.unit(new PunchAirPacket());
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
