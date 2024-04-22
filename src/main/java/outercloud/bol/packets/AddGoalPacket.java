package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AddGoalPacket implements FabricPacket {
    public static final PacketType<AddGoalPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "add_goal"), AddGoalPacket::new);

    public String identifier;

    public AddGoalPacket(String identifier) {
        this.identifier = identifier;
    }

    public AddGoalPacket(PacketByteBuf buf) {
        this.identifier = buf.readString();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.identifier);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
