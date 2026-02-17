package isp;

import library.Loan;
import policy.LoanPolicy;

/**
 * Servicio que SOLO calcula costes.
 * 
 * Implementa únicamente LoanCostCalculator porque es lo único que necesita.
 * Si tuviéramos una interfaz monolítica, tendríamos que implementar también
 * métodos de notificación y registro que no vamos a usar (viola ISP).
 */
public class SimpleCostService implements LoanCostCalculator {
    
    private final LoanPolicy policy;
    
    public SimpleCostService(LoanPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("La política no puede ser null");
        }
        this.policy = policy;
    }
    
    @Override
    public double calculateCost(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        return policy.calculateCost(loan);
    }
    
    @Override
    public String toString() {
        return "SimpleCostService con " + policy.getPolicyName();
    }
}
