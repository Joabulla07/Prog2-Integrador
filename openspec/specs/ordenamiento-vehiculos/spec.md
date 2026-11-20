# ordenamiento-vehiculos Specification

## Purpose

Permite ordenar colecciones de vehículos mediante un orden natural basado en el código de inventario y comparadores externos para criterios comerciales.

## Requirements

### Requirement: Orden natural por código de inventario
El sistema SHALL permitir ordenar `Vehiculo` mediante `java.lang.Comparable<Vehiculo>` usando únicamente el código de inventario normalizado.

#### Scenario: Códigos normalizados diferentes
- **WHEN** se comparan dos vehículos con códigos normalizados distintos
- **THEN** el resultado indica cuál precede al otro según el orden lexicográfico del código

#### Scenario: Código normalizado igual
- **WHEN** dos vehículos tienen el mismo código normalizado, aunque pertenezcan a subclases distintas
- **THEN** el resultado de la comparación es cero

#### Scenario: Código nulo
- **WHEN** uno de los vehículos tiene código nulo
- **THEN** el vehículo sin código se ubica después y no se lanza una excepción

### Requirement: Consistencia entre comparación e igualdad
El sistema SHALL mantener coherencia entre el orden natural y la igualdad basada en código de inventario.

#### Scenario: Vehículos iguales
- **WHEN** dos vehículos son iguales según `equals`
- **THEN** su comparación mediante `compareTo` devuelve cero

### Requirement: Comparadores alternativos
El sistema SHALL proporcionar comparadores para ordenar vehículos por precio, año y marca/modelo.

#### Scenario: Ordenamiento por precio
- **WHEN** se solicita ordenar una lista por precio
- **THEN** los vehículos quedan ordenados por precio ascendente y los valores nulos quedan al final

#### Scenario: Ordenamiento por año
- **WHEN** se solicita ordenar una lista por año
- **THEN** los vehículos quedan ordenados por año según el criterio seleccionado

#### Scenario: Ordenamiento por marca y modelo
- **WHEN** se solicita ordenar una lista por marca y modelo
- **THEN** los vehículos quedan ordenados primero por marca y luego por modelo

### Requirement: Compatibilidad con la API estándar de colecciones
El sistema SHALL permitir ordenar `List<Vehiculo>` con `Collections.sort(...)` o `List.sort(...)`.

#### Scenario: Uso del orden natural
- **WHEN** se ordena una lista con el comparador natural
- **THEN** la lista queda ordenada por código de inventario normalizado

#### Scenario: Uso de comparador externo
- **WHEN** se ordena una lista con un comparador externo
- **THEN** la lista queda ordenada según el criterio de ese comparador
