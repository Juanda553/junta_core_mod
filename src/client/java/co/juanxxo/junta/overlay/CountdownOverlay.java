package co.juanxxo.junta.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class CountdownOverlay {
    private static boolean isVisible = false;
    private static long remainingTimeMs = 0;
    private static long lastUpdateTime = 0;
    private static boolean isPaused = false;
    private static String title = null;

    // Variables para el parpadeo cuando llega a 0
    private static boolean isZero = false;
    private static long zeroStartTime = 0;
    private static final long ZERO_BLINK_DURATION = 5000; // 5 segundos parpadeando

    // Variables para el parpadeo cuando está pausado
    private static long blinkTimer = 0;
    private static boolean blinkState = true;
    
    public static void register() {
        HudRenderCallback.EVENT.register(CountdownOverlay::render);
    }
    
    public static void start(long hours, long minutes, long seconds) {
        start(hours, minutes, seconds, null);
    }

    public static void start(long hours, long minutes, long seconds, String customTitle) {
        long totalMs = (hours * 3600000) + (minutes * 60000) + (seconds * 1000);
        remainingTimeMs = totalMs;
        isVisible = true;
        isPaused = false;
        isZero = false;
        lastUpdateTime = System.currentTimeMillis();
        blinkTimer = System.currentTimeMillis();
        title = (customTitle != null && !customTitle.isBlank()) ? customTitle : null;
    }
    
    public static void addTime(long hours, long minutes, long seconds) {
        if (!isVisible) return;
        
        long additionalMs = (hours * 3600000) + (minutes * 60000) + (seconds * 1000);
        remainingTimeMs += additionalMs;
        
        // Si estaba en 0, reiniciar
        if (isZero) {
            isZero = false;
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    
    public static void pause() {
        if (isVisible && !isZero) {
            isPaused = true;
            blinkTimer = System.currentTimeMillis();
        }
    }
    
    public static void resume() {
        if (isVisible && !isZero) {
            isPaused = false;
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    
    public static void stop() {
        isVisible = false;
        isPaused = false;
        isZero = false;
        remainingTimeMs = 0;
        title = null;
    }
    
    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isVisible) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Actualizar tiempo si no está pausado y no está en 0
        if (!isPaused && !isZero) {
            long elapsed = currentTime - lastUpdateTime;
            remainingTimeMs -= elapsed;
            lastUpdateTime = currentTime;
            
            // Verificar si llegó a 0
            if (remainingTimeMs <= 0) {
                remainingTimeMs = 0;
                isZero = true;
                zeroStartTime = currentTime;
            }
        }
        
        // Si está en 0 y han pasado 5 segundos, desaparecer
        if (isZero) {
            long zeroElapsed = currentTime - zeroStartTime;
            if (zeroElapsed >= ZERO_BLINK_DURATION) {
                isVisible = false;
                return;
            }
        }
        
        // Actualizar parpadeo
        if (currentTime - blinkTimer >= 500) {
            blinkTimer = currentTime;
            blinkState = !blinkState;
        }
        
        // Determinar si debe mostrar el texto (para parpadeo)
        boolean shouldShow = true;
        if (isZero || isPaused) {
            shouldShow = blinkState;
        }
        
        if (!shouldShow) {
            return;
        }
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        // Convertir milisegundos a horas:minutos:segundos
        long totalSeconds = remainingTimeMs / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        
        // Formato del texto
        String timeText;
        if (hours > 0) {
            timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeText = String.format("%02d:%02d", minutes, seconds);
        }
        
        // Añadir "PAUSADO" si está pausado
        if (isPaused) {
            timeText = "PAUSADO - " + timeText;
        }
        
        // Determinar color
        int textColor;
        if (isPaused) {
            textColor = 0xFF0000; // Rojo cuando está pausado
        } else if (isZero) {
            textColor = 0xFFFF00; // Amarillo cuando llega a 0
        } else if (remainingTimeMs <= 10000) { // Menos o igual a 10 segundos
            textColor = 0xFF6600; // Naranja
        } else {
            textColor = 0xFFFFFF; // Blanco normal
        }
        
        // Posición centrada en la parte superior
        int textWidth = client.textRenderer.getWidth(timeText);
        int scale = 3;
        int scaledWidth = textWidth * scale;
        int x = (screenWidth - scaledWidth) / 2;
        int y = 42;

        // Fondo negro semitransparente
        int bgPadding = 10;
        int bgX = x - bgPadding;
        int bgY = y - bgPadding;
        int bgWidth = scaledWidth + (bgPadding * 2);
        int bgHeight = (client.textRenderer.fontHeight * scale) + (bgPadding * 2);
        int titleHeight = 0;
        if (title != null) {
            titleHeight = client.textRenderer.fontHeight + 6; // espacio extra
        }
        context.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight + titleHeight, 0xB0000000);

        // Renderizar texto escalado (contador)
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawText(client.textRenderer, timeText, 0, 0, textColor, true);
        context.getMatrices().pop();

        // Renderizar título pequeño debajo
        if (title != null) {
            int titleY = y + (client.textRenderer.fontHeight * scale) + 6;
            int titleWidth = client.textRenderer.getWidth(title);
            int titleX = (screenWidth - titleWidth) / 2;
            context.drawText(client.textRenderer, title, titleX, titleY, 0xAAAAAA, false);
        }
    }
    
    public static boolean isVisible() {
        return isVisible;
    }
    
    public static boolean isPaused() {
        return isPaused;
    }
}
