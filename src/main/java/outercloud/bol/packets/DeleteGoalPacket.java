package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class DeleteGoalPacket implements FabricPacket {
    public static final PacketType<DeleteGoalPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "delete_goal"), DeleteGoalPacket::new);

    public int index;

    public DeleteGoalPacket(int index) {
        this.index = index;
    }

    public DeleteGoalPacket(PacketByteBuf buf) {
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
