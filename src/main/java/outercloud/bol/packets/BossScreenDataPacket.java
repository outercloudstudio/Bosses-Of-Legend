package outercloud.bol.packets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class BossScreenDataPacket implements FabricPacket {
    public static final PacketType<BossScreenDataPacket> TYPE = PacketType.create(new Identifier("bosses_of_legend", "screen_data"), BossScreenDataPacket::new);

    public ArrayList<NbtCompound> goals;

    public BossScreenDataPacket(ArrayList<NbtCompound> goals) {
        this.goals = goals;
    }

    public BossScreenDataPacket(PacketByteBuf buf) {
        int count = buf.readInt();

        goals = new ArrayList<>();

        for(int index = 0; index < count; index++) {
            goals.add(buf.readNbt());
        }
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(goals.size());

        for(NbtCompound goal: goals) {
            buf.writeNbt(goal);
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
