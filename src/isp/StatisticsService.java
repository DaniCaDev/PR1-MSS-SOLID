package isp;

import library.Loan;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que SOLO lleva estadísticas.
 * 
 * Solo implementa LoanRegistry. No necesita calcular costes ni enviar notificaciones.
 * Gracias a ISP, no tenemos métodos vacíos ni excepciones tipo "no soportado".
 */
public class StatisticsService implements LoanRegistry {
    
    private final List<Loan> prestamosRegistrados;
    private double ingresosTotales;
    
    public StatisticsService() {
        this.prestamosRegistrados = new ArrayList<>();
        this.ingresosTotales = 0.0;
    }
    
    @Override
    public void registerLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null");
        }
        prestamosRegistrados.add(loan);
        System.out.println("[REGISTRO] Préstamo añadido: " + loan);
    }
    
    // Método extra para registrar con coste (útil para estadísticas)
    public void registerLoanWithCost(Loan loan, double coste) {
        registerLoan(loan);
        this.ingresosTotales += coste;
    }
    
    @Override
    public int getTotalLoansProcessed() {
        return prestamosRegistrados.size();
    }
    
    @Override
    public double getTotalRevenue() {
        return ingresosTotales;
    }
    
    public void printStatistics() {
        System.out.println("\n===== ESTADÍSTICAS =====");
        System.out.println("Préstamos totales: " + getTotalLoansProcessed());
        System.out.printf("Ingresos totales: %.2f€%n", getTotalRevenue());
        System.out.println("========================\n");
    }
}
