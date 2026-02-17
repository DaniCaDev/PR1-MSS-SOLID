import library.Book;
import library.Loan;
import notification.ConsoleNotification;
import notification.NotificationChannel;
import policy.LoanPolicy;
import policy.PremiumLoanPolicy;
import policy.StandardLoanPolicy;
import service.LoanService;
import isp.*;

/**
 * Clase principal con las pruebas de todos los ejercicios.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("  PRÁCTICA 1 - PRINCIPIOS SOLID");
        System.out.println("  Sistema de Préstamos de Biblioteca");
        System.out.println("=====================================================\n");
        
        pruebasEjercicio1();
        pruebasEjercicio2();
        pruebasEjercicio3();
        pruebasEjercicio4();
        
        System.out.println("\n=====================================================");
        System.out.println("  FIN DE LAS PRUEBAS");
        System.out.println("=====================================================");
    }
    
    /**
     * Ejercicio 1 - SRP
     * Probamos que Book y Loan funcionan correctamente y tienen responsabilidades separadas.
     */
    private static void pruebasEjercicio1() {
        System.out.println("--- EJERCICIO 1: SRP (Single Responsibility) ---\n");
        
        // Crear libros
        System.out.println("Creando libros...");
        Book libro1 = new Book("El Quijote", "Cervantes", "978-84-376-0494-7");
        Book libro2 = new Book("1984", "George Orwell", "978-0-452-28423-4");
        System.out.println("  " + libro1);
        System.out.println("  " + libro2);
        
        // Probar validación de libro
        System.out.println("\nProbando validación (título vacío)...");
        try {
            Book libroMalo = new Book("", "Autor", "123");
            System.out.println("  ERROR: no debería permitirlo");
        } catch (IllegalArgumentException e) {
            System.out.println("  OK - Excepción: " + e.getMessage());
        }
        
        // Crear préstamos
        System.out.println("\nCreando préstamos...");
        Loan prestamo1 = new Loan(libro1, "Ana García", 7);
        Loan prestamo2 = new Loan(libro2, "Luis Martínez", 21);
        System.out.println("  " + prestamo1);
        System.out.println("  " + prestamo2);
        
        // Probar validación de préstamo
        System.out.println("\nProbando validación (días negativos)...");
        try {
            Loan prestamoMalo = new Loan(libro1, "Pedro", -5);
            System.out.println("  ERROR: no debería permitirlo");
        } catch (IllegalArgumentException e) {
            System.out.println("  OK - Excepción: " + e.getMessage());
        }
        
        System.out.println("\n[SRP] Book solo guarda datos del libro, Loan solo datos del préstamo.");
        System.out.println("      Ninguno calcula costes ni envía notificaciones.\n");
    }
    
    /**
     * Ejercicio 2 - OCP + Polimorfismo
     * Probamos las políticas de préstamo y cómo podemos intercambiarlas.
     */
    private static void pruebasEjercicio2() {
        System.out.println("--- EJERCICIO 2: OCP (Open/Closed) + Polimorfismo ---\n");
        
        Book libro = new Book("Clean Code", "Robert Martin", "978-0-13-235088-4");
        Loan prestamoCorto = new Loan(libro, "María", 5);
        Loan prestamoLargo = new Loan(libro, "Carlos", 20);
        
        // Política estándar
        System.out.println("Política Estándar:");
        LoanPolicy standard = new StandardLoanPolicy();
        System.out.printf("  5 días: %.2f€%n", standard.calculateCost(prestamoCorto));
        System.out.printf("  20 días: %.2f€ (14 normales + 6 con recargo)%n", 
                         standard.calculateCost(prestamoLargo));
        
        // Política premium
        System.out.println("\nPolítica Premium:");
        LoanPolicy premium = new PremiumLoanPolicy();
        System.out.printf("  5 días: %.2f€%n", premium.calculateCost(prestamoCorto));
        System.out.printf("  20 días: %.2f€ (con 10%% descuento)%n", 
                         premium.calculateCost(prestamoLargo));
        
        // Polimorfismo - misma variable, distinto comportamiento
        System.out.println("\nPolimorfismo en acción:");
        LoanPolicy[] politicas = {new StandardLoanPolicy(), new PremiumLoanPolicy()};
        for (LoanPolicy p : politicas) {
            System.out.printf("  %s -> %.2f€%n", p.getPolicyName(), p.calculateCost(prestamoLargo));
        }
        
        System.out.println("\n[OCP] Para añadir nueva política, solo creamos nueva clase.");
        System.out.println("      No tocamos código existente.\n");
    }
    
    /**
     * Ejercicio 3 - DIP
     * Probamos que LoanService recibe sus dependencias por constructor.
     */
    private static void pruebasEjercicio3() {
        System.out.println("--- EJERCICIO 3: DIP (Dependency Inversion) ---\n");
        
        Book libro1 = new Book("Design Patterns", "GoF", "978-0-201-63361-0");
        Book libro2 = new Book("Refactoring", "Fowler", "978-0-13-475759-9");
        Loan prestamo1 = new Loan(libro1, "Elena", 10);
        Loan prestamo2 = new Loan(libro2, "Pablo", 18);
        
        // Creamos las dependencias FUERA del servicio
        LoanPolicy policy = new StandardLoanPolicy();
        NotificationChannel channel = new ConsoleNotification();
        
        // Las inyectamos por constructor
        System.out.println("Creando LoanService con inyección de dependencias...");
        LoanService service = new LoanService(policy, channel);
        System.out.println("  Política: " + service.getPolicyInfo());
        System.out.println("  Canal: " + service.getNotificationChannelInfo());
        
        // Procesamos préstamos
        System.out.println("\nProcesando préstamos:");
        double coste1 = service.processLoan(prestamo1);
        double coste2 = service.processLoan(prestamo2);
        
        // Cambiamos la política creando otro servicio
        System.out.println("\nCambiando a política Premium (sin modificar LoanService):");
        LoanService servicePremium = new LoanService(new PremiumLoanPolicy(), channel);
        double costePremium = servicePremium.processLoan(prestamo1);
        System.out.printf("  Mismo préstamo: Standard=%.2f€, Premium=%.2f€%n", coste1, costePremium);
        
        System.out.println("\n[DIP] LoanService depende de interfaces, no de clases concretas.");
        System.out.println("      No hay 'new ConsoleNotification()' dentro del servicio.\n");
    }
    
    /**
     * Ejercicio 4 - ISP (Bonus)
     * Mostramos cómo las interfaces segregadas permiten implementar solo lo necesario.
     */
    private static void pruebasEjercicio4() {
        System.out.println("--- EJERCICIO 4: ISP (Interface Segregation) [BONUS] ---\n");
        
        Book libro1 = new Book("The Pragmatic Programmer", "Hunt", "978-0-13-595705-9");
        Book libro2 = new Book("DDD", "Eric Evans", "978-0-321-12521-5");
        Loan prestamo1 = new Loan(libro1, "Sara", 12);
        Loan prestamo2 = new Loan(libro2, "Diego", 25);
        
        /*
         * El problema de una interfaz monolítica:
         * Si tuviéramos LoanManagementService con calculateCost + notify + register,
         * un servicio que solo calcula costes tendría que implementar todo,
         * dejando métodos vacíos o lanzando excepciones. Eso viola ISP.
         */
        System.out.println("¿Por qué segregar interfaces?");
        System.out.println("  Una interfaz con todo obliga a implementar métodos innecesarios.");
        System.out.println("  Con interfaces pequeñas, cada clase implementa solo lo que usa.\n");
        
        // SimpleCostService - solo implementa LoanCostCalculator
        System.out.println("SimpleCostService (solo calcula costes):");
        LoanPolicy policy = new StandardLoanPolicy();
        SimpleCostService costService = new SimpleCostService(policy);
        System.out.printf("  Coste préstamo 1: %.2f€%n", costService.calculateCost(prestamo1));
        System.out.printf("  Coste préstamo 2: %.2f€%n", costService.calculateCost(prestamo2));
        System.out.println("  -> No tiene métodos de notificación ni registro (no los necesita)\n");
        
        // StatisticsService - solo implementa LoanRegistry
        System.out.println("StatisticsService (solo estadísticas):");
        StatisticsService stats = new StatisticsService();
        stats.registerLoanWithCost(prestamo1, 12.0);
        stats.registerLoanWithCost(prestamo2, 23.0);
        stats.printStatistics();
        System.out.println("  -> No calcula costes ni notifica (no lo necesita)\n");
        
        // FullLoanService - implementa las 3 interfaces
        System.out.println("FullLoanService (implementa las 3 interfaces):");
        NotificationChannel channel = new ConsoleNotification();
        FullLoanService fullService = new FullLoanService(new PremiumLoanPolicy(), channel);
        
        fullService.processLoanFully(prestamo1);
        fullService.processLoanFully(prestamo2);
        System.out.println("  Estado: " + fullService);
        
        // Polimorfismo con interfaces segregadas
        System.out.println("\nPolimorfismo con interfaces segregadas:");
        LoanCostCalculator calc = fullService;
        LoanRegistry reg = fullService;
        System.out.printf("  Como LoanCostCalculator: %.2f€%n", calc.calculateCost(prestamo1));
        System.out.printf("  Como LoanRegistry: %d préstamos%n", reg.getTotalLoansProcessed());
        
        System.out.println("\n[ISP] Interfaces pequeñas y cohesionadas.");
        System.out.println("      Cada clase implementa solo lo que necesita.\n");
    }
}
