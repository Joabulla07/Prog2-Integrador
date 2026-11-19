# Design

## Context

Paquete `exception/` existe vacío en `src/concesionaria/`. Enums `TipoVehiculo` y `EstadoUnidad` ya implementados en `model.enums/`. Ver proposal.md para motivación.

## Goals / Non-Goals

**Goals:**
- Tres clases de excepción con campos adicionales para contexto de error
- JavaDoc completo en constructores y clases
- Checked exceptions para validaciones (Exception), unchecked para persistencia (RuntimeException)

**Non-Goals:**
- Subtipos por tipo de error específico (ej. AnioInvalidoException)
- Lógica de validación dentro de las excepciones (solo transporte de contexto)

## Decisions

**Estructura de excepciones:**
- `DatoVehiculoInvalidoException extends Exception` con campos `campoInvalido` (String) y `valorInvalido` (String). Constructores: (mensaje, campo, valor) y (mensaje, campo, valor, causa). Rationale: permite identificar qué campo falló y su valor sin recrear el objeto validado.
- `VehiculoNoEncontradoException extends Exception` con campo `codigoBuscado` (String). Constructores: (mensaje, codigo) y (mensaje, codigo, causa). Rationale: el código es la clave de búsqueda, útil para debugging y mensajes de error.
- `PersistenciaException extends RuntimeException` con campos `causa` (Throwable) y `operacion` (String). Constructores: (mensaje, causa) y (mensaje, operacion, causa). Rationale: unchecked porque fallos de DB son generalmente no recuperables; preservar causa es crítico para debugging Sql2o.

**Alternativa considerada:** Una sola excepción genérica con código de error. Rechazada porque pierde tipado y obliga a switch/case en catch blocks.

**Paquete:** `concesionaria.exception` (sigue convención existente del proyecto).

## Risks / Trade-offs

**Trade-off:** Checked exceptions (Exception) obligan a declarar throws o capturar. Rationale: validaciones de dominio son recuperables (el usuario puede corregir datos), por lo que checked es apropiado. Persistencia es unchecked porque fallos de DB suelen ser fatales.

**Riesgo:** Sobrecarga de constructores puede confundir. Mitigación: JavaDoc claro en cada constructor con ejemplos de uso.
