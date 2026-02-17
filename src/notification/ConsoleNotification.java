package notification;

/**
 * Envía notificaciones por consola.
 * Es una implementación simple, pero podríamos tener EmailNotification, SMSNotification, etc.
 */
public class ConsoleNotification implements NotificationChannel {
    
    @Override
    public void notify(String message) {
        if (message == null || message.trim().isEmpty()) {
            System.out.println("[NOTIFICACIÓN] (mensaje vacío)");
            return;
        }
        System.out.println("[NOTIFICACIÓN] " + message);
    }
    
    @Override
    public String getChannelName() {
        return "Consola";
    }
    
    @Override
    public String toString() {
        return "Canal: " + getChannelName();
    }
}
