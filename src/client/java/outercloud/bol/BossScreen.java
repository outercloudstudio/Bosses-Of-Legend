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
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;
import outercloud.bol.goals.GoalUi;
import outercloud.bol.mixin.client.ScreenMixin;
import outercloud.bol.packets.*;

import java.util.ArrayList;

public class BossScreen extends HandledScreen<BossScreenHandler> {
    private int goalsToDisplay;
    private ArrayList<NbtCompound> goals;

    private ArrayList<Element> goalListScreenElements = new ArrayList<>();
    private int goalListStartingIndex = 0;

    private int openGoalIndex = -1;
    private ArrayList<Element> openGoalElements = new ArrayList<>();

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
        this.goals = packet.goals;

        createGoalListScreen();
    }

    private void createGoalListScreen() {
        int goalsBoxHeight = height - 32;
        int goalsFit = Math.min(Math.max(goalsBoxHeight / 18, 1), goals.size() - goalListStartingIndex);
        int goalsBoxStart = 16;

        for(int goalListIndex = 0; goalListIndex < goalsFit; goalListIndex++) {
            int index = goalListIndex;

            NbtCompound goalData = this.goals.get(goalListStartingIndex + goalListIndex);
            String name = goalData.getString("name");
            boolean original = goalData.getBoolean("original");

            int rowX = width / 2 - 128;

            goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of(name), widget -> {

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

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Done"), widget -> {
            close();
        }).dimensions(width / 2, goalsBoxStart, 64, 16).build()));

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("<"), widget -> {

        }).dimensions(width / 2 - 64, goalsBoxStart, 32, 16).build()));

        goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of(">"), widget -> {

        }).dimensions(width / 2 - 32, goalsBoxStart, 32, 16).build()));
    }

    private void destroyGoalListScreen() {
        for(Element element: goalListScreenElements) {
            remove(element);
        }

        goalListScreenElements.clear();
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

    private void createGoalButtons() {
        destroyGoalButtons();

        goalsToDisplay = Math.max((height - 64) / 24, 1);

        for(int index = 0; index < Math.min(this.goals.size(), goalsToDisplay); index++) {
            NbtCompound goalData = this.goals.get(index);
            String name = goalData.getString("name");
            boolean original = goalData.getBoolean("original");

            int x = width / 2 - (128 + 8 + 64 + 8 + 64) / 2;
            int y = 16 + 16 + index * 24;

            int currentIndex = index;

            if(!original) {
                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of(name), widget -> {
                    openGoal(currentIndex);
                }).dimensions(x, y, 128, 16).build()));
                x += 128 + 8;

                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Duplicate"), widget -> {

                }).dimensions(x, y, 64, 16).build()));
                x += 64 + 8;

                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Delete"), widget -> {
                    ClientPlayNetworking.send(new DeleteGoalPacket(currentIndex));

                    goals = new ArrayList<>();

                    createGoalButtons();
                }).dimensions(x, y, 64, 16).build()));
            } else {
                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Default: " + name), widget -> {

                }).dimensions(x, y, 128, 16).build()));
                x += 128 + 8;

                goalListScreenElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Convert"), widget -> {
                    ClientPlayNetworking.send(new ConvertGoalPacket(currentIndex));

                    goals = new ArrayList<>();

                    createGoalButtons();
                }).dimensions(x, y, 64, 16).build()));
                x += 64 + 8;
            }
        }
    }

    private void destroyGoalButtons() {
        for(Element element: goalListScreenElements) {
            remove(element);
        }
    }

    private void openGoal(int index) {
        destroyGoalButtons();

        openGoalIndex = index;

        GoalUi.create(this, goals.get(index), index);
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public <T extends Drawable & Element & Selectable> void addOpenGoalElement(T element) {
        addDrawableChild(element);
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
}
