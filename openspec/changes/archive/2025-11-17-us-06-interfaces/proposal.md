# Proposal

## Why

El TFI requiere implementación de interfaces y transmisión de comportamiento. Las interfaces del doc original (Identificable, EvaluatableComercial) son insuficientes para un sistema de gestión de concesionaria real que necesita valoración, reportes, validaciones y ordenamiento de inventario.

## What Changes

- Crear paquete `concesionaria.interfaces` con 6 interfaces de comportamiento:
  - `Identificable`: `getCodigoInventario()`
  - `EvaluatableComercial`: `obtenerCategoriaComercial()`, `esAptoEntregaInmediata()`
  - `Valuable`: `obtenerValorMercado()`, `calcularDepreciacion()`, `obtenerMargenGanancia()`
  - `Reportable`: `obtenerFichaDirectiva()`, `obtenerResumen()`, `obtenerDetalleCompleto()`
  - `Validable`: `esVehiculoValido()`, `obtenerErroresValidacion()`
  - `Comparable`: `compararPorPrecio()`, `compararPorAntiguedad()`
- JavaDoc completo en cada método explicando contratos y precondiciones
- Interfaces diseñadas para ser implementadas por la jerarquía Vehiculo (US-07)

## Capabilities

### New Capabilities
- `comportamiento-vehiculo`: contratos de comportamiento para identificación, valoración comercial, reportes, validaciones y comparación de vehículos

### Modified Capabilities
- Ninguna

## Impact

- Paquete `interfaces/` nuevo en `src/concesionaria/`
- Interfaces usadas en US-07 (Vehiculo y subclases)
- Sin cambios en dependencias externas
