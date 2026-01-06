package co.juanxxo.junta.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class AnimationOverlay {
    private static AnimationData currentAnimation = null;

    public static void register() {
        HudRenderCallback.EVENT.register(AnimationOverlay::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (currentAnimation == null) return;

        currentAnimation.update();

        if (currentAnimation.isFinished()) {
            currentAnimation = null;
            return;
        }

        if (!currentAnimation.isPlaying()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        Identifier frameTexture = currentAnimation.getCurrentFrame();
        if (frameTexture == null) return;

        AnimationSize animSize = currentAnimation.getSize();
        int renderSize = animSize.getRenderSize();
        int textureSize = animSize.getTextureSize();
        AnimationPosition position = currentAnimation.getPosition();

        // Calcular posición según el enum
        int x = calculateX(screenWidth, renderSize, position);
        int y = calculateY(screenHeight, renderSize, position);

        // Renderizar la textura con reescalado
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        context.drawTexture(
            frameTexture,
            x, y,
            renderSize, renderSize,
            0, 0,
            textureSize, textureSize,
            textureSize, textureSize
        );
        RenderSystem.disableBlend();
    }

    private static int calculateX(int screenWidth, int size, AnimationPosition position) {
        return switch (position) {
            case CENTER, SCOREBOARD, BOTTOM_CENTER, TOP_CENTER -> (screenWidth - size) / 2;
            case TOP_RIGHT, BOTTOM_RIGHT -> screenWidth - size; // no margin
            case TOP_LEFT, BOTTOM_LEFT -> 0; // no margin
        };
    }

    private static int calculateY(int screenHeight, int size, AnimationPosition position) {
        return switch (position) {
            case CENTER -> (screenHeight - size) / 2;
            case SCOREBOARD -> (screenHeight - size) / 2 + 50; // slightly lower
            case BOTTOM_CENTER -> screenHeight - size; // bottom, no extra margin
            case TOP_CENTER, TOP_RIGHT, TOP_LEFT -> 0; // no top margin
            case BOTTOM_RIGHT, BOTTOM_LEFT -> screenHeight - size; // no bottom margin
        };
    }

    public static void play(String animationName, AnimationPosition position, AnimationSize size, int fps) {
        try {
            currentAnimation = new AnimationData(animationName, position, size, fps);
            currentAnimation.start();
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                .addMessage(net.minecraft.text.Text.literal("§cError al cargar animación: " + e.getMessage()));
            currentAnimation = null;
        }
    }

    public static void stop() {
        if (currentAnimation != null) {
            currentAnimation.stop();
            currentAnimation = null;
        }
    }

    public static boolean isPlaying() {
        return currentAnimation != null && currentAnimation.isPlaying();
    }

    public static AnimationData getCurrentAnimation() {
        return currentAnimation;
    }
}
