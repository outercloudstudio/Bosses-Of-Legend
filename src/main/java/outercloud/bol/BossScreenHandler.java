package outercloud.bol;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import outercloud.bol.goals.GoalSerializer;
import outercloud.bol.mixinBridge.MobEntityMixinBridge;
import outercloud.bol.packets.*;

import java.util.ArrayList;

public class BossScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<BossScreenHandler> TYPE = new ScreenHandlerType<BossScreenHandler>(BossScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    public static final Identifier IDENTIFIER = new Identifier("bosses_of_legend", "boss_screen");

    private ServerPlayerEntity player;
    private MobEntity entity;

    protected BossScreenHandler(int syncId, ServerPlayerEntity player, MobEntity entity) {
        super(TYPE, syncId);

        this.player = player;
        this.entity = entity;

        ServerPlayNetworking.registerReceiver(player.networkHandler, BossScreenReadyPacket.TYPE, this::receiveReady);
        ServerPlayNetworking.registerReceiver(player.networkHandler, DeleteGoalPacket.TYPE, this::receiveDeleteGoal);
        ServerPlayNetworking.registerReceiver(player.networkHandler, ConvertGoalPacket.TYPE, this::receiveConvertGoal);
        ServerPlayNetworking.registerReceiver(player.networkHandler, EditGoalPacket.TYPE, this::receiveEditGoal);
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

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if(player.getWorld().isClient) return;

        ServerPlayNetworking.unregisterReceiver(this.player.networkHandler, BossScreenReadyPacket.TYPE);
        ServerPlayNetworking.unregisterReceiver(this.player.networkHandler, DeleteGoalPacket.TYPE);
        ServerPlayNetworking.unregisterReceiver(this.player.networkHandler, ConvertGoalPacket.TYPE);
        ServerPlayNetworking.unregisterReceiver(this.player.networkHandler, EditGoalPacket.TYPE);
    }

    private void sendData(ServerPlayerEntity player) {
        ArrayList<NbtCompound> goalsData = new ArrayList<>();

        GoalSelector goalSelector = ((MobEntityMixinBridge) entity).getGoalSelector();

        for(PrioritizedGoal prioritizedGoal: goalSelector.getGoals()) {
            int priority = prioritizedGoal.getPriority();
            Goal goal = prioritizedGoal.getGoal();

            NbtCompound compound = new NbtCompound();
            compound.putInt("priority", priority);
            compound.putString("name", goal.getClass().getSimpleName());
            compound.putBoolean("original", ((MobEntityMixinBridge) entity).getGoalIsOriginal(prioritizedGoal));

            NbtCompound data = GoalSerializer.serialize(prioritizedGoal);

            compound.put("data", data);
            compound.putString("identifier", data.getString("identifier"));

            goalsData.add(compound);
        }

        ServerPlayNetworking.send(player, new BossScreenDataPacket(goalsData));
    }

    private void receiveReady(BossScreenReadyPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        sendData(player);
    }

    private void receiveDeleteGoal(DeleteGoalPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        GoalSelector goalSelector = ((MobEntityMixinBridge) entity).getGoalSelector();

        ((MobEntityMixinBridge) entity).removeGoal(goalSelector.getGoals().stream().toList().get(packet.index));

        sendData(player);
    }

    private void receiveConvertGoal(ConvertGoalPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        GoalSelector goalSelector = ((MobEntityMixinBridge) entity).getGoalSelector();

        ((MobEntityMixinBridge) entity).convertGoal(goalSelector.getGoals().stream().toList().get(packet.index));

        sendData(player);
    }

    private void receiveEditGoal(EditGoalPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        BossesOfLegend.LOGGER.info(packet.nbt.toString());

        GoalSelector goalSelector = ((MobEntityMixinBridge) entity).getGoalSelector();

        ((MobEntityMixinBridge) entity).editGoal(packet.nbt, goalSelector.getGoals().stream().toList().get(packet.index));
    }
}
