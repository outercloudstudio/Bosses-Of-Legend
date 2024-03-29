package outercloud.bol.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import outercloud.bol.goals.conditions.ConditionGroup;

import java.util.EnumSet;

public class InflictEffectGoal extends Goal implements SerializableGoal {
    public static final Identifier IDENTIFIER = new Identifier("bol", "inflict_effect");

    private final MobEntity mob;

    private Identifier effect;
    private int chance;
    private String command;
    private int duration;
    private int amplifier;
    private ConditionGroup conditionGroup;

    public InflictEffectGoal(MobEntity mob, Identifier effect, int chance, String command, int duration, int amplifier, ConditionGroup conditionGroup) {
        this.mob = mob;
        this.effect = effect;
        this.chance = chance;
        this.command = command;
        this.duration = duration;
        this.amplifier = amplifier;
        this.conditionGroup = conditionGroup;

        setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (mob.getRandom().nextInt(toGoalTicks(Math.max(chance, 1))) != 0) return false;

        LivingEntity livingEntity = mob.getTarget();
        return livingEntity != null && livingEntity.isAlive() && mob.canTarget(livingEntity);
    }

    @Override
    public void start() {
        if(mob.getTarget() == null) return;

        StatusEffect statusEffect = Registries.STATUS_EFFECT.get(effect);

        if(statusEffect == null) return;

        mob.getTarget().addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier), mob);

        MinecraftServer server = mob.getServer();

        if(!command.isEmpty()) server.getCommandManager().executeWithPrefix(server.getCommandSource().withEntity(mob).withLevel(4), command);
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
        nbt.putInt("chance", chance);
        nbt.putString("command", command);
        nbt.putInt("duration", duration);
        nbt.putInt("amplifier", amplifier);
        nbt.put("conditionGroup", this.conditionGroup.serialize());

        return nbt;
    }

    public static SerializableGoal deserialize(MobEntity mobEntity, NbtCompound nbt) {
        return new InflictEffectGoal(mobEntity, new Identifier(nbt.getString("effect")), nbt.getInt("chance"), nbt.getString("command"), nbt.getInt("duration"), nbt.getInt("amplifier"), new ConditionGroup(nbt.getList("conditionGroup", NbtElement.COMPOUND_TYPE)));
    }
}
