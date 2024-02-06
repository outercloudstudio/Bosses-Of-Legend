package outercloud.bol;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Commands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("bosses")
            .requires(source -> source.hasPermissionLevel(4))
            .then(CommandManager.argument("target", EntityArgumentType.entity())
                .executes(context -> {
                    if(context.getSource().getPlayer() == null) return -1;

                    Entity entity = EntityArgumentType.getEntity(context, "target");

                    if(entity == null) return  -1;

                    if(!(entity instanceof MobEntity)) return -1;

                    context.getSource().getPlayer().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntity) -> new BossScreenHandler(syncId, context.getSource().getPlayer(), (MobEntity) entity), Text.of("Edit Boss")));

                    return 1;
                })
            )
        );
    }
}
