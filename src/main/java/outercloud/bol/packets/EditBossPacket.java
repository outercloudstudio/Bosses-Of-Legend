package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class EditBossPacket implements FabricPacket {
    public static final PacketType<EditBossPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "edit_boss"), EditBossPacket::new);

    public EditBossPacket() {}

    public EditBossPacket(PacketByteBuf buf) {}

    public void write(PacketByteBuf buf) {}

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
