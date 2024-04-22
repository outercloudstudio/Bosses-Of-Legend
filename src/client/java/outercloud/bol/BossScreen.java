package outercloud.bol;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;
import outercloud.bol.goals.GoalDeserializers;
import outercloud.bol.goals.OpenGoalUIs;
import outercloud.bol.goals.OpenGoalScreen;
import outercloud.bol.mixin.client.ScreenMixin;
import outercloud.bol.packets.*;

import java.util.ArrayList;

public class BossScreen extends HandledScreen<BossScreenHandler> {
    private ArrayList<NbtCompound> goals;

    private ArrayList<Element> goalListScreenElements = new ArrayList<>();
    private int goalListStartingIndex = 0;

    private OpenGoalScreen openGoalScreen;

    public BossScreen(BossScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        ClientPlayNetworking.registerReceiver(BossScreenDataPacket.TYPE, this::receiveData);
        ClientPlayNetworking.send(new BossScreenReadyPacket());
    }

    public static void register() {
        HandledScreens.register(BossScreenHandler.TYPE, BossScreen::new);
    }

    @Override
    protected void init() {

    }

    @Override
    public void close() {
        super.close();

        ClientPlayNetworking.unregisterReceiver(BossScreenDataPacket.TYPE);
    }

    private void receiveData(BossScreenDataPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
        goals = packet.goals;

        createGoalListScreen();
    }

    private void createGoalListScreen() {
        if(goalListStartingIndex < 0) goalListStartingIndex = 0;
        if(goalListStartingIndex >= goals.size()) goalListStartingIndex = goals.size() - 1;

        int goalsBoxHeight = height - 56;
        int maxGoalsFit = Math.max(goalsBoxHeight / 18, 1);
        int goalsFit = Math.min(maxGoalsFit, goals.size() - goalListStartingIndex);
        int goalsBoxStart = 16;

        for(int goalListIndex = 0; goalListIndex < maxGoalsFit; goalListIndex++) {
            if(goalListIndex >= goalsFit) {
                goalsBoxStart += 18;

                continue;
            }

            int index = goalListIndex;

            NbtCompound goalData = this.goals.get(goalListStartingIndex + goalListIndex);
            String name = goalData.getString("name");
            boolean original = goalData.getBoolean("original");

            int rowX = width / 2 - 128;

            goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of(name), widget -> {
                if(original) return;

                destroyGoalListScreen();

                createGoalScreen(index);
            }).dimensions(rowX, goalsBoxStart, 128, 16).build()));
            rowX += 128;

            if(original) {
                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Convert"), widget -> {
                    ClientPlayNetworking.send(new ConvertGoalPacket(index));

                    destroyGoalListScreen();
                }).dimensions(rowX, goalsBoxStart, 128, 16).build()));
            } else {
                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Duplicate"), widget -> {
                    throw new NotImplementedException();
                }).dimensions(rowX, goalsBoxStart, 64, 16).build()));
                rowX += 64;

                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Delete"), widget -> {
                    ClientPlayNetworking.send(new DeleteGoalPacket(index));

                    destroyGoalListScreen();
                }).dimensions(rowX, goalsBoxStart, 64, 16).build()));
            }

            goalsBoxStart += 18;
        }

        TextFieldWidget addTextField = new TextFieldWidget(getTextRenderer(), width / 2 - 64 - 8, goalsBoxStart, 128, 16, Text.of(""));
        goalListScreenElements.add(addDrawableChild(addTextField));

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("+"), widget -> {
            addGoal(addTextField.getText());
        }).dimensions(width / 2 + 56 + 4, goalsBoxStart, 16, 16).build()));

        goalsBoxStart += 24;

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Done"), widget -> {
            close();
        }).dimensions(width / 2, goalsBoxStart, 64, 16).build()));

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("<"), widget -> {
            goalListStartingIndex--;

            destroyGoalListScreen();
            createGoalListScreen();
        }).dimensions(width / 2 - 64, goalsBoxStart, 32, 16).build()));

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of(">"), widget -> {
            goalListStartingIndex++;

            destroyGoalListScreen();
            createGoalListScreen();
        }).dimensions(width / 2 - 32, goalsBoxStart, 32, 16).build()));
    }

    private void destroyGoalListScreen() {
        for(Element element: goalListScreenElements) {
            remove(element);
        }

        goalListScreenElements.clear();
    }

    private void createGoalScreen(int index) {
        OpenGoalScreen openGoalScreen = new OpenGoalScreen(this, goals.get(index), index);

        OpenGoalUIs.create(openGoalScreen);

        openGoalScreen.add(ButtonWidget.builder(Text.of("Back"), widget -> {
            destroyGoalScreen();

            createGoalListScreen();
        }).dimensions(width / 2 - 16, height - 20, 32, 16).build());

        this.openGoalScreen = openGoalScreen;
    }

    private void destroyGoalScreen() {
        openGoalScreen.destroy();

        openGoalScreen = null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);

        for (Drawable drawable : ((ScreenMixin) this).getDrawables()) {
            drawable.render(context, mouseX, mouseY, delta);
        }

        this.drawForeground(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {}

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    public void remove(Element child) {
        super.remove(child);
    }

    public void editGoal(NbtCompound nbt, int index) {
        ClientPlayNetworking.send(new EditGoalPacket(nbt, index));

        goals.get(index).put("data", nbt);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            client.player.closeHandledScreen();

            return true;
        }

        if (client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void addGoal(String identifier) {
        ClientPlayNetworking.send(new AddGoalPacket(identifier));
    }
}
