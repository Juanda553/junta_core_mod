package co.juanxxo.junta.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AnimationData {
    private final String animationName;
    private final AnimationPosition position;
    private final AnimationSize size;
    private final int fps;
    private final List<Identifier> frames;
    private int currentFrame;
    private long lastFrameTime;
    private boolean isPlaying;
    private long frameDelay;

    public AnimationData(String animationName, AnimationPosition position, AnimationSize size, int fps) {
        this.animationName = animationName;
        this.position = position;
        this.size = size;
        this.fps = fps;
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
        this.lastFrameTime = 0;
        this.isPlaying = false;
        this.frameDelay = 1000 / fps; // Milisegundos por frame
        
        loadFrames();
    }

    private void loadFrames() {
        MinecraftClient client = MinecraftClient.getInstance();
        int frameNumber = 0;
        
        // Intentar cargar frames hasta que no se encuentre más
        while (true) {
            String framePath = String.format("textures/gui/animations/%s/%04d.png", animationName, frameNumber);
            Identifier frameId = Identifier.of("junta", framePath);
            
            try {
                // Intentar cargar el recurso
                InputStream stream = client.getResourceManager()
                    .getResource(frameId)
                    .orElseThrow()
                    .getInputStream();
                
                // Si se carga correctamente, añadir el frame
                frames.add(frameId);
                stream.close();
                frameNumber++;
            } catch (Exception e) {
                // No se encontró más frames
                break;
            }
        }
        
        if (frames.isEmpty()) {
            throw new RuntimeException("No se encontraron frames para la animación: " + animationName);
        }
    }

    public void start() {
        this.isPlaying = true;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void stop() {
        this.isPlaying = false;
        this.currentFrame = 0;
    }

    public void update() {
        if (!isPlaying) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDelay) {
            currentFrame++;
            lastFrameTime = currentTime;
            
            if (currentFrame >= frames.size()) {
                // Animación completada
                isPlaying = false;
                currentFrame = 0;
            }
        }
    }

    public Identifier getCurrentFrame() {
        if (frames.isEmpty()) return null;
        return frames.get(Math.min(currentFrame, frames.size() - 1));
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isFinished() {
        return !isPlaying && currentFrame == 0;
    }

    public AnimationPosition getPosition() {
        return position;
    }

    public AnimationSize getSize() {
        return size;
    }

    public int getTotalFrames() {
        return frames.size();
    }

    public int getCurrentFrameNumber() {
        return currentFrame;
    }
}
