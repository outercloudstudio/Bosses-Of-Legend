package outercloud.bol.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Identifier;

public class EmptyGoal extends Goal implements SerializableGoal {
    public static final Identifier IDENTIFIER = new Identifier("bol", "empty");

    @Override
    public boolean canStart() {
        return false;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
