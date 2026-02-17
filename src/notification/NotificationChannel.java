package notification;

/**
 * Interfaz para enviar notificaciones.
 * 
 * DIP: LoanService depende de esta interfaz, no de una implementación concreta.
 * Así podemos cambiar de consola a email sin tocar el servicio.
 */
public interface NotificationChannel {
    
    void notify(String message);
    
    String getChannelName();
}
