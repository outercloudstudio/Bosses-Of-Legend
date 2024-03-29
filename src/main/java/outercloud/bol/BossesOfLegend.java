package outercloud.bol;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outercloud.bol.goals.EmptyGoal;
import outercloud.bol.goals.GoalDeserializers;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.goals.conditions.ConditionDeserializers;
import outercloud.bol.goals.conditions.HealthCondition;

public class BossesOfLegend implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bol");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(Commands::register);

		BossScreenHandler.register();

		GoalDeserializers.register(EmptyGoal.IDENTIFIER,  EmptyGoal::deserialize);
		GoalDeserializers.register(InflictEffectGoal.IDENTIFIER,  InflictEffectGoal::deserialize);

		ConditionDeserializers.register(HealthCondition.IDENTIFIER, HealthCondition::deserialize);
	}
}