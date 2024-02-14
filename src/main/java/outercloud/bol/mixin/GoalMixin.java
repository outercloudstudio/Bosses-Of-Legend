package outercloud.bol.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import outercloud.bol.goals.SerializableGoal;

@Mixin(Goal.class)
public class GoalMixin implements SerializableGoal {
    @Override
    public NbtCompound serialize() {
        throw new NotImplementedException();
    }

    @Override
    public void deserialize(NbtCompound nbt) {}

    @Override
    public Identifier getIdentifier() {
        return new Identifier("minecraft", this.getClass().getSimpleName());
    }
}
