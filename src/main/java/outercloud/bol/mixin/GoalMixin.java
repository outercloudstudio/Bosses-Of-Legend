package outercloud.bol.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import outercloud.bol.goals.EmptyGoal;
import outercloud.bol.goals.SerializableGoal;

@Mixin(Goal.class)
public class GoalMixin implements SerializableGoal {
    @Override
    public Identifier getIdentifier() {
        return EmptyGoal.IDENTIFIER;
    }
}
