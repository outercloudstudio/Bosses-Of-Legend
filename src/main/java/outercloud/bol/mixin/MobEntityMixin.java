package outercloud.bol.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import outercloud.bol.BossesOfLegend;
import outercloud.bol.goals.GoalSerializer;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.goals.SerializableGoal;
import outercloud.bol.mixinBridge.MobEntityMixinBridge;

import java.util.ArrayList;
import java.util.List;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements MobEntityMixinBridge {
	@Accessor
	public abstract GoalSelector getGoalSelector();

	private ArrayList<PrioritizedGoal> originalGoals = new ArrayList<PrioritizedGoal>();
	private ArrayList<Integer> convertedGoals = new ArrayList<Integer>();
	private ArrayList<PrioritizedGoal> customGoals = new ArrayList<>();

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	private void init(CallbackInfo info) {
		MobEntity me = (MobEntity) (Object) this;

		if (me.getWorld() == null || me.getWorld().isClient) return;

		GoalSelector goalSelector = getGoalSelector();

		originalGoals.addAll(goalSelector.getGoals());

//		goalSelector.add(1, new InflictEffectGoal(me));
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putIntArray("convertedGoals", convertedGoals);

		NbtList goalDatas = new NbtList();

		for(PrioritizedGoal prioritizedGoal: customGoals) {
			NbtCompound goalData = GoalSerializer.serialize(prioritizedGoal);

			goalDatas.add(goalData);
		}

		nbt.put("customGoals", goalDatas);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	private void readNbt(NbtCompound nbt, CallbackInfo ci) {
		convertedGoals = new ArrayList<>();

		GoalSelector goalSelector = getGoalSelector();

		int[] convertedGoalIndexes = nbt.getIntArray("convertedGoals");

		if(convertedGoalIndexes != null) {
			ArrayList<PrioritizedGoal> goalsToRemove = new ArrayList<>();

			for(int index: convertedGoalIndexes) {
				convertedGoals.add(index);

				goalsToRemove.add(originalGoals.get(index));
			}

			for(PrioritizedGoal goal: goalsToRemove) {
				goalSelector.remove(goal.getGoal());
			}
		}

		NbtList customGoalDatas = (NbtList) nbt.get("customGoals");

		if(customGoalDatas != null) {
			for(NbtElement nbtElement: customGoalDatas) {
				PrioritizedGoal deserializedGoal = GoalSerializer.deserialize((NbtCompound) nbtElement);

				if(deserializedGoal == null) {
					BossesOfLegend.LOGGER.error("Failed to serialized goal when loading: " + ((NbtCompound) nbtElement).getString("identifier"));

					continue;
				}

				goalSelector.add(deserializedGoal.getPriority(), deserializedGoal.getGoal());
			}
		}
	}

	public boolean getGoalIsOriginal(PrioritizedGoal prioritizedGoal){
		return originalGoals.contains(prioritizedGoal) && !convertedGoals.contains(originalGoals.indexOf(prioritizedGoal));
	}

	public void removeGoal(PrioritizedGoal prioritizedGoal) {
		GoalSelector goalSelector = getGoalSelector();

		goalSelector.remove(prioritizedGoal.getGoal());

		customGoals.remove(prioritizedGoal);

		BossesOfLegend.LOGGER.info("Removed: " + prioritizedGoal.getGoal().getClass().getSimpleName());
	}

	public void convertGoal(PrioritizedGoal prioritizedGoal) {
		convertedGoals.add(originalGoals.indexOf(prioritizedGoal));

		GoalSelector goalSelector = getGoalSelector();

		goalSelector.remove(prioritizedGoal.getGoal());

		NbtCompound serializedGoal = GoalSerializer.serialize(prioritizedGoal);
		PrioritizedGoal deserializedGoal = GoalSerializer.deserialize(serializedGoal);

		if(deserializedGoal == null) {
			BossesOfLegend.LOGGER.error("Failed to serialized goal when converting: " + prioritizedGoal.getGoal().getClass().getSimpleName());

			return;
		}

		customGoals.add(deserializedGoal);

		goalSelector.add(deserializedGoal.getPriority(), deserializedGoal.getGoal());

		BossesOfLegend.LOGGER.info("Converted: " + prioritizedGoal.getGoal().getClass().getSimpleName());
	}
}