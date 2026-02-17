package isp;

import library.Loan;
import notification.NotificationChannel;
import policy.LoanPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio completo que implementa las 3 interfaces.
 * 
 * Este sí necesita las tres funcionalidades: calcular, notificar y registrar.
 * Por eso implementa LoanCostCalculator, LoanNotifier y LoanRegistry.
 * 
 * La diferencia con una interfaz monolítica es que aquí ELEGIMOS implementar las tres,
 * mientras que SimpleCostService solo implementa una y no tiene métodos de más.
 */
public class FullLoanService implements LoanCostCalculator, LoanNotifier, LoanRegistry {
    
    private final LoanPolicy policy;
    private final NotificationChannel channel;
    private final List<Loan> prestamos;
    private double ingresos;
    
    public FullLoanService(LoanPolicy policy, NotificationChannel channel) {
        if (policy == null) {
            throw new IllegalArgumentException("La política no puede ser null");
        }
        if (channel == null) {
            throw new IllegalArgumentException("El canal no puede ser null");
        }
        
        this.policy = policy;
        this.channel = channel;
        this.prestamos = new ArrayList<>();
        this.ingresos = 0.0;
    }
    
    // === LoanCostCalculator ===
    
    @Override
    public double calculateCost(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        return policy.calculateCost(loan);
    }
    
    // === LoanNotifier ===
    
    @Override
    public void notifyLoan(Loan loan, String message) {
        String msg = String.format("Préstamo de '%s' a %s: %s",
                loan.getBook().getTitle(),
                loan.getBorrowerName(),
                message);
        channel.notify(msg);
    }
    
    @Override
    public void sendNotification(String message) {
        channel.notify(message);
    }
    
    // === LoanRegistry ===
    
    @Override
    public void registerLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        prestamos.add(loan);
    }
    
    @Override
    public int getTotalLoansProcessed() {
        return prestamos.size();
    }
    
    @Override
    public double getTotalRevenue() {
        return ingresos;
    }
    
    // === Método que usa todo ===
    
    /**
     * Procesa un préstamo completo: calcula, registra y notifica.
     */
    public double processLoanFully(Loan loan) {
        double coste = calculateCost(loan);
        registerLoan(loan);
        ingresos += coste;
        
        String msg = String.format("Coste: %.2f€ (%s)", coste, policy.getPolicyName());
        notifyLoan(loan, msg);
        
        return coste;
    }
    
    @Override
    public String toString() {
        return String.format("FullLoanService[%s, préstamos=%d, ingresos=%.2f€]",
                           policy.getPolicyName(), getTotalLoansProcessed(), getTotalRevenue());
    }
}
