# Design

## Context

El proyecto actualmente no tiene enums del dominio. La estructura de paquetes ya existe: `concesionaria.model.enums` está vacío. Ver proposal.md para motivación.

## Goals / Non-Goals

**Goals:**
- Implementar TipoVehiculo y EstadoUnidad con campo descripcion y método fromString
- Proporcionar JavaDoc completo en español

**Non-Goals:**
- No crear enums para carrocerías o tipos de motocicleta (quedan como String libre)
- No cambiar arquitectura existente

## Decisions

**Enum pattern estándar de Java:**
- Usar `private final String descripcion` con constructor parametrizado
- Método `getDescripcion()` público
- Método estático `fromString(String)` que itera sobre `values()` comparando con `name()`, `descripcion` y variantes normalizadas (trim, uppercase)
- Lanzar `IllegalArgumentException` con mensaje descriptivo si no coincide

**Alternativa considerada:** Usar mapa estático para fromString. Rechazado porque con solo 3 y 2 valores respectivamente, la iteración es simple y no justifica la complejidad adicional.

**Normalización en fromString:**
- TipoVehiculo: comparar con name() (AUTOMOVIL), name() en minúsculas, descripcion (Automóvil), descripcion en minúsculas
- EstadoUnidad: comparar con name() (CERO_KM, USADO), name() en minúsculas, descripcion ("0 km", "Usado"), descripcion en minúsculas

## Risks / Trade-offs

**Riesgo:** fromString puede ser sensible a acentos o espacios en descripciones.
**Mitigación:** Usar trim() y normalización a mayúsculas en comparaciones, pero mantener descripciones originales con acentos para legibilidad.

**Trade-off:** fromString acepta múltiples formatos (nombre, descripción, variantes). Esto aumenta flexibilidad pero también complejidad de mantenimiento. Aceptable dado el dominio es estable y las descripciones no cambiarán frecuentemente.

## Migration Plan

No requiere migración. Es código nuevo sin dependencias existentes.

## Open Questions

Ninguna.
