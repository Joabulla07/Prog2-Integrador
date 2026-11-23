# Spec Delta

## Purpose

Centraliza las operaciones de negocio de vehículos y genera información ejecutiva confiable desde los datos persistidos por el repositorio.

## ADDED Requirements

### Requirement: Registro de vehículos
El sistema SHALL permitir registrar vehículos únicamente después de validar sus datos obligatorios y reglas de dominio, sin aceptar un código de inventario ingresado por el usuario.

#### Scenario: Registro válido
- **WHEN** se registra un vehículo nuevo sin código de inventario y todos sus datos cumplen las reglas de dominio
- **THEN** el servicio delega el alta al repositorio y devuelve el vehículo con el código asignado por MSSQL

#### Scenario: Vehículo con código previo
- **WHEN** se intenta registrar un vehículo que ya posee codigoInventario
- **THEN** el sistema rechaza el registro y no delega la operación al repositorio

#### Scenario: Datos obligatorios incompletos
- **WHEN** se registra un vehículo con marca, modelo, color o una estructura específica vacía
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

### Requirement: Validación de atributos y reglas comerciales
El sistema SHALL validar el tipo de vehículo, estado, año, precio y kilometraje antes de persistir un vehículo.

#### Scenario: Año fuera de rango
- **WHEN** el año es menor a 1950 o mayor al año actual más uno
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

#### Scenario: Tipo o estado nulo
- **WHEN** TipoVehiculo o EstadoUnidad es nulo
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

#### Scenario: Precio no positivo
- **WHEN** precioLista es nulo o menor o igual a cero
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

#### Scenario: Estado CERO_KM consistente
- **WHEN** el estado es CERO_KM y kilometraje no es cero
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

#### Scenario: Estado USADO consistente
- **WHEN** el estado es USADO y kilometraje es nulo o menor o igual a cero
- **THEN** el sistema rechaza el registro con DatoVehiculoInvalidoException

### Requirement: Consulta y eliminación de vehículos
El sistema SHALL convertir códigos inexistentes en errores específicos de la capa de negocio.

#### Scenario: Consulta existente
- **WHEN** se solicita un vehículo por un código que existe en el repositorio
- **THEN** el servicio devuelve el vehículo persistido

#### Scenario: Consulta inexistente
- **WHEN** se solicita un vehículo por un código que no existe
- **THEN** el servicio lanza VehiculoNoEncontradoException con el código buscado

#### Scenario: Eliminación inexistente
- **WHEN** se elimina un código que no existe
- **THEN** el servicio lanza VehiculoNoEncontradoException con el código buscado

#### Scenario: Eliminación existente
- **WHEN** se elimina un código que existe
- **THEN** el repositorio elimina el vehículo y el servicio confirma la operación

### Requirement: Reporte directivo de vehículos
El sistema SHALL construir un reporte ejecutivo desde los vehículos persistidos con totales por tipo, totales por estado y fichas directivas individuales.

#### Scenario: Reporte con vehículos
- **WHEN** se genera el reporte directivo
- **THEN** se incluyen los totales de AUTOMOVIL, CAMIONETA y MOTOCICLETA, los totales de unidades CERO_KM y USADO, y la ficha de cada vehículo

#### Scenario: Ficha con código generado
- **WHEN** un vehículo persistido participa del reporte
- **THEN** su ficha directiva incluye el codigoInventario asignado por MSSQL

#### Scenario: Reporte sin vehículos
- **WHEN** no existen vehículos persistidos
- **THEN** el reporte contiene todos los totales en cero y una colección vacía de fichas
