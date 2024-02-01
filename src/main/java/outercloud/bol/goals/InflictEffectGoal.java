package outercloud.bol.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import outercloud.bol.BossesOfLegend;

import java.util.EnumSet;

public class InflictEffectGoal extends Goal {
    private final MobEntity mob;
    
    public InflictEffectGoal(MobEntity mob) {
        this.mob = mob;
        setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (mob.getRandom().nextInt(toGoalTicks(100)) != 0) return false;

        LivingEntity livingEntity = mob.getTarget();
        return livingEntity != null && livingEntity.isAlive() && mob.canTarget(livingEntity);
    }

    @Override
    public void start() {
        mob.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 5, 4), mob);

        ServerWorld world = (ServerWorld) mob.getWorld();
        world.spawnParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 3, mob.getZ(), 1, 0, 0, 0, 0);
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }
}
