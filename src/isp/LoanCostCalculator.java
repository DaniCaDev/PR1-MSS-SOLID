package isp;

import library.Loan;

/**
 * Interfaz solo para calcular costes.
 * 
 * ISP: en vez de tener una interfaz gigante con calculateCost + notify + register,
 * separamos en interfaces pequeñas. Así cada clase implementa solo lo que necesita.
 */
public interface LoanCostCalculator {
    double calculateCost(Loan loan);
}
