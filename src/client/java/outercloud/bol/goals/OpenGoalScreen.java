package outercloud.bol.goals;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import outercloud.bol.BossScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class OpenGoalScreen {
    public BossScreen screen;
    public NbtCompound nbt;
    public int index;
    public int nextY = 32;

    private TextWidget title;
    private ArrayList<Element> elements = new ArrayList<>();

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
            if(text.isEmpty()) {
                inputWidget.setText("0");

                return;
            }

            nbt.getCompound("data").putInt(key, Integer.parseInt(text));

            screen.editGoal(nbt.getCompound("data"), index);
        });

        add(inputWidget);

        nextY += inputWidget.getHeight() + 4;
    }

    public void destroy() {
        for(Element element: elements) {
            screen.remove(element);
        }
    }
}
