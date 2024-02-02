package outercloud.bol;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import outercloud.bol.mixin.client.ScreenMixin;

public class BossScreen extends HandledScreen<BossScreenHandler> {
    private int goalsToDisplay;
    private MobEntity mobEntity;

    public BossScreen(BossScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
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

        goalsToDisplay = Math.max((height - 64) / 24, 1);

        for(int row = 0; row < goalsToDisplay; row++) {
            int x = width / 4 - 64;
            int y = 16 + 16 + row * 24;

            addDrawableChild(ButtonWidget.builder(Text.of("Goal " + row), widget -> {

            }).dimensions(x, y, 128, 16).build());

            addDrawableChild(ButtonWidget.builder(Text.of("Duplicate"), widget -> {

            }).dimensions(x + 72 + 64, y, 64, 16).build());

            addDrawableChild(ButtonWidget.builder(Text.of("Delete"), widget -> {

            }).dimensions(x + 72 + 16 + 32 + 16 + 32 + 32 + 8, y, 64, 16).build());
        }
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
}
