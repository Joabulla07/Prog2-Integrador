# domain-enums Specification

## Purpose

Define tipos enumerativos para clasificar vehículos por familia y estado de unidad, permitiendo conversión desde texto y descripciones legibles en español.

## Requirements

### Requirement: Enum TipoVehiculo clasifica familia de vehículos
El sistema SHALL proporcionar un enum TipoVehiculo con valores AUTOMOVIL, CAMIONETA y MOTOCICLETA, cada uno con una descripción legible en español.

#### Scenario: Obtener descripción de tipo de vehículo
- **WHEN** se invoca getDescripcion() en TipoVehiculo.AUTOMOVIL
- **THEN** el sistema devuelve "Automóvil"

#### Scenario: Obtener descripción de camioneta
- **WHEN** se invoca getDescripcion() en TipoVehiculo.CAMIONETA
- **THEN** el sistema devuelve "Camioneta"

#### Scenario: Obtener descripción de motocicleta
- **WHEN** se invoca getDescripcion() en TipoVehiculo.MOTOCICLETA
- **THEN** el sistema devuelve "Motocicleta"

### Requirement: Enum TipoVehiculo convierte desde texto
El sistema SHALL proporcionar un método estático fromString(String) en TipoVehiculo que acepte el nombre del enum (mayúsculas/minúsculas) o la descripción en español.

#### Scenario: Conversión desde nombre en mayúsculas
- **WHEN** se invoca TipoVehiculo.fromString("AUTOMOVIL")
- **THEN** el sistema devuelve TipoVehiculo.AUTOMOVIL

#### Scenario: Conversión desde nombre en minúsculas
- **WHEN** se invoca TipoVehiculo.fromString("automovil")
- **THEN** el sistema devuelve TipoVehiculo.AUTOMOVIL

#### Scenario: Conversión desde descripción en español
- **WHEN** se invoca TipoVehiculo.fromString("Automóvil")
- **THEN** el sistema devuelve TipoVehiculo.AUTOMOVIL

#### Scenario: Error en conversión con texto inválido
- **WHEN** se invoca TipoVehiculo.fromString("INVALIDO")
- **THEN** el sistema lanza IllegalArgumentException

### Requirement: Enum EstadoUnidad clasifica estado de unidad
El sistema SHALL proporcionar un enum EstadoUnidad con valores CERO_KM y USADO, cada uno con una descripción legible en español.

#### Scenario: Obtener descripción de CERO_KM
- **WHEN** se invoca getDescripcion() en EstadoUnidad.CERO_KM
- **THEN** el sistema devuelve "0 km"

#### Scenario: Obtener descripción de USADO
- **WHEN** se invoca getDescripcion() en EstadoUnidad.USADO
- **THEN** el sistema devuelve "Usado"

### Requirement: Enum EstadoUnidad convierte desde texto
El sistema SHALL proporcionar un método estático fromString(String) en EstadoUnidad que acepte el nombre del enum (mayúsculas/minúsculas), la descripción en español o variantes comunes.

#### Scenario: Conversión desde nombre en mayúsculas
- **WHEN** se invoca EstadoUnidad.fromString("CERO_KM")
- **THEN** el sistema devuelve EstadoUnidad.CERO_KM

#### Scenario: Conversión desde descripción "0 km"
- **WHEN** se invoca EstadoUnidad.fromString("0 km")
- **THEN** el sistema devuelve EstadoUnidad.CERO_KM

#### Scenario: Conversión desde nombre alternativo
- **WHEN** se invoca EstadoUnidad.fromString("USADO")
- **THEN** el sistema devuelve EstadoUnidad.USADO

#### Scenario: Conversión desde descripción "Usado"
- **WHEN** se invoca EstadoUnidad.fromString("Usado")
- **THEN** el sistema devuelve EstadoUnidad.USADO

#### Scenario: Error en conversión con texto inválido
- **WHEN** se invoca EstadoUnidad.fromString("NUEVO")
- **THEN** el sistema lanza IllegalArgumentException
