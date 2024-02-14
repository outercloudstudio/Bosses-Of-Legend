package outercloud.bol.goals;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface SerializableGoal {
    default NbtCompound serialize() {
        return new NbtCompound();
    }

    default void deserialize(NbtCompound nbt) {}

    Identifier getIdentifier();
}
