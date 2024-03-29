package outercloud.bol.goals.conditions;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;

public class ConditionGroup {
    private ArrayList<Condition> conditions = new ArrayList<>();

    public ConditionGroup() {}

    public ConditionGroup(NbtList nbt) {
        for(int index = 0; index < nbt.size(); index++) {
            conditions.add(ConditionDeserializers.deserialize(nbt.getCompound(index)));
        }
    }

    public void add(Condition condition) {
        conditions.add(condition);
    }

    public Condition get(int index) {
        return conditions.get(index);
    }

    public void remove(Condition condition) {
        conditions.remove(condition);
    }

    public void remove(int index) {
        conditions.remove(index);
    }

    public int indexOf(Condition condition) {
        return conditions.indexOf(condition);
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public boolean allPasses(MobEntity entity) {
        for(Condition condition: conditions) {
            if(!condition.passes(entity)) return false;
        }

        return true;
    }

    public NbtList serialize() {
        NbtList nbt = new NbtList();

        for(Condition condition: conditions) {
            nbt.add(condition.serialize());
        }

        return nbt;
    }
}
