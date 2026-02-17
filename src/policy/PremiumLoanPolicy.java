package policy;

import library.Loan;

/**
 * Política premium con descuentos.
 * 
 * Tarifa reducida de 0.75€/día y si el préstamo es de más de 7 días
 * se aplica un 10% de descuento adicional.
 */
public class PremiumLoanPolicy implements LoanPolicy {
    
    private static final double TARIFA_DIARIA = 0.75;
    private static final int DIAS_PARA_DESCUENTO = 7;
    private static final double DESCUENTO = 0.10; // 10%
    
    @Override
    public double calculateCost(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        
        int dias = loan.getDaysLoaned();
        double coste = dias * TARIFA_DIARIA;
        
        // Descuento del 10% para préstamos largos
        if (dias > DIAS_PARA_DESCUENTO) {
            coste = coste * (1 - DESCUENTO);
        }
        
        return coste;
    }
    
    @Override
    public String getPolicyName() {
        return "Política Premium";
    }
    
    @Override
    public String toString() {
        return getPolicyName() + " (0.75€/día, 10% dto. tras 7 días)";
    }
}
