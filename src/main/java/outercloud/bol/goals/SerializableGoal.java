package outercloud.bol.goals;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface SerializableGoal {
    NbtCompound serialize();
    void deserialize(NbtCompound nbt);
    Identifier getIdentifier();
}
