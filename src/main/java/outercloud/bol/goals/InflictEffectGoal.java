package outercloud.bol.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.EnumSet;

public class InflictEffectGoal extends Goal implements SerializableGoal {
    public static final Identifier IDENTIFIER = new Identifier("bol", "inflict_effect");

    private final MobEntity mob;

    private Identifier effect;

    public InflictEffectGoal(MobEntity mob, Identifier effect) {
        this.mob = mob;
        this.effect = effect;
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
        if(mob.getTarget() == null) return;

        StatusEffect statusEffect = Registries.STATUS_EFFECT.get(effect);

        if(statusEffect == null) return;

        mob.getTarget().addStatusEffect(new StatusEffectInstance(statusEffect, 20 * 5, 4), mob);

        ServerWorld world = (ServerWorld) mob.getWorld();
        world.spawnParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 3, mob.getZ(), 1, 0, 0, 0, 0);
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public NbtCompound serialize() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("effect", effect.toString());

        return nbt;
    }

    public static SerializableGoal deserialize(MobEntity mobEntity, NbtCompound nbt) {
        return new InflictEffectGoal(mobEntity, new Identifier(nbt.getString("effect")));
    }
}
