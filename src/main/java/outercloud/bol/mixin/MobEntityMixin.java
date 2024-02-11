package outercloud.bol.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import outercloud.bol.BossesOfLegend;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.mixinBridge.MobEntityMixinBridge;

import java.util.ArrayList;
import java.util.List;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements MobEntityMixinBridge {
	@Accessor
	public abstract GoalSelector getGoalSelector();

	private ArrayList<PrioritizedGoal> originalGoals = new ArrayList<PrioritizedGoal>();
	private ArrayList<Integer> convertedGoals = new ArrayList<Integer>();

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	private void init(CallbackInfo info) {
		MobEntity me = (MobEntity) (Object) this;

		if (me.getWorld() == null || me.getWorld().isClient) return;

		GoalSelector goalSelector = getGoalSelector();

		originalGoals.addAll(goalSelector.getGoals());

		goalSelector.add(1, new InflictEffectGoal(me));
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putIntArray("convertedGoals", convertedGoals);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	private void readNbt(NbtCompound nbt, CallbackInfo ci) {
		convertedGoals = new ArrayList<>();

		for(int index: nbt.getIntArray("convertedGoals")) {
			convertedGoals.add(index);
		}
	}

	public boolean getGoalIsOriginal(PrioritizedGoal prioritizedGoal){
		return originalGoals.contains(prioritizedGoal) && !convertedGoals.contains(originalGoals.indexOf(prioritizedGoal));
	}

	public void convertGoal(PrioritizedGoal prioritizedGoal) {
		convertedGoals.add(originalGoals.indexOf(prioritizedGoal));

		BossesOfLegend.LOGGER.info(prioritizedGoal.getGoal().getClass().getSimpleName());
	}
}