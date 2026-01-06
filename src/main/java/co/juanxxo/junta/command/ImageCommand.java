package co.juanxxo.junta.command;

import co.juanxxo.junta.overlay.AnimationPosition;
import co.juanxxo.junta.overlay.AnimationSize;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ImageCommand {
    public static final Identifier SHOW_IMAGE_ID = Identifier.of("junta", "show_image");
    public static final Identifier HIDE_IMAGE_ID = Identifier.of("junta", "hide_image");

    public record ShowImagePayload(String imageName, String position, String size, 
                                   float durationSeconds, float fadeInSeconds, float fadeOutSeconds) 
                                   implements CustomPayload {
        public static final CustomPayload.Id<ShowImagePayload> ID = new CustomPayload.Id<>(SHOW_IMAGE_ID);
        public static final PacketCodec<RegistryByteBuf, ShowImagePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ShowImagePayload::imageName,
            PacketCodecs.STRING, ShowImagePayload::position,
            PacketCodecs.STRING, ShowImagePayload::size,
            PacketCodecs.FLOAT, ShowImagePayload::durationSeconds,
            PacketCodecs.FLOAT, ShowImagePayload::fadeInSeconds,
            PacketCodecs.FLOAT, ShowImagePayload::fadeOutSeconds,
            ShowImagePayload::new
        );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record HideImagePayload() implements CustomPayload {
        public static final CustomPayload.Id<HideImagePayload> ID = new CustomPayload.Id<>(HIDE_IMAGE_ID);
        public static final PacketCodec<RegistryByteBuf, HideImagePayload> CODEC = PacketCodec.unit(new HideImagePayload());

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("jntcore")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("image")
                .then(CommandManager.argument("name", StringArgumentType.word())
                    .then(CommandManager.argument("position", StringArgumentType.word())
                        .then(CommandManager.argument("size", StringArgumentType.word())
                            .then(CommandManager.argument("duration", FloatArgumentType.floatArg(0.0f))
                                .executes(context -> executeShowImage(
                                    context,
                                    FloatArgumentType.getFloat(context, "duration"),
                                    0.0f, // fadeIn default
                                    0.0f,  // fadeOut default
                                    null // all players
                                ))
                                .then(CommandManager.argument("fadeIn", FloatArgumentType.floatArg(0.0f))
                                    .executes(context -> executeShowImage(
                                        context,
                                        FloatArgumentType.getFloat(context, "duration"),
                                        FloatArgumentType.getFloat(context, "fadeIn"),
                                        0.0f,  // fadeOut default
                                        null // all players
                                    ))
                                    .then(CommandManager.argument("fadeOut", FloatArgumentType.floatArg(0.0f))
                                        .executes(context -> executeShowImage(
                                            context,
                                            FloatArgumentType.getFloat(context, "duration"),
                                            FloatArgumentType.getFloat(context, "fadeIn"),
                                            FloatArgumentType.getFloat(context, "fadeOut"),
                                            null // all players
                                        ))
                                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                            .executes(context -> executeShowImage(
                                                context,
                                                FloatArgumentType.getFloat(context, "duration"),
                                                FloatArgumentType.getFloat(context, "fadeIn"),
                                                FloatArgumentType.getFloat(context, "fadeOut"),
                                                EntityArgumentType.getPlayer(context, "player")
                                            ))
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
                .then(CommandManager.literal("hide")
                    .executes(context -> executeHideImage(context, null))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> executeHideImage(context, EntityArgumentType.getPlayer(context, "player")))
                    )
                )
            )
        );
    }

    private static int executeShowImage(CommandContext<ServerCommandSource> context, 
                                       float duration, float fadeIn, float fadeOut,
                                       ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        String imageName = StringArgumentType.getString(context, "name");
        String positionStr = StringArgumentType.getString(context, "position");
        String sizeStr = StringArgumentType.getString(context, "size");

        // Validate position
        AnimationPosition position = AnimationPosition.fromString(positionStr);
        if (position == null) {
            source.sendError(Text.literal("Posición inválida"));
            return 0;
        }

        // Validate size
        AnimationSize size = AnimationSize.fromString(sizeStr);
        if (size == null) {
            source.sendError(Text.literal("Tamaño inválido"));
            return 0;
        }

        ShowImagePayload payload = new ShowImagePayload(imageName, position.getName(), size.getName(), 
                                                        duration, fadeIn, fadeOut);

        // Send packet to target player or all players
        if (targetPlayer != null) {
            ServerPlayNetworking.send(targetPlayer, payload);
            source.sendFeedback(() -> Text.literal(
                String.format("§aImagen '%s' mostrada a %s (posición: %s, tamaño: %s, duración: %.1fs, fadeIn: %.1fs, fadeOut: %.1fs)", 
                imageName, targetPlayer.getName().getString(), position.getName(), size.getName(), duration, fadeIn, fadeOut)
            ), true);
        } else {
            for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, payload);
            }
            source.sendFeedback(() -> Text.literal(
                String.format("§aImagen '%s' mostrada a todos (posición: %s, tamaño: %s, duración: %.1fs, fadeIn: %.1fs, fadeOut: %.1fs)", 
                imageName, position.getName(), size.getName(), duration, fadeIn, fadeOut)
            ), true);
        }

        return 1;
    }

    private static int executeHideImage(CommandContext<ServerCommandSource> context, 
                                       ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        // Send packet to target player or all players
        if (targetPlayer != null) {
            ServerPlayNetworking.send(targetPlayer, new HideImagePayload());
            source.sendFeedback(() -> Text.literal(
                String.format("§aImagen ocultada para %s", targetPlayer.getName().getString())
            ), true);
        } else {
            for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, new HideImagePayload());
            }
            source.sendFeedback(() -> Text.literal("§aImagen ocultada para todos"), true);
        }

        return 1;
    }
}
