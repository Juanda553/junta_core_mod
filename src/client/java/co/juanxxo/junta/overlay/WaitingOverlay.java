package co.juanxxo.junta.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class WaitingOverlay {
    private static boolean isVisible = false;
    private static int maxPlayers = 40;
    private static long animationStartTime = 0;
    private static long dotsTimer = 0;
    private static int dotsCount = 1;
    
    // Variables para la animación de cierre tipo TV antigua
    private static boolean isClosing = false;
    private static long closeStartTime = 0;
    private static final long CLOSE_DURATION = 400; // 0.4 segundos para la animación (más rápido)
    
    public static void register() {
        HudRenderCallback.EVENT.register(WaitingOverlay::render);
    }
    
    public static void show(int maxPlayerCount) {
        isVisible = true;
        maxPlayers = maxPlayerCount;
        isClosing = false;
        animationStartTime = System.currentTimeMillis();
        dotsTimer = System.currentTimeMillis();
        dotsCount = 1;
    }
    
    public static void hide() {
        if (isVisible && !isClosing) {
            isClosing = true;
            closeStartTime = System.currentTimeMillis();
        }
    }
    
    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!isVisible && !isClosing) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        // Actualizar animación de puntos cada 500ms
        long currentTime = System.currentTimeMillis();
        if (currentTime - dotsTimer >= 500) {
            dotsTimer = currentTime;
            dotsCount = (dotsCount % 3) + 1;
        }
        
        // Calcular progreso de la animación de cierre
        float closeProgress = 0.0f;
        if (isClosing) {
            long elapsed = currentTime - closeStartTime;
            closeProgress = Math.min(1.0f, elapsed / (float) CLOSE_DURATION);
            
            if (closeProgress >= 1.0f) {
                isVisible = false;
                isClosing = false;
                return;
            }
        }
        
        // Fondo negro semitransparente
        int bgWidth = 400;
        int bgHeight = 80;
        int bgX = (screenWidth - bgWidth) / 2;
        int bgY = screenHeight - 150; // Parte inferior de la pantalla
        
        // Aplicar transformación de cierre tipo TV
        context.getMatrices().push();
        
        if (isClosing) {
            // Efecto de "aplastar" verticalmente y luego horizontalmente
            float verticalScale = 1.0f - (closeProgress * 0.99f);
            float horizontalScale = closeProgress < 0.5f ? 1.0f : 1.0f - ((closeProgress - 0.5f) * 2.0f);
            
            // Usar la posición Y del cuadro como punto de anclaje
            float centerX = screenWidth / 2.0f;
            float anchorY = bgY + (bgHeight / 2.0f); // Centro del cuadro en Y
            
            context.getMatrices().translate(centerX, anchorY, 0);
            context.getMatrices().scale(horizontalScale, verticalScale, 1.0f);
            context.getMatrices().translate(-centerX, -anchorY, 0);
        }
        
        // Color negro con transparencia (ARGB)
        int backgroundColor = 0xB0000000; // 70% opaco
        context.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, backgroundColor);
        
        // Contar jugadores actuales
        int currentPlayers = 0;
        if (client.world != null) {
            currentPlayers = client.world.getPlayers().size();
        }
        
        // Crear texto con puntos animados
        String dots = ".".repeat(dotsCount);
        String waitingText = "Esperando jugadores" + dots;
        String playerCountText = currentPlayers + "/" + maxPlayers;
        
        // Renderizar texto "Esperando jugadores..."
        int textColor = 0xFFFFFF; // Blanco
        int waitingTextWidth = client.textRenderer.getWidth(waitingText);
        int waitingTextX = (screenWidth - waitingTextWidth) / 2;
        int waitingTextY = bgY + 15;
        
        context.drawText(client.textRenderer, waitingText, waitingTextX, waitingTextY, textColor, true);
        
        // Renderizar contador de jugadores (más grande)
        int playerCountWidth = client.textRenderer.getWidth(playerCountText);
        int playerCountX = (screenWidth - playerCountWidth * 2) / 2; // x2 para escala
        int playerCountY = bgY + 40;
        
        context.getMatrices().push();
        context.getMatrices().translate(playerCountX, playerCountY, 0);
        context.getMatrices().scale(2.0f, 2.0f, 1.0f);
        context.drawText(client.textRenderer, playerCountText, 0, 0, 0xFFFFFF, true); // Verde
        context.getMatrices().pop();
        
        context.getMatrices().pop();
    }
    
    public static boolean isVisible() {
        return isVisible;
    }
}
