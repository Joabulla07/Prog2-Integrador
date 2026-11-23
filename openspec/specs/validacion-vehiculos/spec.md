# validacion-vehiculos Specification

## Purpose

Proporciona excepciones específicas del dominio para validar datos de vehículos, manejar búsquedas fallidas y errores de persistencia con contexto adicional.

## Requirements

### Requirement: Validación de datos de vehículo
ConcesionariaService SHALL validar los datos de un vehículo antes de delegar su persistencia al repositorio. La validación SHALL incluir campos obligatorios no vacíos, TipoVehiculo y EstadoUnidad no nulos, año entre 1950 y el año actual más uno, precioLista mayor a cero y coherencia entre estado y kilometraje. El sistema SHALL lanzar DatoVehiculoInvalidoException con el campo y valor inválido en el contexto. La consola y Vehiculo SHALL NO contener reglas de validación de dominio.

#### Scenario: Año fuera de rango
- **WHEN** se valida un vehículo con año menor a 1950 o mayor al año actual + 1
- **THEN** se lanza DatoVehiculoInvalidoException con campo="anio" y el valor inválido

#### Scenario: Kilometraje negativo
- **WHEN** se valida un vehículo con kilometraje negativo
- **THEN** se lanza DatoVehiculoInvalidoException con campo="kilometraje" y el valor inválido

#### Scenario: Incoherencia estado-kilometraje 0km
- **WHEN** se valida un vehículo con estado CERO_KM y kilometraje mayor a 0
- **THEN** se lanza DatoVehiculoInvalidoException indicando la incoherencia

#### Scenario: Incoherencia estado-kilometraje usado
- **WHEN** se valida un vehículo con estado USADO y kilometraje menor o igual a 0
- **THEN** se lanza DatoVehiculoInvalidoException indicando la incoherencia

#### Scenario: Campo obligatorio vacío
- **WHEN** se valida un vehículo con marca, modelo, color, carrocería o tipoMotocicleta vacío
- **THEN** se lanza DatoVehiculoInvalidoException con el campo faltante

#### Scenario: Validación fuera del modelo y la consola
- **WHEN** se registra o modifica un vehículo
- **THEN** la decisión de validez se ejecuta en ConcesionariaService y no en Vehiculo ni en la consola

### Requirement: Búsqueda de vehículo no encontrado
El sistema SHALL lanzar VehiculoNoEncontradoException cuando no se encuentre un vehículo por su código de inventario, incluyendo el código buscado en el contexto.

#### Scenario: Código inexistente en búsqueda
- **WHEN** se busca un vehículo por código que no existe en el repositorio
- **THEN** se lanza VehiculoNoEncontradoException con codigoBuscado

#### Scenario: Código inexistente en eliminación
- **WHEN** se intenta eliminar un vehículo por código que no existe
- **THEN** se lanza VehiculoNoEncontradoException con codigoBuscado

### Requirement: Errores de persistencia
El sistema SHALL lanzar PersistenciaException cuando falle una operación de base de datos con Sql2o/MSSQL, preservando la causa original y la operación que falló.

#### Scenario: Error de conexión
- **WHEN** falla la conexión a MSSQL durante una operación
- **THEN** se lanza PersistenciaException con la causa Throwable y la operación

#### Scenario: Error en consulta SQL
- **WHEN** falla una consulta parametrizada con Sql2o
- **THEN** se lanza PersistenciaException con la causa Sql2oException y la operación

#### Scenario: Error en transacción
- **WHEN** falla una transacción de guardado o eliminación
- **THEN** se lanza PersistenciaException con la causa y la operación
