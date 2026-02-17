package library;

/**
 * Representa un préstamo de un libro.
 * 
 * SRP: solo guarda los datos del préstamo (qué libro, a quién, cuántos días).
 * El cálculo del coste lo hace LoanPolicy, las notificaciones van por otro lado.
 */
public class Loan {
    
    private final Book book;
    private final String borrowerName;
    private final int daysLoaned;
    
    /**
     * Crea un préstamo válido.
     * No permitimos días negativos o cero porque no tendría sentido.
     */
    public Loan(Book book, String borrowerName, int daysLoaned) {
        if (book == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del prestatario no puede estar vacío");
        }
        if (daysLoaned <= 0) {
            throw new IllegalArgumentException("Los días de préstamo deben ser mayor que 0");
        }
        
        this.book = book;
        this.borrowerName = borrowerName.trim();
        this.daysLoaned = daysLoaned;
    }
    
    public Book getBook() {
        return book;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public int getDaysLoaned() {
        return daysLoaned;
    }
    
    @Override
    public String toString() {
        return String.format("Loan[libro='%s', prestatario='%s', días=%d]",
                           book.getTitle(), borrowerName, daysLoaned);
    }
}
