# Design

## Context

El proyecto actualmente tiene interfaces (Identificable, EvaluatableComercial, Valuable, Reportable, Validable, Comparable) y enums (TipoVehiculo, EstadoUnidad) definidos, pero no tiene la jerarquía de clases del dominio. La estructura de paquetes está definida como `src/concesionaria/{modelo,repositorio,servicio,config}`. Este cambio establece la base del modelo del dominio.

Ver proposal.md - Why para la motivación del cambio.

## Goals / Non-Goals

**Goals:**
- Establecer la jerarquía de clases del dominio con Vehiculo abstracta y subclases específicas
- Cumplir requisitos del TFI: clases abstractas, herencia, polimorfismo, JavaBean/POJO, Wrappers
- Proveer base para capas posteriores (repository, service, ui)

**Non-Goals:**
- Implementación de equals/hashCode/toString (US-08)
- Integración con repositorio MSSQL (US-09)
- Lógica de negocio y validaciones (US-10)
- UI y menús de consola (US-11, US-12)

## Decisions

**Uso de Wrappers (Integer, Double) en lugar de primitivos:**
- **Decisión:** Usar `Integer` para `anio` y `kilometraje`, `Double` para `precioLista`
- **Rationale:** Requisito explícito del TFI sobre aplicación de Wrappers. Permite null para valores opcionales (ej. kilometraje null en 0km antes de validación)
- **Alternativa considerada:** Usar primitivos (int, double) - rechazado por no cumplir requisito TFI

**Vehiculo como clase abstracta:**
- **Decisión:** Vehiculo es `abstract`, no se puede instanciar directamente
- **Rationale:** Representa concepto genérico, solo las subclases concretas tienen sentido en el dominio. Cumple requisito TFI sobre clases abstractas
- **Alternativa considerada:** Clase concreta - rechazada por permitir instancias genéricas sin tipo específico

**Polimorfismo vía métodos abstractos:**
- **Decisión:** `obtenerTipoVehiculo()`, `obtenerTipoEstructura()`, `obtenerFichaDirectiva()` como abstractos en Vehiculo
- **Rationale:** Cada subclase implementa su comportamiento específico. Evita atributo `tipo` en Vehiculo que podría desincronizarse. Cumple requisito TFI sobre polimorfismo
- **Alternativa considerada:** Atributo `tipo` en Vehiculo con setter - rechazado por riesgo de inconsistencia

**Carrocería y tipoMotocicleta como String libre:**
- **Decisión:** No crear enums cerrados para carrocerías ni tipos de moto
- **Rationale:** Concesionaria multimarca necesita flexibilidad para agregar nuevos tipos sin modificar código. Validación ocurre en capa service (US-10)
- **Alternativa considerada:** Enums cerrados (ej. Carroceria.SEDAN, Carroceria.HATCHBACK) - rechazado por limitar catálogo multimarca

**Constructores JavaBean:**
- **Decisión:** Constructor público sin argumentos + constructor parametrizado completo
- **Rationale:** Requisito TFI sobre patrón JavaBean/POJO. Constructor vacío necesario para frameworks/serialización
- **Alternativa considerada:** Solo constructor parametrizado - rechazado por no cumplir JavaBean

**Implementación de interfaces:**
- **Decisión:** Vehiculo implementa Identificable, EvaluatableComercial, Serializable
- **Rationale:** Identificable y EvaluatableComercial ya definidos en proyecto. Serializable para persistencia futura
- **Alternativa considerada:** Implementar todas las interfaces (Valuable, Reportable, Validable, Comparable) - rechazado por ahora, se agregan cuando se necesiten en USs específicas

## Risks / Trade-offs

**Riesgo:** Validación de carrocería/tipoMotocicleta diferida a capa service
- **Mitigación:** Documentar claramente que validación ocurre en ConcesionariaService (US-10). Strings pueden ser vacíos o null temporalmente

**Trade-off:** Wrappers permiten null, de validación
- **Mitigación:** Validación centralizada en service layer (US-10) con excepciones DatoVehiculoInvalidoException

**Riesgo:** Polimorfismo vía métodos abstractos requiere implementación en cada subclase
- **Mitigación:** Compilador fuerza implementación. JavaDoc claro en Vehiculo sobre contrato

## Open Questions

Ninguna. El diseño está completamente determinado por requisitos del TFI y specs existentes.
