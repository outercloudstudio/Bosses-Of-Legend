package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ConvertGoalPacket implements FabricPacket {
    public static final PacketType<ConvertGoalPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "convert_goal"), ConvertGoalPacket::new);

    public int index;

    public ConvertGoalPacket(int index) {
        this.index = index;
    }

    public ConvertGoalPacket(PacketByteBuf buf) {
        this.index = buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(this.index);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
