# Design

## Context

Paquete `interfaces/` existe vacío en `src/concesionaria/`. Excepciones ya implementadas en US-05. Ver proposal.md para motivación.

## Goals / Non-Goals

**Goals:**
- 6 interfaces con contratos de comportamiento separados por responsabilidad
- JavaDoc completo explicando contratos y precondiciones
- Interfaces diseñadas para implementación en jerarquía Vehiculo (US-07)

**Non-Goals:**
- Implementación de lógica de negocio (solo contratos)
- Interfaces genéricas reutilizables fuera del dominio de concesionaria
- Dependencias externas o frameworks

## Decisions

**Separación por responsabilidad (SRP):**
- `Identificable`: solo identificación (código de inventario). Rationale: contrato simple, reutilizable en otros contextos.
- `EvaluatableComercial`: clasificación comercial y aptitud de entrega. Rationale: lógica de ventas separada de valoración.
- `Valuable`: valoración financiera (precio, depreciación, margen). Rationale: cálculos monetarios complejos aislados.
- `Reportable`: generación de reportes en diferentes formatos. Rationale: presentación separada de lógica.
- `Validable`: validaciones de dominio. Rationale: reutilizable en múltiples puntos (registro, actualización).
- `Comparable`: ordenamiento para listados. Rationale: permite sorting sin exponer lógica interna.

**Categoría comercial (EvaluatableComercial.obtenerCategoriaComercial):**
- Combinación tipo + rango de precio: "Económico" (<15M), "Medio" (15-40M), "Premium" (>40M)
- Ejemplo: "Automóvil Premium 0 km", "Camioneta Media Usada"
- Rationale: criterio estándar en concesionarias, fácil de filtrar en reportes.

**Aptitud de entrega (EvaluatableComercial.esAptoEntregaInmediata):**
- 0 km: siempre true (stock disponible, sin revisión)
- Usado: true si kilometraje < 50000 km Y antigüedad <= 5 años
- Rationale: criterios realistas de concesionaria (usados con poco uso/aptos, viejos requieren revisión).

**Depreciación (Valuable.calcularDepreciacion):**
- Fórmula: 10% por año de antigüedad + 5% por cada 10000 km
- Máximo 70% de depreciación (valor mínimo 30% del precio lista)
- Rationale: fórmula estándar del mercado, simple pero realista.

**Margen de ganancia (Valuable.obtenerMargenGanancia):**
- 0 km: 0% (no hay precio de compra)
- Usado: 20% sobre precio de compra (fijo por simplicidad)
- Rationale: simplificación para TFI, en producción sería variable por vehículo.

**Validable.obtenerErroresValidacion:**
- Devuelve List<String> con mensajes descriptivos
- Rationale: permite mostrar múltiples errores al usuario en UI (US-11/US-12).

**Alternativa considerada:** Una sola interfaz `VehiculoBehavior` con todos los métodos. Rechazada porque viola SRP, difícil de implementar parcialmente, y acoplamiento innecesario.

**Paquete:** `concesionaria.interfaces` (sigue convención existente).

## Risks / Trade-offs

**Trade-off:** 6 interfaces pueden parecer excesivas para TFI. Rationale: SRP y extensibilidad; si en el futuro se agrega comportamiento, no se rompen interfaces existentes.

**Riesgo:** Cálculos de depreciación/margen son simplificaciones. Mitigación: JavaDoc indica que son aproximaciones para TFI, en producción usarían algoritmos más complejos.

**Riesgo:** Comparable usa métodos específicos en lugar de java.lang.Comparable. Rationale: control total sobre criterios de comparación, permite múltiples criterios sin conflictos.
