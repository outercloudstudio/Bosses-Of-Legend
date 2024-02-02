package outercloud.bol;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class BossScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<BossScreenHandler> TYPE = new ScreenHandlerType<BossScreenHandler>(BossScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    public static final Identifier IDENTIFIER = new Identifier("bosses_of_legend", "boss_screen");

    protected BossScreenHandler(int syncId) {
        super(TYPE, syncId);
    }

    public BossScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(TYPE, syncId);
    }

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER, IDENTIFIER, TYPE);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
