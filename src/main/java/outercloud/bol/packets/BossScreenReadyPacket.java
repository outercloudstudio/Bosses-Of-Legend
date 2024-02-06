package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class BossScreenReadyPacket implements FabricPacket {
    public static final PacketType<BossScreenReadyPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "screen_ready"), BossScreenReadyPacket::new);

    public BossScreenReadyPacket() {}

    public BossScreenReadyPacket(PacketByteBuf buf) {}

    public void write(PacketByteBuf buf) {}

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
