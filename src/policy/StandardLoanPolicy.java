package policy;

import library.Loan;

/**
 * Política estándar de préstamos.
 * 
 * Tarifa: 1€/día los primeros 14 días, luego 1.50€/día extra.
 * Implementa LoanPolicy así que podemos intercambiarla por otra sin problemas (OCP).
 */
public class StandardLoanPolicy implements LoanPolicy {
    
    private static final double TARIFA_DIARIA = 1.00;
    private static final int DIAS_NORMALES = 14;
    private static final double RECARGO_EXTRA = 0.50;
    
    @Override
    public double calculateCost(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        
        int dias = loan.getDaysLoaned();
        
        if (dias <= DIAS_NORMALES) {
            return dias * TARIFA_DIARIA;
        } else {
            // Primeros 14 días a tarifa normal, el resto con recargo
            double costeBase = DIAS_NORMALES * TARIFA_DIARIA;
            double diasExtra = dias - DIAS_NORMALES;
            double costeExtra = diasExtra * (TARIFA_DIARIA + RECARGO_EXTRA);
            return costeBase + costeExtra;
        }
    }
    
    @Override
    public String getPolicyName() {
        return "Política Estándar";
    }
    
    @Override
    public String toString() {
        return getPolicyName() + " (1€/día, recargo tras 14 días)";
    }
}
