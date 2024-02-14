package outercloud.bol.mixinBridge;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

public interface MobEntityMixinBridge {
    GoalSelector getGoalSelector();

    boolean getGoalIsOriginal(PrioritizedGoal prioritizedGoal);

    void removeGoal(PrioritizedGoal prioritizedGoal);

    void convertGoal(PrioritizedGoal prioritizedGoal);

    void addGoal(PrioritizedGoal prioritizedGoal);
}
