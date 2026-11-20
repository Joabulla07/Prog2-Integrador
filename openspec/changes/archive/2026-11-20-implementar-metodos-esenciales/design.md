# Design

## Context

Las clases Vehiculo, Automovil, Camioneta y Motocicleta ya existen en `src/concesionaria/model/` con atributos, constructores, getters/setters y métodos polimórficos implementados. Actualmente no sobreescriben equals, hashCode ni toString, lo que causa comportamiento incorrecto en colecciones y dificulta el debugging.

Ver proposal.md - Why para la motivación del cambio.

## Goals / Non-Goals

**Goals:**
- Implementar equals/hashCode/toString en Vehiculo para comportamiento correcto en colecciones
- Implementar toString específico en subclases para mostrar atributos adicionales
- Cumplir requisito del TFI sobre sobreescritura de métodos esenciales

**Non-Goals:**
- Cambios en la lógica de negocio o validaciones
- Modificaciones en la estructura de clases o atributos
- Cambios en persistencia o configuración

## Decisions

**Normalización de codigoInventario en equals:**
- **Decisión:** Usar `trim().toUpperCase()` para normalizar codigoInventario antes de comparar
- **Rationale:** Permite que "AUTO-001", "auto-001" y "  AUTO-001  " se consideren iguales, evitando duplicados por diferencias de formato
- **Alternativa considerada:** Comparación exacta sin normalización - rechazada por riesgo de duplicados con variaciones de formato

**Uso de Objects.hash para hashCode:**
- **Decisión:** Usar `Objects.hash(codigoInventario)` en lugar de implementación manual
- **Rationale:** Objects.hash maneja null correctamente y es el estándar moderno de Java. Garantiza consistencia con equals cuando se usa el mismo campo
- **Alternativa considerada:** Implementación manual con multiplicadores (31 * hash + field) - rechazada por ser más propensa a errores y menos legible

**Formato de toString en Vehiculo:**
- **Decisión:** Formato compacto: "Vehiculo[tipo=..., marca=..., modelo=..., anio=..., codigo=...]"
- **Rationale:** Balance entre legibilidad y concisión. Incluye información clave para identificación rápida
- **Alternativa considerada:** Formato JSON o multi-línea - rechazado por ser más verboso de lo necesario para debugging

**Encadenamiento de super.toString() en subclases:**
- **Decisión:** Las subclases llaman `super.toString()` y concatenan su atributo específico
- **Rationale:** Evita duplicación de código y garantiza que cambios en Vehiculo.toString() se propagan a subclases
- **Alternativa considerada:** Reimplementar toString completo en cada subclase - rechazada por duplicación de código

**Manejo de null en toString:**
- **Decisión:** Usar marcadores "[sin valor]" para atributos null
- **Rationale:** Permite identificar fácilmente qué campos faltan sin lanzar NullPointerException
- **Alternativa considerada:** Lanzar excepción o mostrar "null" - rechazado por ser menos informativo o más propenso a errores

## Risks / Trade-offs

**Riesgo:** Normalización agresiva en equals puede causar colisiones no deseadas
- **Mitigación:** La normalización es intencional para evitar duplicados por formato. Si se requiere distinción case-sensitive, se puede cambiar en el futuro

**Trade-off:** toString no es un formato serializable (no es JSON ni XML)
- **Mitigación:** toString es para debugging humano, no para persistencia. La persistencia usa Sql2o con mapeo explícito

**Riesgo:** hashCode puede colisionar con diferentes códigos normalizados
- **Mitigación:** Colisiones de hashCode son aceptables en Java; equals resuelve la ambigüedad. Objects.hash proporciona buena distribución

## Open Questions

Ninguna. El diseño está completamente determinado por requisitos del TFI y el spec.
