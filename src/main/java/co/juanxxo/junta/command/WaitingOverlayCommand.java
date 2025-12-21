package co.juanxxo.junta.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WaitingOverlayCommand {
    public static final Identifier SHOW_WAITING_OVERLAY_ID = Identifier.of("junta", "show_waiting_overlay");
    public static final Identifier HIDE_WAITING_OVERLAY_ID = Identifier.of("junta", "hide_waiting_overlay");
    
    public record ShowWaitingPayload(int maxPlayers) implements CustomPayload {
        public static final CustomPayload.Id<ShowWaitingPayload> ID = new CustomPayload.Id<>(SHOW_WAITING_OVERLAY_ID);
        public static final PacketCodec<RegistryByteBuf, ShowWaitingPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ShowWaitingPayload::maxPlayers,
            ShowWaitingPayload::new
        );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public record HideWaitingPayload() implements CustomPayload {
        public static final CustomPayload.Id<HideWaitingPayload> ID = new CustomPayload.Id<>(HIDE_WAITING_OVERLAY_ID);
        public static final PacketCodec<RegistryByteBuf, HideWaitingPayload> CODEC = PacketCodec.unit(new HideWaitingPayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("jntcore")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("overlay")
                .then(CommandManager.literal("waiting")
                    .then(CommandManager.literal("show")
                        .then(CommandManager.argument("maxPlayers", IntegerArgumentType.integer(1, 1000))
                            .executes(WaitingOverlayCommand::showOverlay)
                        )
                    )
                    .then(CommandManager.literal("hide")
                        .executes(WaitingOverlayCommand::hideOverlay)
                    )
                )
            )
        );
    }
    
    private static int showOverlay(CommandContext<ServerCommandSource> context) {
        int maxPlayers = IntegerArgumentType.getInteger(context, "maxPlayers");
        ServerCommandSource source = context.getSource();
        
        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setWaitingOverlay(true, maxPlayers);
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new ShowWaitingPayload(maxPlayers));
        }
        
        source.sendFeedback(() -> Text.literal("Overlay de espera activado para " + maxPlayers + " jugadores máximo"), true);
        return 1;
    }
    
    private static int hideOverlay(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setWaitingOverlay(false, 0);
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new HideWaitingPayload());
        }
        
        source.sendFeedback(() -> Text.literal("Overlay de espera desactivado"), true);
        return 1;
    }
}
