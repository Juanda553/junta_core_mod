package co.juanxxo.junta.overlay;

import co.juanxxo.junta.command.CountdownCommand;
import co.juanxxo.junta.command.WaitingOverlayCommand;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class OverlayState {
    // Estado del Waiting Overlay
    private static boolean waitingOverlayActive = false;
    private static int waitingMaxPlayers = 40;
    
    // Estado del Countdown
    private static boolean countdownActive = false;
    private static long countdownRemainingMs = 0;
    private static String countdownTitle = "";
    private static boolean countdownPaused = false;
    private static long countdownLastUpdate = 0;
    
    // Waiting Overlay
    public static void setWaitingOverlay(boolean active, int maxPlayers) {
        waitingOverlayActive = active;
        waitingMaxPlayers = maxPlayers;
    }
    
    public static void syncWaitingOverlay(ServerPlayerEntity player) {
        if (waitingOverlayActive) {
            ServerPlayNetworking.send(player, new WaitingOverlayCommand.ShowWaitingPayload(waitingMaxPlayers));
        }
    }
    
    // Countdown
    public static void setCountdown(boolean active, long hours, long minutes, long seconds, String title) {
        countdownActive = active;
        countdownRemainingMs = (hours * 3600000) + (minutes * 60000) + (seconds * 1000);
        countdownTitle = title;
        countdownPaused = false;
        countdownLastUpdate = System.currentTimeMillis();
    }
    
    public static void setCountdownPaused(boolean paused) {
        if (!countdownPaused && paused) {
            // Al pausar, actualizar el tiempo restante
            updateRemainingTime();
        }
        countdownPaused = paused;
        if (!paused) {
            // Al reanudar, reiniciar el tiempo de última actualización
            countdownLastUpdate = System.currentTimeMillis();
        }
    }
    
    public static void stopCountdown() {
        countdownActive = false;
        countdownPaused = false;
        countdownRemainingMs = 0;
    }
    
    private static void updateRemainingTime() {
        if (countdownActive && !countdownPaused) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - countdownLastUpdate;
            countdownRemainingMs -= elapsed;
            countdownLastUpdate = currentTime;
            
            if (countdownRemainingMs < 0) {
                countdownRemainingMs = 0;
            }
        }
    }
    
    public static void syncCountdown(ServerPlayerEntity player) {
        if (countdownActive) {
            // Actualizar tiempo restante antes de sincronizar
            updateRemainingTime();
            
            // Convertir milisegundos restantes a horas, minutos, segundos
            long totalSeconds = countdownRemainingMs / 1000;
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;
            
            ServerPlayNetworking.send(player, new CountdownCommand.StartCountdownPayload(
                hours, minutes, seconds, countdownTitle
            ));
            
            if (countdownPaused) {
                ServerPlayNetworking.send(player, new CountdownCommand.PauseCountdownPayload());
            }
        }
    }
    
    public static void syncAll(ServerPlayerEntity player) {
        syncWaitingOverlay(player);
        syncCountdown(player);
    }
}
