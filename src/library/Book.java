package library;

/**
 * Representa un libro en el sistema.
 * 
 * Aplicamos SRP: esta clase solo se encarga de guardar los datos del libro.
 * No sabe nada de préstamos, usuarios ni costes - eso lo manejan otras clases.
 */
public class Book {
    
    private final String title;
    private final String author;
    private final String isbn;
    
    /**
     * Crea un libro con los datos básicos.
     * Validamos que no vengan valores vacíos porque un libro sin título no tiene sentido.
     */
    public Book(String title, String author, String isbn) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        
        this.title = title.trim();
        this.author = author.trim();
        this.isbn = isbn.trim();
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    @Override
    public String toString() {
        return String.format("Book[título='%s', autor='%s', ISBN='%s']", 
                           title, author, isbn);
    }
    
    // Dos libros son iguales si tienen el mismo ISBN
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }
    
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
