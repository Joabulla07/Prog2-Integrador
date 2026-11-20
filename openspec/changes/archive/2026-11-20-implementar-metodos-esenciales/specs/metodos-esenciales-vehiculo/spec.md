# Spec Delta

## Purpose

Implementa los métodos esenciales equals, hashCode y toString en la jerarquía de clases Vehiculo para garantizar comportamiento correcto en colecciones y facilitar el debugging.

## ADDED Requirements

### Requirement: Método equals en Vehiculo por código de inventario
El sistema SHALL sobreescribir el método equals en Vehiculo para comparar instancias por su código de inventario normalizado.

#### Scenario: Igualdad por mismo código de inventario
- **WHEN** se comparan dos instancias de Vehiculo con el mismo codigoInventario (ignorando mayúsculas/minúsculas y espacios)
- **THEN** equals devuelve true

#### Scenario: Desigualdad por diferente código de inventario
- **WHEN** se comparan dos instancias de Vehiculo con diferentes codigoInventario
- **THEN** equals devuelve false

#### Scenario: Desigualdad con null
- **WHEN** se compara una instancia de Vehiculo con null
- **THEN** equals devuelve false

#### Scenario: Desigualdad con diferente tipo
- **WHEN** se compara una instancia de Vehiculo con un objeto de otra clase
- **THEN** equals devuelve false

### Requirement: Método hashCode en Vehiculo consistente con equals
El sistema SHALL sobreescribir el método hashCode en Vehiculo usando Objects.hash con codigoInventario para garantizar consistencia con equals.

#### Scenario: hashCode consistente con equals
- **WHEN** dos instancias de Vehiculo tienen el mismo codigoInventario
- **THEN** hashCode devuelve el mismo valor para ambas

#### Scenario: hashCode diferente para códigos diferentes
- **WHEN** dos instancias de Vehiculo tienen diferentes codigoInventario
- **THEN** hashCode devuelve valores diferentes (probabilísticamente)

### Requirement: Método toString en Vehiculo con información legible
El sistema SHALL sobreescribir el método toString en Vehiculo para devolver una representación legible que incluya el tipo de vehículo y atributos principales.

#### Scenario: toString muestra información básica
- **WHEN** se invoca toString en una instancia de Vehiculo
- **THEN** devuelve un String con tipo de vehículo, marca, modelo, año y código de inventario

#### Scenario: toString maneja atributos null
- **WHEN** se invoca toString en una instancia de Vehiculo con atributos null
- **THEN** devuelve un String con marcadores "[sin valor]" para los atributos null

### Requirement: Método toString en Automovil con carrocería
El sistema SHALL sobreescribir el método toString en Automovil encadenando super.toString() e incluyendo el atributo carrocería.

#### Scenario: toString de Automovil incluye carrocería
- **WHEN** se invoca toString en una instancia de Automovil
- **THEN** devuelve el resultado de super.toString() concatenado con el tipo de carrocería

### Requirement: Método toString en Camioneta con carrocería
El sistema SHALL sobreescribir el método toString en Camioneta encadenando super.toString() e incluyendo el atributo carrocería.

#### Scenario: toString de Camioneta incluye carrocería
- **WHEN** se invoca toString en una instancia de Camioneta
- **THEN** devuelve el resultado de super.toString() concatenado con el tipo de carrocería

### Requirement: Método toString en Motocicleta con tipo
El sistema SHALL sobreescribir el método toString en Motocicleta encadenando super.toString() e incluyendo el atributo tipoMotocicleta.

#### Scenario: toString de Motocicleta incluye tipo
- **WHEN** se invoca toString en una instancia de Motocicleta
- **THEN** devuelve el resultado de super.toString() concatenado con el tipo de motocicleta
