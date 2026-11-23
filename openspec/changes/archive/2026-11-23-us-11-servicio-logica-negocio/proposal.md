# Proposal

## Why

La lógica de dominio todavía no tiene una capa de servicio que centralice las reglas de registro, consulta y eliminación de vehículos. Esto obliga a depender de la consola o del modelo para validar datos y no ofrece un reporte directivo único construido desde el repositorio persistido.

US-11 introduce `ConcesionariaService` para separar validaciones y operaciones de negocio de la interfaz, el modelo y la persistencia.

## What Changes

- Crear `ConcesionariaService` con `VehiculoRepository` inyectado por constructor.
- Validar todos los datos obligatorios, tipos, año, precio, kilometraje y coherencia entre estado y kilometraje antes de persistir.
- Impedir el registro de vehículos que ya tengan código de inventario.
- Lanzar `VehiculoNoEncontradoException` al buscar o eliminar un código inexistente.
- Generar un reporte directivo con totales por tipo y estado, además de fichas de cada vehículo persistido con código generado.

## Capabilities

### New Capabilities
- `logica-negocio-concesionaria`: operaciones de registro, búsqueda, eliminación y reporte directivo centralizadas en el servicio de negocio.

### Modified Capabilities
- `validacion-vehiculos`: la validación de dominio debe ser propiedad de `ConcesionariaService`; el modelo debe no contener reglas de negocio y la consola no debe validar vehículos.

## Impact

- Nuevo paquete `src/concesionaria/servicio/` con `ConcesionariaService`.
- Uso de `VehiculoRepository` y sus operaciones CRUD existentes.
- Uso de `DatoVehiculoInvalidoException` y `VehiculoNoEncontradoException`.
- Agregado de fichas directivas a los datos reportados sin cambiar la persistencia MSSQL.
- Tests de servicio y reportes; no cambia el contrato SQL existente.
