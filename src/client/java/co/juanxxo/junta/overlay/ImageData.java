package co.juanxxo.junta.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import java.io.InputStream;

public class ImageData {
    private final String imageName;
    private final AnimationPosition position;
    private final AnimationSize size;
    private final float durationSeconds;
    private final float fadeInSeconds;
    private final float fadeOutSeconds;
    private final Identifier imageId;
    
    private long startTime;
    private boolean isPlaying;
    
    public ImageData(String imageName, AnimationPosition position, AnimationSize size, 
                     float durationSeconds, float fadeInSeconds, float fadeOutSeconds) {
        this.imageName = imageName;
        this.position = position;
        this.size = size;
        this.durationSeconds = durationSeconds;
        this.fadeInSeconds = fadeInSeconds;
        this.fadeOutSeconds = fadeOutSeconds;
        this.isPlaying = false;
        
        // Load the image
        String imagePath = String.format("textures/gui/images/%s.png", imageName);
        this.imageId = Identifier.of("junta", imagePath);
        
        // Verify the image exists
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            InputStream stream = client.getResourceManager()
                .getResource(imageId)
                .orElseThrow()
                .getInputStream();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("No se encontró la imagen: " + imageName);
        }
    }
    
    public void start() {
        this.isPlaying = true;
        this.startTime = System.currentTimeMillis();
    }
    
    public void stop() {
        this.isPlaying = false;
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public boolean isFinished() {
        if (!isPlaying) return true;
        
        float elapsed = getElapsedSeconds();
        return elapsed >= (fadeInSeconds + durationSeconds + fadeOutSeconds);
    }
    
    public float getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }
    
    public float getAlpha() {
        float elapsed = getElapsedSeconds();
        
        // Fade in
        if (elapsed < fadeInSeconds) {
            return fadeInSeconds > 0 ? elapsed / fadeInSeconds : 1.0f;
        }
        
        // Full visibility
        float fadeOutStart = fadeInSeconds + durationSeconds;
        if (elapsed < fadeOutStart) {
            return 1.0f;
        }
        
        // Fade out
        float fadeOutElapsed = elapsed - fadeOutStart;
        if (fadeOutElapsed < fadeOutSeconds) {
            return fadeOutSeconds > 0 ? 1.0f - (fadeOutElapsed / fadeOutSeconds) : 0.0f;
        }
        
        return 0.0f;
    }
    
    public Identifier getImageId() {
        return imageId;
    }
    
    public AnimationPosition getPosition() {
        return position;
    }
    
    public AnimationSize getSize() {
        return size;
    }
}
