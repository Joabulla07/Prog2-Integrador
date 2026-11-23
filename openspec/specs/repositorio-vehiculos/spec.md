# repositorio-vehiculos Specification

## Purpose

Define la arquitectura de repositorio genérico con persistencia MSSQL mediante Sql2o, indexación en memoria con colecciones tipadas y operaciones CRUD específicas para vehículos.

## Requirements

### Requirement: Interfaz genérica de repositorio CRUD
El sistema SHALL proporcionar una interfaz genérica parametrizada `RepositorioGenerico<T extends Identificable, ID>` que defina el contrato CRUD básico para entidades identificables.

#### Scenario: Guardar entidad
- **WHEN** se invoca guardar(T entidad) en un repositorio genérico
- **THEN** la entidad se persiste y se devuelve con su identificador asignado

#### Scenario: Buscar por identificador
- **WHEN** se invoca buscarPorCodigo(ID codigo) con un identificador existente
- **THEN** se devuelve la entidad correspondiente o null si no existe

#### Scenario: Listar todas las entidades
- **WHEN** se invoca listarTodos() en un repositorio genérico
- **THEN** se devuelve una colección con todas las entidades persistidas

#### Scenario: Eliminar por identificador
- **WHEN** se invoca eliminarPorCodigo(ID codigo) con un identificador existente
- **THEN** la entidad se elimina de la persistencia

### Requirement: Índice genérico con Map tipado
El sistema SHALL proporcionar una clase genérica `IndiceGenerico<K, V extends Identificable>` respaldada internamente por un Map<K, V> para indexar entidades en memoria con tipado seguro.

#### Scenario: Agregar entidad al índice
- **WHEN** se invoca agregar(K clave, V valor) en IndiceGenerico
- **THEN** la entidad se almacena en el Map interno con la clave especificada

#### Scenario: Buscar entidad por clave
- **WHEN** se invoca buscar(K clave) en IndiceGenerico
- **THEN** se devuelve la entidad asociada a la clave o null si no existe

#### Scenario: Verificar existencia de clave
- **WHEN** se invoca contiene(K clave) en IndiceGenerico
- **THEN** se devuelve true si la clave existe en el índice, false en caso contrario

#### Scenario: Eliminar entidad del índice
- **WHEN** se invoca eliminar(K clave) en IndiceGenerico
- **THEN** la entidad se elimina del Map interno

### Requirement: Repositorio específico de vehículos
El sistema SHALL proporcionar una interfaz `VehiculoRepository extends RepositorioGenerico<Vehiculo, String>` que herede las operaciones CRUD genéricas y declare métodos específicos del dominio.

#### Scenario: Listar vehículos por estado
- **WHEN** se invoca listarPorEstado(EstadoUnidad estado) en VehiculoRepository
- **THEN** se devuelve una lista de vehículos filtrados por el estado especificado

#### Scenario: Listar vehículos por tipo
- **WHEN** se invoca listarPorTipo(TipoVehiculo tipo) en VehiculoRepository
- **THEN** se devuelve una lista de vehículos filtrados por el tipo especificado

### Requirement: Persistencia MSSQL con Sql2o
El sistema SHALL proporcionar una clase `MssqlVehiculoRepository implements VehiculoRepository` que use Sql2o para persistir vehículos en MSSQL, excluyendo codigo_inventario del INSERT y permitiendo que MSSQL genere el identificador.

#### Scenario: Alta de vehículo sin código
- **WHEN** se invoca guardar(Vehiculo vehiculo) con un vehículo sin codigoInventario
- **THEN** se ejecuta INSERT sin codigo_inventario, MSSQL genera el código calculado y se asigna al objeto

#### Scenario: Recuperación de código generado
- **WHEN** se completa el INSERT de un vehículo
- **THEN** se recupera codigo_inventario mediante OUTPUT INSERTED.codigo_inventario y se llama a vehiculo.asignarCodigoInventario() una única vez

#### Scenario: Mapeo polimórfico desde tipo_vehiculo
- **WHEN** se lee un registro desde MSSQL con tipo_vehiculo = 'AUTOMOVIL'
- **THEN** se instancia un objeto Automovil con los datos del registro

#### Scenario: Mapeo polimórfico para camioneta
- **WHEN** se lee un registro desde MSSQL con tipo_vehiculo = 'CAMIONETA'
- **THEN** se instancia un objeto Camioneta con los datos del registro

#### Scenario: Mapeo polimórfico para motocicleta
- **WHEN** se lee un registro desde MSSQL con tipo_vehiculo = 'MOTOCICLETA'
- **THEN** se instancia un objeto Motocicleta con los datos del registro

#### Scenario: Traducción de errores Sql2o
- **WHEN** ocurre un error Sql2oException durante una operación de persistencia
- **THEN** se lanza PersistenciaException preservando la causa original

### Requirement: Asignación única de código de inventario
El sistema SHALL garantizar que el código de inventario generado por MSSQL se asigne al objeto vehículo una única vez mediante el método asignarCodigoInventario().

#### Scenario: Asignación exitosa después de persistir
- **WHEN** se recupera codigo_inventario de MSSQL después del INSERT
- **THEN** se invoca vehiculo.asignarCodigoInventario(codigo) y el código queda asignado

#### Scenario: Rechazo de reasignación
- **WHEN** se intenta asignar codigoInventario a un vehículo que ya tiene código
- **THEN** el método asignarCodigoInventario lanza IllegalStateException

### Requirement: Consultas parametrizadas con Sql2o
El sistema SHALL usar consultas parametrizadas de Sql2o para todas las operaciones de base de datos, previniendo inyección SQL.

#### Scenario: Consulta con parámetros
- **WHEN** se ejecuta una consulta con filtros (estado, tipo)
- **THEN** se usa Sql2o con parámetros :estado, :tipo en lugar de concatenar strings

### Requirement: Conversión de enums desde strings
El sistema SHALL convertir los valores de tipo_vehiculo y estado_unidad desde strings de MSSQL a enums Java usando los métodos fromString() de TipoVehiculo y EstadoUnidad.

#### Scenario: Conversión de tipo_vehiculo
- **WHEN** se lee 'AUTOMOVIL' desde la columna tipo_vehiculo
- **THEN** se convierte a TipoVehiculo.AUTOMOVIL mediante TipoVehiculo.fromString()

#### Scenario: Conversión de estado_unidad
- **WHEN** se lee 'CERO_KM' desde la columna estado_unidad
- **THEN** se convierte a EstadoUnidad.CERO_KM mediante EstadoUnidad.fromString()
