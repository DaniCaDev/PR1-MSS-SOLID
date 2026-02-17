# Práctica 1 - Diseño Orientado a Objetos y Principios SOLID

## Universidad de La Laguna
### Grado en Ingeniería Informática
### Modelado de Sistemas Software - Curso 2025-2026

---

## 📋 Índice

1. [Descripción](#descripción)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Diagramas UML](#diagramas-uml)
4. [Principios SOLID Aplicados](#principios-solid-aplicados)
5. [Cómo Ejecutar](#cómo-ejecutar)
6. [Pruebas Realizadas](#pruebas-realizadas)

---

## Descripción

Sistema de gestión de préstamos de biblioteca que demuestra la aplicación de los **principios SOLID** en Java:

- **S**ingle Responsibility Principle (SRP)
- **O**pen/Closed Principle (OCP)
- **L**iskov Substitution Principle (LSP)
- **I**nterface Segregation Principle (ISP)
- **D**ependency Inversion Principle (DIP)

---

## Estructura del Proyecto

```
practica1_solid/
└── src/
    ├── Main.java                    # Punto de entrada y pruebas
    │
    ├── library/                     # Modelo de dominio (Ejercicio 1 - SRP)
    │   ├── Book.java               # Representa un libro
    │   └── Loan.java               # Representa un préstamo
    │
    ├── policy/                      # Políticas de préstamo (Ejercicio 2 - OCP)
    │   ├── LoanPolicy.java         # Interfaz de política
    │   ├── StandardLoanPolicy.java # Política estándar
    │   └── PremiumLoanPolicy.java  # Política premium
    │
    ├── notification/                # Sistema de notificaciones (Ejercicio 3 - DIP)
    │   ├── NotificationChannel.java    # Interfaz de canal
    │   └── ConsoleNotification.java    # Implementación consola
    │
    ├── service/                     # Servicios de negocio (Ejercicio 3 - DIP)
    │   └── LoanService.java        # Servicio principal
    │
    └── isp/                         # Interfaces segregadas (Ejercicio 4 - ISP)
        ├── LoanCostCalculator.java  # Interfaz cálculo
        ├── LoanNotifier.java        # Interfaz notificación
        ├── LoanRegistry.java        # Interfaz registro
        ├── SimpleCostService.java   # Solo calcula costes
        ├── StatisticsService.java   # Solo estadísticas
        └── FullLoanService.java     # Implementa todo
```

---

## Diagramas UML

### Diagrama de Clases General

```mermaid
classDiagram
    class Book {
        -String title
        -String author
        -String isbn
        +Book(String, String, String)
        +getTitle() String
        +getAuthor() String
        +getIsbn() String
        +toString() String
    }

    class Loan {
        -Book book
        -String borrowerName
        -int daysLoaned
        +Loan(Book, String, int)
        +getBook() Book
        +getBorrowerName() String
        +getDaysLoaned() int
        +toString() String
    }

    Loan "1" --> "1" Book : contiene
```

### Diagrama de Políticas (OCP + LSP)

```mermaid
classDiagram
    class LoanPolicy {
        <<interface>>
        +calculateCost(Loan) double
        +getPolicyName() String
    }

    class StandardLoanPolicy {
        -double DAILY_RATE = 1.00
        -int STANDARD_PERIOD = 14
        -double EXTRA_DAY_RATE = 0.50
        +calculateCost(Loan) double
        +getPolicyName() String
    }

    class PremiumLoanPolicy {
        -double DAILY_RATE = 0.75
        -int DISCOUNT_THRESHOLD = 7
        -double DISCOUNT_PERCENTAGE = 0.10
        +calculateCost(Loan) double
        +getPolicyName() String
    }

    class FuturePolicy {
        <<extensible>>
        +calculateCost(Loan) double
        +getPolicyName() String
    }

    LoanPolicy <|.. StandardLoanPolicy : implements
    LoanPolicy <|.. PremiumLoanPolicy : implements
    LoanPolicy <|.. FuturePolicy : implements

    note for LoanPolicy "OCP: Abierto para extensión\nCerrado para modificación"
```

### Diagrama de Notificaciones (DIP)

```mermaid
classDiagram
    class NotificationChannel {
        <<interface>>
        +notify(String message) void
        +getChannelName() String
    }

    class ConsoleNotification {
        -String PREFIX
        +notify(String message) void
        +getChannelName() String
    }

    class EmailNotification {
        <<futuro>>
        +notify(String message) void
        +getChannelName() String
    }

    class SMSNotification {
        <<futuro>>
        +notify(String message) void
        +getChannelName() String
    }

    NotificationChannel <|.. ConsoleNotification : implements
    NotificationChannel <|.. EmailNotification : implements
    NotificationChannel <|.. SMSNotification : implements

    note for NotificationChannel "DIP: Abstracción que permite\ndesacoplar implementaciones"
```

### Diagrama de LoanService (DIP - Inyección de Dependencias)

```mermaid
classDiagram
    class LoanService {
        -LoanPolicy policy
        -NotificationChannel notificationChannel
        +LoanService(LoanPolicy, NotificationChannel)
        +processLoan(Loan) double
        +getPolicyInfo() String
        +getNotificationChannelInfo() String
    }

    class LoanPolicy {
        <<interface>>
        +calculateCost(Loan) double
    }

    class NotificationChannel {
        <<interface>>
        +notify(String) void
    }

    class StandardLoanPolicy {
        +calculateCost(Loan) double
    }

    class PremiumLoanPolicy {
        +calculateCost(Loan) double
    }

    class ConsoleNotification {
        +notify(String) void
    }

    LoanService --> LoanPolicy : depende de abstracción
    LoanService --> NotificationChannel : depende de abstracción
    
    LoanPolicy <|.. StandardLoanPolicy
    LoanPolicy <|.. PremiumLoanPolicy
    NotificationChannel <|.. ConsoleNotification

    note for LoanService "DIP: Dependencias inyectadas\npor constructor (no usa 'new')"
```

### Diagrama ISP - Comparación Interfaz Monolítica vs Segregada

```mermaid
classDiagram
    class LoanManagementService {
        <<interface - VIOLA ISP>>
        +calculateCost(Loan) double
        +notifyUser(String) void
        +registerLoan(Loan) void
        +getTotalLoansProcessed() int
    }

    note for LoanManagementService "❌ MALO: Obliga a implementar\ntodos los métodos aunque\nno se necesiten"
```

### Diagrama ISP - Interfaces Segregadas (Solución Correcta)

```mermaid
classDiagram
    class LoanCostCalculator {
        <<interface>>
        +calculateCost(Loan) double
    }

    class LoanNotifier {
        <<interface>>
        +notifyLoan(Loan, String) void
        +sendNotification(String) void
    }

    class LoanRegistry {
        <<interface>>
        +registerLoan(Loan) void
        +getTotalLoansProcessed() int
        +getTotalRevenue() double
    }

    class SimpleCostService {
        -LoanPolicy policy
        +calculateCost(Loan) double
    }

    class StatisticsService {
        -List~Loan~ registeredLoans
        -double totalRevenue
        +registerLoan(Loan) void
        +getTotalLoansProcessed() int
        +getTotalRevenue() double
    }

    class FullLoanService {
        -LoanPolicy policy
        -NotificationChannel channel
        -List~Loan~ registeredLoans
        +calculateCost(Loan) double
        +notifyLoan(Loan, String) void
        +sendNotification(String) void
        +registerLoan(Loan) void
        +getTotalLoansProcessed() int
        +processLoanFully(Loan) double
    }

    LoanCostCalculator <|.. SimpleCostService : implements
    LoanRegistry <|.. StatisticsService : implements
    
    LoanCostCalculator <|.. FullLoanService : implements
    LoanNotifier <|.. FullLoanService : implements
    LoanRegistry <|.. FullLoanService : implements

    note for SimpleCostService "Solo implementa lo que necesita:\nLoanCostCalculator"
    note for StatisticsService "Solo implementa lo que necesita:\nLoanRegistry"
    note for FullLoanService "Implementa las 3 interfaces\nporque las NECESITA todas"
```

### Diagrama de Secuencia - Procesar Préstamo

```mermaid
sequenceDiagram
    participant Main
    participant LoanService
    participant LoanPolicy
    participant NotificationChannel

    Main->>LoanService: processLoan(loan)
    activate LoanService
    
    LoanService->>LoanPolicy: calculateCost(loan)
    activate LoanPolicy
    LoanPolicy-->>LoanService: cost (double)
    deactivate LoanPolicy
    
    LoanService->>LoanService: buildNotificationMessage(loan, cost)
    
    LoanService->>NotificationChannel: notify(message)
    activate NotificationChannel
    NotificationChannel-->>LoanService: void
    deactivate NotificationChannel
    
    LoanService-->>Main: cost (double)
    deactivate LoanService
```

### Diagrama de Paquetes

```mermaid
graph TB
    subgraph "src"
        Main[Main.java]
        
        subgraph "library"
            Book[Book.java]
            Loan[Loan.java]
        end
        
        subgraph "policy"
            LoanPolicy[LoanPolicy.java]
            StandardLoanPolicy[StandardLoanPolicy.java]
            PremiumLoanPolicy[PremiumLoanPolicy.java]
        end
        
        subgraph "notification"
            NotificationChannel[NotificationChannel.java]
            ConsoleNotification[ConsoleNotification.java]
        end
        
        subgraph "service"
            LoanService[LoanService.java]
        end
        
        subgraph "isp"
            LoanCostCalculator[LoanCostCalculator.java]
            LoanNotifier[LoanNotifier.java]
            LoanRegistry[LoanRegistry.java]
            SimpleCostService[SimpleCostService.java]
            StatisticsService[StatisticsService.java]
            FullLoanService[FullLoanService.java]
        end
    end
    
    Main --> library
    Main --> policy
    Main --> notification
    Main --> service
    Main --> isp
    
    service --> library
    service --> policy
    service --> notification
    
    isp --> library
    isp --> policy
    isp --> notification
```

---

## Principios SOLID Aplicados

### 1. SRP - Single Responsibility Principle (Ejercicio 1)

**"Una clase debe tener una, y solo una, razón para cambiar"**

| Clase | Responsabilidad | ¿Qué NO hace? |
|-------|-----------------|---------------|
| `Book` | Representar datos de un libro | No gestiona préstamos, no calcula costes |
| `Loan` | Representar datos de un préstamo | No calcula costes, no envía notificaciones |

```java
// Book tiene UNA responsabilidad: representar un libro
public class Book {
    private final String title;
    private final String author;
    private final String isbn;
    // Solo getters, representa datos
}

// Loan tiene UNA responsabilidad: representar un préstamo
public class Loan {
    private final Book book;
    private final String borrowerName;
    private final int daysLoaned;
    // Solo getters, representa datos
}
```

### 2. OCP - Open/Closed Principle (Ejercicio 2)

**"Las entidades software deben estar abiertas para extensión, pero cerradas para modificación"**

```java
// Interfaz que define el contrato (cerrada para modificación)
public interface LoanPolicy {
    double calculateCost(Loan loan);
}

// Nueva política = Nueva clase (abierto para extensión)
// NO modificamos código existente
public class StudentLoanPolicy implements LoanPolicy {
    @Override
    public double calculateCost(Loan loan) {
        return loan.getDaysLoaned() * 0.50; // Descuento estudiante
    }
}
```

### 3. LSP - Liskov Substitution Principle (Ejercicio 2)

**"Los subtipos deben ser sustituibles por sus tipos base"**

```java
// Cualquier LoanPolicy puede sustituir a otra
LoanPolicy policy = new StandardLoanPolicy();
double cost1 = policy.calculateCost(loan);

policy = new PremiumLoanPolicy();  // Sustituimos sin problemas
double cost2 = policy.calculateCost(loan);
// El programa sigue funcionando correctamente
```

### 4. DIP - Dependency Inversion Principle (Ejercicio 3)

**"Depende de abstracciones, no de implementaciones concretas"**

```java
public class LoanService {
    // Dependemos de INTERFACES (abstracciones)
    private final LoanPolicy policy;
    private final NotificationChannel notificationChannel;
    
    // Inyección por constructor
    public LoanService(LoanPolicy policy, NotificationChannel channel) {
        this.policy = policy;
        this.notificationChannel = channel;
    }
    
    // NO hacemos esto (violaría DIP):
    // private final ConsoleNotification channel = new ConsoleNotification();
}
```

### 5. ISP - Interface Segregation Principle (Ejercicio 4)

**"Los clientes no deben verse forzados a depender de interfaces que no usan"**

```java
// ❌ MAL: Interfaz monolítica
interface LoanManagementService {
    double calculateCost(Loan loan);     // No todos lo necesitan
    void notifyUser(String message);     // No todos lo necesitan
    void registerLoan(Loan loan);        // No todos lo necesitan
    int getTotalLoansProcessed();        // No todos lo necesitan
}

// ✓ BIEN: Interfaces segregadas
interface LoanCostCalculator {
    double calculateCost(Loan loan);
}

interface LoanNotifier {
    void notifyLoan(Loan loan, String msg);
}

interface LoanRegistry {
    void registerLoan(Loan loan);
    int getTotalLoansProcessed();
}

// Cada servicio implementa SOLO lo que necesita
class SimpleCostService implements LoanCostCalculator { ... }
class StatisticsService implements LoanRegistry { ... }
class FullLoanService implements LoanCostCalculator, LoanNotifier, LoanRegistry { ... }
```

---

## Cómo Ejecutar

### Requisitos
- Java JDK 11 o superior

### Compilación y Ejecución

```bash
# 1. Navegar al directorio del proyecto
cd practica1_solid

# 2. Compilar todos los archivos
javac src/**/*.java

# 3. Ejecutar el programa
java -cp src Main
```

### Salida Esperada

El programa ejecutará automáticamente todas las pruebas y mostrará:
- Pruebas del Ejercicio 1 (SRP)
- Pruebas del Ejercicio 2 (OCP + Polimorfismo)
- Pruebas del Ejercicio 3 (DIP)
- Pruebas del Ejercicio 4 (ISP - Bonus)

---

## Pruebas Realizadas

### Ejercicio 1 - SRP
- ✓ Creación de libros válidos
- ✓ Validación de libro con título vacío
- ✓ Creación de préstamos válidos
- ✓ Validación de préstamo con días negativos
- ✓ Demostración de responsabilidades separadas

### Ejercicio 2 - OCP + Polimorfismo
- ✓ Cálculo con StandardLoanPolicy (préstamo corto)
- ✓ Cálculo con StandardLoanPolicy (préstamo largo con recargo)
- ✓ Cálculo con PremiumLoanPolicy (préstamo corto)
- ✓ Cálculo con PremiumLoanPolicy (préstamo largo con descuento)
- ✓ Polimorfismo: misma variable, diferente comportamiento
- ✓ Demostración de extensibilidad sin modificación

### Ejercicio 3 - DIP
- ✓ Creación de dependencias como abstracciones
- ✓ Inyección de dependencias por constructor
- ✓ Procesamiento de préstamos (cálculo + notificación)
- ✓ Cambio de política sin modificar LoanService
- ✓ Demostración de desacoplamiento

### Ejercicio 4 - ISP (Bonus)
- ✓ SimpleCostService implementa solo LoanCostCalculator
- ✓ StatisticsService implementa solo LoanRegistry
- ✓ FullLoanService implementa las 3 interfaces
- ✓ Ningún servicio tiene métodos vacíos
- ✓ Polimorfismo con interfaces segregadas

---

## Autor

Práctica desarrollada para la asignatura **Modelado de Sistemas Software**  
Grado en Ingeniería Informática  
Universidad de La Laguna

---

## Licencia

LICENCIA MIT