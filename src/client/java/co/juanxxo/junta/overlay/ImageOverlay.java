package co.juanxxo.junta.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class ImageOverlay {
    private static ImageData currentImage = null;

    public static void register() {
        HudRenderCallback.EVENT.register(ImageOverlay::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (currentImage == null) return;

        if (currentImage.isFinished()) {
            currentImage = null;
            return;
        }

        if (!currentImage.isPlaying()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        Identifier imageTexture = currentImage.getImageId();
        if (imageTexture == null) return;

        AnimationSize animSize = currentImage.getSize();
        int renderSize = animSize.getRenderSize();
        int textureSize = animSize.getTextureSize();
        AnimationPosition position = currentImage.getPosition();

        // Calculate position
        int x = calculateX(screenWidth, renderSize, position);
        int y = calculateY(screenHeight, renderSize, position);

        // Get alpha based on fade
        float alpha = currentImage.getAlpha();

        // Render texture with fade
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        
        context.drawTexture(
            imageTexture,
            x, y,
            renderSize, renderSize,
            0, 0,
            textureSize, textureSize,
            textureSize, textureSize
        );
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    private static int calculateX(int screenWidth, int size, AnimationPosition position) {
        return switch (position) {
            case CENTER, SCOREBOARD, BOTTOM_CENTER, TOP_CENTER -> (screenWidth - size) / 2;
            case TOP_RIGHT, BOTTOM_RIGHT -> screenWidth - size;
            case TOP_LEFT, BOTTOM_LEFT -> 0;
        };
    }

    private static int calculateY(int screenHeight, int size, AnimationPosition position) {
        return switch (position) {
            case CENTER -> (screenHeight - size) / 2;
            case SCOREBOARD -> (screenHeight - size) / 2 + 50;
            case BOTTOM_CENTER -> screenHeight - size;
            case TOP_CENTER, TOP_RIGHT, TOP_LEFT -> 0;
            case BOTTOM_RIGHT, BOTTOM_LEFT -> screenHeight - size;
        };
    }

    public static void show(String imageName, AnimationPosition position, AnimationSize size, 
                           float durationSeconds, float fadeInSeconds, float fadeOutSeconds) {
        try {
            currentImage = new ImageData(imageName, position, size, durationSeconds, fadeInSeconds, fadeOutSeconds);
            currentImage.start();
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                .addMessage(net.minecraft.text.Text.literal("§cError al cargar imagen: " + e.getMessage()));
            currentImage = null;
        }
    }

    public static void hide() {
        if (currentImage != null) {
            currentImage.stop();
            currentImage = null;
        }
    }

    public static boolean isShowing() {
        return currentImage != null && currentImage.isPlaying();
    }

    public static ImageData getCurrentImage() {
        return currentImage;
    }
}
