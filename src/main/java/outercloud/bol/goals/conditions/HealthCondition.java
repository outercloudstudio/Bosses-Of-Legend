package outercloud.bol.goals.conditions;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class HealthCondition implements Condition {
    public enum Operator {
        LessThan,
        LessThanEqual,
        Equal,
        GreaterThan,
        GreaterThanEqual,
    }

    public static Identifier IDENTIFIER = new Identifier("bol", "health_condition");

    private float limit;
    private Operator operator;

    public HealthCondition(float limit, Operator operator) {
        this.limit = limit;
        this.operator = operator;
    }

    @Override
    public boolean passes(MobEntity entity) {
        return entity.getHealth() < 10;
    }

    @Override
    public NbtCompound serialize() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("identifier", IDENTIFIER.toString());

        nbt.putFloat("limit", limit);

        if(operator == Operator.LessThan) {
            nbt.putString("operator", "lessThan");
        } else if(operator == Operator.LessThanEqual) {
            nbt.putString("operator", "lessThanEqual");
        } else if(operator == Operator.Equal) {
            nbt.putString("operator", "equal");
        } else if(operator == Operator.GreaterThan) {
            nbt.putString("operator", "greaterThan");
        } else if(operator == Operator.GreaterThanEqual) {
            nbt.putString("operator", "greaterThanEqual");
        }

        return nbt;
    }

    public static Condition deserialize(NbtCompound nbt) {
        float limit = nbt.getFloat("limit");

        Operator operator = Operator.LessThan;

        String readOperator = nbt.getString("operator");

        if(readOperator.equals("lessThanEqual")) {
            operator = Operator.LessThanEqual;
        } else if(readOperator.equals("Equal")) {
            operator = Operator.Equal;
        } else if(readOperator.equals("greaterThan")) {
            operator = Operator.GreaterThan;
        } else if(readOperator.equals("greaterThanEqual")) {
            operator = Operator.GreaterThanEqual;
        }

        return new HealthCondition(limit, operator);
    }
}
