package outercloud.bol.mixinBridge;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.nbt.NbtCompound;

public interface MobEntityMixinBridge {
    GoalSelector getGoalSelector();

    boolean getGoalIsOriginal(PrioritizedGoal prioritizedGoal);

    void removeGoal(PrioritizedGoal prioritizedGoal);

    void convertGoal(PrioritizedGoal prioritizedGoal);

    void editGoal(NbtCompound nbt, PrioritizedGoal prioritizedGoal);

    void addGoal(PrioritizedGoal prioritizedGoal);
}
