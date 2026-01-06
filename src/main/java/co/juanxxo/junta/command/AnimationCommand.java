package co.juanxxo.junta.command;

import co.juanxxo.junta.overlay.AnimationPosition;
import co.juanxxo.junta.overlay.AnimationSize;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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

public class AnimationCommand {
    public static final Identifier PLAY_ANIMATION_ID = Identifier.of("junta", "play_animation");
    public static final Identifier STOP_ANIMATION_ID = Identifier.of("junta", "stop_animation");

    public record PlayAnimationPayload(String animationName, String position, String size, int fps) implements CustomPayload {
        public static final CustomPayload.Id<PlayAnimationPayload> ID = new CustomPayload.Id<>(PLAY_ANIMATION_ID);
        public static final PacketCodec<RegistryByteBuf, PlayAnimationPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, PlayAnimationPayload::animationName,
            PacketCodecs.STRING, PlayAnimationPayload::position,
            PacketCodecs.STRING, PlayAnimationPayload::size,
            PacketCodecs.INTEGER, PlayAnimationPayload::fps,
            PlayAnimationPayload::new
        );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record StopAnimationPayload() implements CustomPayload {
        public static final CustomPayload.Id<StopAnimationPayload> ID = new CustomPayload.Id<>(STOP_ANIMATION_ID);
        public static final PacketCodec<RegistryByteBuf, StopAnimationPayload> CODEC = PacketCodec.unit(new StopAnimationPayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("jntcore")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("animation")
                .then(CommandManager.argument("name", StringArgumentType.word())
                    .then(CommandManager.argument("position", StringArgumentType.word())
                        .then(CommandManager.argument("size", StringArgumentType.word())
                            .executes(context -> executePlayAnimation(context, 24)) // FPS por defecto: 24
                            .then(CommandManager.argument("fps", IntegerArgumentType.integer(1, 120))
                                .executes(context -> executePlayAnimation(
                                    context, 
                                    IntegerArgumentType.getInteger(context, "fps")
                                ))
                            )
                        )
                    )
                )
                .then(CommandManager.literal("stop")
                    .executes(AnimationCommand::executeStopAnimation)
                )
            )
        );
    }

    private static int executePlayAnimation(CommandContext<ServerCommandSource> context, int fps) {
        ServerCommandSource source = context.getSource();
        String animationName = StringArgumentType.getString(context, "name");
        String positionStr = StringArgumentType.getString(context, "position");
        String sizeStr = StringArgumentType.getString(context, "size");

        // Validar posición
        AnimationPosition position = AnimationPosition.fromString(positionStr);
        if (position == null) {
            source.sendError(Text.literal("Posición inválida. Usa: centro, scoreboard, up_derecha, up_izquierda, down_derecha, down_izquierda"));
            return 0;
        }

        // Validar tamaño
        AnimationSize size = AnimationSize.fromString(sizeStr);
        if (size == null) {
            source.sendError(Text.literal("Tamaño inválido. Usa: small, normal"));
            return 0;
        }

        // Enviar paquete a todos los jugadores
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(
                player,
                new PlayAnimationPayload(animationName, position.getName(), size.getName(), fps)
            );
        }

        source.sendFeedback(() -> Text.literal(
            String.format("§aAnimación '%s' iniciada (posición: %s, tamaño: %s, fps: %d)", 
            animationName, position.getName(), size.getName(), fps)
        ), true);

        return 1;
    }

    private static int executeStopAnimation(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        // Enviar paquete a todos los jugadores
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new StopAnimationPayload());
        }

        source.sendFeedback(() -> Text.literal("§aAnimación detenida"), true);
        return 1;
    }
}
