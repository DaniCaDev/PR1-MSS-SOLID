package isp;

import library.Loan;

/**
 * Interfaz solo para notificaciones de préstamos.
 */
public interface LoanNotifier {
    void notifyLoan(Loan loan, String message);
    void sendNotification(String message);
}
