# Proposal

## Why

El TFI requiere tratamiento de excepciones propias del dominio y persistencia. Las excepciones estándar de Java no proveen contexto específico para errores de validación de vehículos, búsquedas fallidas o fallos de base de datos.

## What Changes

- Crear paquete `concesionaria.exception` con tres clases de excepción:
  - `DatoVehiculoInvalidoException extends Exception`: validaciones de dominio (año, kilometraje, estado, campos obligatorios)
  - `VehiculoNoEncontradoException extends Exception`: búsquedas por código de inventario fallidas
  - `PersistenciaException extends RuntimeException`: fallos de Sql2o/MSSQL
- Cada excepción incluye campos adicionales para contexto (campo inválido, valor, código buscado, operación, causa)
- JavaDoc completo en constructores y clases

## Capabilities

### New Capabilities
- `validacion-vehiculos`: manejo de excepciones del dominio para validación de datos, búsquedas y persistencia de vehículos

### Modified Capabilities
- Ninguna

## Impact

- Paquete `exception/` nuevo en `src/concesionaria/`
- Excepciones usadas en US-07 (Vehiculo), US-09 (MssqlVehiculoRepository), US-10 (ConcesionariaService)
- Sin cambios en dependencias externas
