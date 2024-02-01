package outercloud.bol.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import outercloud.bol.BossesOfLegend;

import java.util.EnumSet;

public class InflictEffectGoal extends Goal {
    private final MobEntity mob;

    public InflictEffectGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.mob.canTarget(livingEntity);
    }

    @Override
    public void start() {
        BossesOfLegend.LOGGER.info("Inflict Effect");
    }
}
