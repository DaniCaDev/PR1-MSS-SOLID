package isp;

import library.Loan;

/**
 * Interfaz para registro y estadísticas de préstamos.
 */
public interface LoanRegistry {
    void registerLoan(Loan loan);
    int getTotalLoansProcessed();
    double getTotalRevenue();
}
