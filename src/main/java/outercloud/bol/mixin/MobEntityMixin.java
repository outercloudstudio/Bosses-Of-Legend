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
import outercloud.bol.BossesOfLegend;
import outercloud.bol.goals.InflictEffectGoal;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
	@Accessor
	abstract GoalSelector getGoalSelector();

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	private void init(CallbackInfo info) {
		MobEntity me = (MobEntity) (Object) this;

		if (me.getWorld() == null || me.getWorld().isClient) return;

		GoalSelector goalSelector = getGoalSelector();
		goalSelector.add(1, new InflictEffectGoal(me));
	}
}