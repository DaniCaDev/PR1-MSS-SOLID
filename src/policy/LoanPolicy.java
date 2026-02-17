package policy;

import library.Loan;

/**
 * Interfaz para las políticas de préstamo.
 * 
 * Esto es OCP en acción: si queremos añadir una nueva política (ej: StudentPolicy),
 * solo creamos una nueva clase que implemente esta interfaz. No tocamos nada más.
 * 
 * También aplica LSP: cualquier política puede sustituir a otra sin romper el programa.
 */
public interface LoanPolicy {
    
    /**
     * Calcula cuánto cuesta el préstamo según esta política.
     */
    double calculateCost(Loan loan);
    
    /**
     * Nombre de la política para mostrar al usuario.
     */
    String getPolicyName();
}
