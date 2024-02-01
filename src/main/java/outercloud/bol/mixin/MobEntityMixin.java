package outercloud.bol.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import outercloud.bol.goals.InflictEffectGoal;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Accessor
	private GoalSelector getGoalSelector() {
		throw new NotImplementedException();
	}

	@Inject(at = @At("HEAD"), method = "initGoals")
	private void initGoals(CallbackInfo info) {
		MobEntity me = (MobEntity) (Object) this;

		GoalSelector goalSelector = getGoalSelector();
		goalSelector.add(0, new InflictEffectGoal(me));
	}
}