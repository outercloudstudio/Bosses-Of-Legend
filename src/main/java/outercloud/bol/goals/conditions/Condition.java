package outercloud.bol.goals.conditions;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;

public interface Condition {
    boolean passes(MobEntity entity);

    NbtCompound serialize();
}
