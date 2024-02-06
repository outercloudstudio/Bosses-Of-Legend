package outercloud.bol;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import outercloud.bol.mixin.client.ScreenMixin;
import outercloud.bol.packets.BossScreenDataPacket;
import outercloud.bol.packets.BossScreenReadyPacket;
import outercloud.bol.packets.DeleteGoalPacket;

import java.util.ArrayList;

public class BossScreen extends HandledScreen<BossScreenHandler> {
    private int goalsToDisplay;
    private MobEntity mobEntity;
    private ArrayList<NbtCompound> goals;
    private ArrayList<Element> goalElements = new ArrayList<>();

    public BossScreen(BossScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        ClientPlayNetworking.send(new BossScreenReadyPacket());
    }

    public static void register() {
        HandledScreens.register(BossScreenHandler.TYPE, BossScreen::new);
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.of("Done"), widget -> {
            close();
        }).dimensions(width / 2 - 32, height - 24, 64, 16).build());

        addDrawableChild(ButtonWidget.builder(Text.of("<"), widget -> {

        }).dimensions(width / 4 - 32, height - 24, 16, 16).build());

        addDrawableChild(ButtonWidget.builder(Text.of("+"), widget -> {

        }).dimensions(width / 4 - 8, height - 24, 16, 16).build());

        addDrawableChild(ButtonWidget.builder(Text.of(">"), widget -> {

        }).dimensions(width / 4 + 16, height - 24, 16, 16).build());

        ClientPlayNetworking.registerReceiver(BossScreenDataPacket.TYPE, this::recieveData);
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

    @Override
    public void close() {
        super.close();

        ClientPlayNetworking.unregisterReceiver(BossScreenDataPacket.TYPE);
    }

    private void recieveData(BossScreenDataPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
        this.goals = packet.goals;

        createGoalButtons();
    }

    private void createGoalButtons() {
        for(Element element: goalElements) {
            remove(element);
        }

        goalsToDisplay = Math.max((height - 64) / 24, 1);

        for(int index = 0; index < Math.min(this.goals.size(), goalsToDisplay); index++) {
            NbtCompound goalData = this.goals.get(index);
            String name = goalData.getString("name");

            int x = width / 2 - (128 + 8 + 64 + 8 + 64) / 2;
            int y = 16 + 16 + index * 24;

            goalElements.add(addDrawableChild(ButtonWidget.builder(Text.of(name), widget -> {

            }).dimensions(x, y, 128, 16).build()));
            x += 128 + 8;

            goalElements.add(addDrawableChild(ButtonWidget.builder(Text.of("Duplicate"), widget -> {

            }).dimensions(x, y, 64, 16).build()));
            x += 64 + 8;

            int currentIndex = index;
            goalElements.add( addDrawableChild(ButtonWidget.builder(Text.of("Delete"), widget -> {
                ClientPlayNetworking.send(new DeleteGoalPacket(currentIndex));

                goals.remove(currentIndex);

                createGoalButtons();
            }).dimensions(x, y, 64, 16).build()));
        }
    }
}
