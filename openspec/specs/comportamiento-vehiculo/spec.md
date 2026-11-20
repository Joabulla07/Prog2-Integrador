# comportamiento-vehiculo Specification

## Purpose

Define contratos de comportamiento para identificación, valoración comercial, reportes, validaciones y comparación de vehículos en el sistema de gestión de concesionaria.

## Requirements

### Requirement: Identificación de vehículo
El sistema SHALL permitir obtener el código de inventario único de un vehículo mediante la interfaz Identificable.

#### Scenario: Obtener código de inventario
- **WHEN** se invoca getCodigoInventario() en un vehículo
- **THEN** se devuelve el código de inventario único (ej. "AUTO-001")

### Requirement: Evaluación comercial
El sistema SHALL permitir clasificar vehículos comercialmente y determinar aptitud de entrega mediante la interfaz EvaluatableComercial.

#### Scenario: Categoría comercial por tipo y precio
- **WHEN** se invoca obtenerCategoriaComercial() en un vehículo
- **THEN** se devuelve una categoría combinando tipo y rango de precio (ej. "Automóvil Premium 0 km")

#### Scenario: Aptitud de entrega inmediata 0 km
- **WHEN** se invoca esAptoEntregaInmediata() en un vehículo 0 km
- **THEN** se devuelve true (siempre apto para entrega)

#### Scenario: Aptitud de entrega inmediata usado
- **WHEN** se invoca esAptoEntregaInmediata() en un vehículo usado
- **THEN** se devuelve true si kilometraje < 50000 km y antigüedad <= 5 años, false en caso contrario

### Requirement: Valoración de vehículo
El sistema SHALL permitir calcular valor de mercado, depreciación y margen de ganancia mediante la interfaz Valuable.

#### Scenario: Valor de mercado 0 km
- **WHEN** se invoca obtenerValorMercado() en un vehículo 0 km
- **THEN** se devuelve el precio lista sin ajustes

#### Scenario: Valor de mercado usado
- **WHEN** se invoca obtenerValorMercado() en un vehículo usado
- **THEN** se devuelve el precio lista ajustado por depreciación

#### Scenario: Cálculo de depreciación
- **WHEN** se invoca calcularDepreciacion() en un vehículo usado
- **THEN** se devuelve el porcentaje de depreciación basado en kilometraje y antigüedad

#### Scenario: Margen de ganancia
- **WHEN** se invoca obtenerMargenGanancia() en un vehículo
- **THEN** se devuelve el margen entre precio de compra y venta (0 para 0 km)

### Requirement: Reportes de vehículo
El sistema SHALL permitir generar reportes ejecutivos y detallados mediante la interfaz Reportable.

#### Scenario: Ficha directiva
- **WHEN** se invoca obtenerFichaDirectiva() en un vehículo
- **THEN** se devuelve un resumen ejecutivo con marca, modelo, año, precio y estado

#### Scenario: Resumen legible
- **WHEN** se invoca obtenerResumen() en un vehículo
- **THEN** se devuelve una descripción corta legible (ej. "Toyota Corolla 2026 - 0 km")

#### Scenario: Detalle completo
- **WHEN** se invoca obtenerDetalleCompleto() en un vehículo
- **THEN** se devuelve todos los campos en formato legible

### Requirement: Validación de vehículo
El sistema SHALL permitir validar reglas del dominio y obtener errores específicos mediante la interfaz Validable.

#### Scenario: Validación exitosa
- **WHEN** se invoca esVehiculoValido() en un vehículo con todos los datos correctos
- **THEN** se devuelve true

#### Scenario: Validación fallida
- **WHEN** se invoca esVehiculoValido() en un vehículo con datos inválidos
- **THEN** se devuelve false

#### Scenario: Obtener errores de validación
- **WHEN** se invoca obtenerErroresValidacion() en un vehículo con múltiples errores
- **THEN** se devuelve una lista con todos los errores específicos (ej. año fuera de rango, kilometraje negativo)
