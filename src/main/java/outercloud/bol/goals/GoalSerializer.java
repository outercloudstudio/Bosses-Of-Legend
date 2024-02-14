package outercloud.bol.goals;

import com.mojang.datafixers.types.Func;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import outercloud.bol.BossesOfLegend;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class GoalSerializer {
    private static HashMap<Identifier, BiFunction<MobEntity, NbtCompound, SerializableGoal>> deserializerRegistry = new HashMap<>();

    public static void register(Identifier identifier, BiFunction<MobEntity, NbtCompound, SerializableGoal> deserializer) {
        deserializerRegistry.put(identifier, deserializer);
    }

    public static NbtCompound serialize(PrioritizedGoal prioritizedGoal) {
        NbtCompound data = ((SerializableGoal) prioritizedGoal.getGoal()).serialize();
        data.putInt("priority", prioritizedGoal.getPriority());

        Identifier identifier = ((SerializableGoal) prioritizedGoal.getGoal()).getIdentifier();

        data.putString("identifier", identifier.toString());

        return  data;
    }

    public static PrioritizedGoal deserialize(MobEntity mobEntity, NbtCompound nbt) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        Map.Entry<Identifier, BiFunction<MobEntity, NbtCompound, SerializableGoal>> deserializer = deserializerRegistry.entrySet().stream().filter(entry -> entry.getKey().equals(identifier)).findFirst().orElse(null);

        if(deserializer == null) {
            BossesOfLegend.LOGGER.error("Could not deserialize goal with identifier " + identifier + " because it has not been registered!");

            return null;
        }

        Goal goal = (Goal) deserializer.getValue().apply(mobEntity, nbt);

        return  new PrioritizedGoal(nbt.getInt("priority"), goal);
    }
}
