# Proposal

## Why

El sistema actual tiene el modelo de vehículos y la base de datos MSSQL configurada, pero falta la capa de repositorio que implemente persistencia con Sql2o, genéricos parametrizados y colecciones en memoria. US-10 requiere esta capa para completar la arquitectura de datos del TFI.

## What Changes

- Crear interfaz genérica `RepositorioGenerico<T extends Identificable, ID>` con contrato CRUD parametrizado
- Crear clase genérica `IndiceGenerico<K, V extends Identificable>` como utilidad de Map tipado para indexación en memoria
- Crear interfaz `VehiculoRepository extends RepositorioGenerico<Vehiculo, String>` con métodos específicos del dominio
- Crear clase `MssqlVehiculoRepository implements VehiculoRepository` usando Sql2o para persistencia en MSSQL
- Implementar generación de `codigo_inventario` por MSSQL (columna calculada PERSISTED) con OUTPUT INSERTED
- Implementar mapeo polimórfico desde `tipo_vehiculo` a instancias de Automovil/Camioneta/Motocicleta
- Traducir errores de Sql2o a `PersistenciaException`

## Capabilities

### New Capabilities
- `repositorio-vehiculos`: Define la arquitectura de repositorio genérico, persistencia MSSQL con Sql2o, indexación en memoria y operaciones CRUD específicas para vehículos

### Modified Capabilities
- Ninguna (es una nueva capa, no modifica comportamiento existente)

## Impact

- **Paquete**: `src/concesionaria/repositorio/` (actualmente vacío)
- **Dependencias**: Sql2o ya configurado en `ConexionDB`, MSSQL con tabla Vehiculos ya creada
- **Integración**: `Vehiculo.asignarCodigoInventario()` ya implementado, será usado por el repositorio
- **Excepciones**: `PersistenciaException` ya existe en `exception/`
