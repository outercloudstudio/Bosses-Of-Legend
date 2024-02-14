package outercloud.bol;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outercloud.bol.goals.EmptyGoal;
import outercloud.bol.goals.GoalSerializer;
import outercloud.bol.goals.InflictEffectGoal;

public class BossesOfLegend implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bosses-of-legend");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(Commands::register);

		BossScreenHandler.register();

		GoalSerializer.register(EmptyGoal.IDENTIFIER,  EmptyGoal::deserialize);
		GoalSerializer.register(InflictEffectGoal.IDENTIFIER,  InflictEffectGoal::deserialize);
	}
}