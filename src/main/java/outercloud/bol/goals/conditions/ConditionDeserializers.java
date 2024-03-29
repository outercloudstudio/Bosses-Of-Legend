package outercloud.bol.goals.conditions;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import outercloud.bol.BossesOfLegend;

import java.util.HashMap;
import java.util.function.Function;

public class ConditionDeserializers {
    private static HashMap<Identifier, Function<NbtCompound, Condition>> registry = new HashMap<>();

    public static void register(Identifier identifier, Function<NbtCompound, Condition> deserializer) {
        registry.put(identifier, deserializer);
    }

    public static Condition deserialize(NbtCompound nbt) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        if(!registry.containsKey(identifier)) {
            BossesOfLegend.LOGGER.error("Could not deserialize condition with identifier " + identifier + " because it has not been registered!");

            return null;
        }

        Function<NbtCompound, Condition> deserializer = registry.get(identifier);

        return deserializer.apply(nbt);
    }
}
