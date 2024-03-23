package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class EditGoalPacket implements FabricPacket {
    public static final PacketType<EditGoalPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "edit_goal"), EditGoalPacket::new);

    public NbtCompound nbt;
    public int index;

    public EditGoalPacket(NbtCompound nbt, int index) {
        this.nbt = nbt;
        this.index = index;
    }

    public EditGoalPacket(PacketByteBuf buf) {
        this.nbt = buf.readNbt();
        this.index = buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeNbt(nbt);
        buf.writeInt(this.index);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
