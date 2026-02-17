package service;

import library.Loan;
import notification.NotificationChannel;
import policy.LoanPolicy;

/**
 * Servicio que gestiona el proceso de préstamo.
 * 
 * DIP: esta clase NO crea sus dependencias con "new". Las recibe por constructor.
 * Esto permite cambiar la política o el canal de notificación desde fuera.
 * 
 * Si hiciéramos "new ConsoleNotification()" aquí dentro, estaríamos acoplados
 * a esa implementación y no podríamos cambiarla sin modificar esta clase.
 */
public class LoanService {
    
    // Dependencias - son interfaces, no clases concretas
    private final LoanPolicy policy;
    private final NotificationChannel notificationChannel;
    
    /**
     * Constructor con inyección de dependencias.
     * Las dependencias vienen de fuera, no se crean aquí.
     */
    public LoanService(LoanPolicy policy, NotificationChannel notificationChannel) {
        if (policy == null) {
            throw new IllegalArgumentException("La política no puede ser null");
        }
        if (notificationChannel == null) {
            throw new IllegalArgumentException("El canal de notificación no puede ser null");
        }
        
        this.policy = policy;
        this.notificationChannel = notificationChannel;
    }
    
    /**
     * Procesa un préstamo: calcula el coste y envía notificación.
     */
    public double processLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        
        // Delega el cálculo a la política (polimorfismo)
        double cost = policy.calculateCost(loan);
        
        // Monta el mensaje y lo envía
        String message = String.format(
            "Préstamo procesado - Libro: '%s' | Prestatario: %s | Días: %d | Coste: %.2f€ | Política: %s",
            loan.getBook().getTitle(),
            loan.getBorrowerName(),
            loan.getDaysLoaned(),
            cost,
            policy.getPolicyName()
        );
        
        notificationChannel.notify(message);
        
        return cost;
    }
    
    public String getPolicyInfo() {
        return policy.toString();
    }
    
    public String getNotificationChannelInfo() {
        return notificationChannel.toString();
    }
}
