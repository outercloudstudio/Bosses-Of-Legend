package outercloud.bol.goals;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import outercloud.bol.BossScreen;
import outercloud.bol.BossesOfLegend;
import outercloud.bol.goals.conditions.Condition;
import outercloud.bol.goals.conditions.ConditionDeserializers;
import outercloud.bol.goals.conditions.ConditionUIs;

import java.util.ArrayList;
import java.util.Arrays;

public class OpenGoalScreen {
    public BossScreen screen;
    public NbtCompound nbt;
    public int index;
    public int nextY = 32;

    private TextWidget title;
    private ArrayList<Element> elements = new ArrayList<>();
    private ArrayList<Element> conditionElements = new ArrayList<>();
    private int conditionY;

    public OpenGoalScreen(BossScreen screen, NbtCompound nbt, int index) {
        this.screen = screen;
        this.nbt = nbt;
        this.index = index;

        this.title = add(new TextWidget(screen.width / 2 - 60, 12, 120, 18, Text.of("Goal"), screen.getTextRenderer()));
    }

    public void setTitle(String title) {
        this.title.setMessage(Text.of(title));
    }

    public <T extends Drawable & Element & Selectable> T add(T element) {
        screen.addDrawableChild(element);

        elements.add(element);

        return element;
    }

    public <T extends Drawable & Element & Selectable> T addConditionElement(T element) {
        conditionElements.add(element);

        return add(element);
    }

    public <T extends Drawable & Element & Selectable> void addTextProperty(String key, String name) {
        TextWidget labelWidget = add(new TextWidget(screen.width / 2 - 128 - 64, nextY, 120, 18, Text.of(name), screen.getTextRenderer()));
        labelWidget.alignRight();

        TextFieldWidget inputWidget = new TextFieldWidget(screen.getTextRenderer(), screen.width / 2 - 64, nextY, 128, 16, Text.of(""));
        inputWidget.setText(nbt.getCompound("data").getString(key));

        inputWidget.setChangedListener(text -> {
            nbt.getCompound("data").putString(key, text);

            screen.editGoal(nbt.getCompound("data"), index);
        });

        add(inputWidget);

        nextY += inputWidget.getHeight() + 4;
    }

    public <T extends Drawable & Element & Selectable> void addIntProperty(String key, String name) {
        TextWidget labelWidget = add(new TextWidget(screen.width / 2 - 128 - 64, nextY, 120, 18, Text.of(name), screen.getTextRenderer()));
        labelWidget.alignRight();

        TextFieldWidget inputWidget = new TextFieldWidget(screen.getTextRenderer(), screen.width / 2 - 64, nextY, 128, 16, Text.of(""));
        inputWidget.setText(String.valueOf(nbt.getCompound("data").getInt(key)));

        inputWidget.setTextPredicate(text -> text.isEmpty() || Arrays.stream(text.split("")).filter("0123456789"::contains).toList().size() == text.length());

        inputWidget.setChangedListener(text -> {
            if(text.isEmpty())  text = "0";

            nbt.getCompound("data").putInt(key, Integer.parseInt(text));

            screen.editGoal(nbt.getCompound("data"), index);
        });

        add(inputWidget);

        nextY += inputWidget.getHeight() + 4;
    }

    public void addConditionGroup() {
        NbtList nbtList = nbt.getCompound("data").getList("conditionGroup", NbtElement.COMPOUND_TYPE);

        add(new TextWidget(screen.width / 2 - 64, nextY, 120, 18, Text.of("Conditions:"), screen.getTextRenderer())).alignLeft();
        nextY += 18;

        TextFieldWidget addTextField = new TextFieldWidget(screen.getTextRenderer(), screen.width / 2 - 64 - 8, nextY, 128, 16, Text.of(""));
        add(addTextField);

        add(ButtonWidget.builder(Text.of("+"), widget -> {
            NbtCompound conditionNbt = new NbtCompound();
            conditionNbt.putBoolean("DEFAULT", true);
            conditionNbt.putString("identifier", addTextField.getText());

            Condition condition = ConditionDeserializers.deserialize(conditionNbt);

            if(condition == null) return;

            nbt.getCompound("data").getList("conditionGroup", NbtElement.COMPOUND_TYPE).add(condition.serialize());

            screen.editGoal(nbt.getCompound("data"), index);

            reloadConditionElements();
        }).dimensions(screen.width / 2 + 56 + 4, nextY, 16, 16).build());

        nextY += 24;

        conditionY = nextY;

        createConditionElements();
    }

    private void createConditionElements() {
        NbtList nbtList = nbt.getCompound("data").getList("conditionGroup", NbtElement.COMPOUND_TYPE);

        for(int index = 0; index < nbtList.size(); index++) {
            ConditionUIs.create(this, nbtList.getCompound(index), index);
        }
    }

    private void destroyConditionElements() {
        for(Element element: conditionElements) {
            screen.remove(element);
        }

        nextY = conditionY;
    }

    public void reloadConditionElements() {
        destroyConditionElements();
        createConditionElements();
    }

    public void destroy() {
        for(Element element: elements) {
            screen.remove(element);
        }
    }
}
