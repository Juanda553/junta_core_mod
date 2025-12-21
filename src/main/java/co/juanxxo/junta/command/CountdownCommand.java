package co.juanxxo.junta.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
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

public class CountdownCommand {
    public static final Identifier START_COUNTDOWN_ID = Identifier.of("junta", "start_countdown");
    public static final Identifier ADD_TIME_ID = Identifier.of("junta", "add_countdown_time");
    public static final Identifier PAUSE_COUNTDOWN_ID = Identifier.of("junta", "pause_countdown");
    public static final Identifier RESUME_COUNTDOWN_ID = Identifier.of("junta", "resume_countdown");
    public static final Identifier STOP_COUNTDOWN_ID = Identifier.of("junta", "stop_countdown");
    
    public record StartCountdownPayload(long hours, long minutes, long seconds, String title) implements CustomPayload {
        public static final CustomPayload.Id<StartCountdownPayload> ID = new CustomPayload.Id<>(START_COUNTDOWN_ID);
        public static final PacketCodec<RegistryByteBuf, StartCountdownPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, StartCountdownPayload::hours,
            PacketCodecs.VAR_LONG, StartCountdownPayload::minutes,
            PacketCodecs.VAR_LONG, StartCountdownPayload::seconds,
            PacketCodecs.STRING, StartCountdownPayload::title,
            StartCountdownPayload::new
        );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public record AddTimePayload(long hours, long minutes, long seconds) implements CustomPayload {
        public static final CustomPayload.Id<AddTimePayload> ID = new CustomPayload.Id<>(ADD_TIME_ID);
        public static final PacketCodec<RegistryByteBuf, AddTimePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_LONG, AddTimePayload::hours,
            PacketCodecs.VAR_LONG, AddTimePayload::minutes,
            PacketCodecs.VAR_LONG, AddTimePayload::seconds,
            AddTimePayload::new
        );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public record PauseCountdownPayload() implements CustomPayload {
        public static final CustomPayload.Id<PauseCountdownPayload> ID = new CustomPayload.Id<>(PAUSE_COUNTDOWN_ID);
        public static final PacketCodec<RegistryByteBuf, PauseCountdownPayload> CODEC = PacketCodec.unit(new PauseCountdownPayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public record ResumeCountdownPayload() implements CustomPayload {
        public static final CustomPayload.Id<ResumeCountdownPayload> ID = new CustomPayload.Id<>(RESUME_COUNTDOWN_ID);
        public static final PacketCodec<RegistryByteBuf, ResumeCountdownPayload> CODEC = PacketCodec.unit(new ResumeCountdownPayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public record StopCountdownPayload() implements CustomPayload {
        public static final CustomPayload.Id<StopCountdownPayload> ID = new CustomPayload.Id<>(STOP_COUNTDOWN_ID);
        public static final PacketCodec<RegistryByteBuf, StopCountdownPayload> CODEC = PacketCodec.unit(new StopCountdownPayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("jntcore")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("countdown")
                .then(CommandManager.literal("start")
                    .then(CommandManager.argument("hours", LongArgumentType.longArg(0))
                        .then(CommandManager.argument("minutes", LongArgumentType.longArg(0))
                            .then(CommandManager.argument("seconds", LongArgumentType.longArg(0))
                                .then(CommandManager.argument("title", com.mojang.brigadier.arguments.StringArgumentType.greedyString())
                                    .executes(CountdownCommand::startCountdownWithTitle)
                                )
                                .executes(CountdownCommand::startCountdown)
                            )
                        )
                    )
                )
                .then(CommandManager.literal("add")
                    .then(CommandManager.argument("hours", LongArgumentType.longArg(0))
                        .then(CommandManager.argument("minutes", LongArgumentType.longArg(0))
                            .then(CommandManager.argument("seconds", LongArgumentType.longArg(0))
                                .executes(CountdownCommand::addTime)
                            )
                        )
                    )
                )
                .then(CommandManager.literal("pause")
                    .executes(CountdownCommand::pauseCountdown)
                )
                .then(CommandManager.literal("resume")
                    .executes(CountdownCommand::resumeCountdown)
                )
                .then(CommandManager.literal("stop")
                    .executes(CountdownCommand::stopCountdown)
                )
            )
        );
    }
    
    private static int startCountdown(CommandContext<ServerCommandSource> context) {
        long hours = LongArgumentType.getLong(context, "hours");
        long minutes = LongArgumentType.getLong(context, "minutes");
        long seconds = LongArgumentType.getLong(context, "seconds");
        ServerCommandSource source = context.getSource();

        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setCountdown(true, hours, minutes, seconds, "");

        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new StartCountdownPayload(hours, minutes, seconds, ""));
        }

        source.sendFeedback(() -> Text.literal(String.format("Countdown iniciado: %02d:%02d:%02d", hours, minutes, seconds)), true);
        return 1;
    }

    private static int startCountdownWithTitle(CommandContext<ServerCommandSource> context) {
        long hours = LongArgumentType.getLong(context, "hours");
        long minutes = LongArgumentType.getLong(context, "minutes");
        long seconds = LongArgumentType.getLong(context, "seconds");
        String title = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "title");
        ServerCommandSource source = context.getSource();

        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setCountdown(true, hours, minutes, seconds, title);

        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new StartCountdownPayload(hours, minutes, seconds, title));
        }

        source.sendFeedback(() -> Text.literal(String.format("Countdown iniciado: %02d:%02d:%02d - %s", hours, minutes, seconds, title)), true);
        return 1;
    }
    
    private static int addTime(CommandContext<ServerCommandSource> context) {
        long hours = LongArgumentType.getLong(context, "hours");
        long minutes = LongArgumentType.getLong(context, "minutes");
        long seconds = LongArgumentType.getLong(context, "seconds");
        ServerCommandSource source = context.getSource();
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new AddTimePayload(hours, minutes, seconds));
        }
        
        source.sendFeedback(() -> Text.literal(String.format("Tiempo agregado: +%02d:%02d:%02d", hours, minutes, seconds)), true);
        return 1;
    }
    
    private static int pauseCountdown(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setCountdownPaused(true);
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new PauseCountdownPayload());
        }
        
        source.sendFeedback(() -> Text.literal("Countdown pausado"), true);
        return 1;
    }
    
    private static int resumeCountdown(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.setCountdownPaused(false);
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new ResumeCountdownPayload());
        }
        
        source.sendFeedback(() -> Text.literal("Countdown reanudado"), true);
        return 1;
    }
    
    private static int stopCountdown(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Guardar estado
        co.juanxxo.junta.overlay.OverlayState.stopCountdown();
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new StopCountdownPayload());
        }
        
        source.sendFeedback(() -> Text.literal("Countdown detenido"), true);
        return 1;
    }
}
