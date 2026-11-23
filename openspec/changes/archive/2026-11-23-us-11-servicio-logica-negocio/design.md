# Design

## Context

El repositorio de vehículos ya expone persistencia CRUD y MSSQL asigna el código de inventario. El modelo contiene datos y comportamiento comercial, pero no existe una clase de negocio que coordine validación y reportes. La nueva clase debe apoyarse en `VehiculoRepository` sin introducir JDBC ni reglas SQL.

## Goals / Non-Goals

**Goals:**
- Centralizar validaciones y operaciones de negocio en `ConcesionariaService`.
- Mantener la persistencia detrás de `VehiculoRepository`.
- Producir totales por tipo y estado más fichas directivas con código persistido.
- Mantener excepciones de dominio y persistencia diferenciadas.

**Non-Goals:**
- Cambiar el esquema o las consultas MSSQL existentes.
- Mover validaciones o generación de código al modelo `Vehiculo`.
- Incorporar una interfaz de consola o capa web.
- Añadir caché, concurrencia o almacenamiento alternativo.

## Decisions

- `ConcesionariaService` recibirá `VehiculoRepository` por constructor y conservará una referencia final.
  - Alternativa descartada: instanciación interna del repositorio, que ocultaría dependencias y dificultaría pruebas.

- `registrarVehiculo(Vehiculo v)` ejecutará una única secuencia de validación antes de `guardar(v)`.
  - Validará marca, modelo, color, tipo concreto y campos específicos; tipo y estado; año; precio; kilometraje; coherencia estado-kilometraje; y ausencia de código previo.
  - Alternativa descartada: usar métodos de validación en `Vehiculo`, porque distribuiría reglas de negocio en el modelo.

- Las operaciones `obtenerVehiculoPorCodigo` y `eliminarVehiculo` comprobarán el resultado del repositorio y lanzarán `VehiculoNoEncontradoException` cuando falte el vehículo.
  - Alternativa descartada: devolver `null`, porque perdería el contexto del código buscado y rompería el contrato del dominio.

- `generarReporteDirectivos()` devolverá `String` construido con `listarTodos()`; los totales se calcularán por `TipoVehiculo` y `EstadoUnidad`, y cada ficha se obtendrá mediante `obtenerFichaDirectiva()`.
  - Alternativa descartada: almacenar fichas o totales en el servicio, porque serían estado derivado que podría quedar desactualizado.

- Las fichas directivas deberán incluir `codigoInventario`; esto exige completar las implementaciones existentes de `obtenerFichaDirectiva()` sin modificar el repositorio.

- La excepción `PersistenciaException` seguirá propagándose desde el repositorio sin envolverla nuevamente en el servicio.

## Risks / Trade-offs

- [Fichas sin código en vehículos recuperados] → Verificar que el mapeo MSSQL asigna `codigoInventario` antes de construir el reporte.
- [Reglas duplicadas con versiones anteriores] → Mantener la validación solo en `ConcesionariaService` y retirar validaciones de negocio de consola/modelo.
- [Reporte con formato ambiguo] → Definir en los tests una estructura textual estable con encabezados de totales y una ficha por línea.
- [Excepciones checked] → Mantener las firmas coherentes con `DatoVehiculoInvalidoException` y `VehiculoNoEncontradoException`.

## Migration Plan

1. Agregar `ConcesionariaService` sin modificar los contratos existentes del repositorio.
2. Incorporar los tests de validación, registro, búsquedas, eliminación y reporte.
3. Actualizar las fichas para incluir código generado.
4. Ejecutar compilación y tests completos.

Rollback: eliminar la clase y sus tests no requiere cambios en tablas, repositorio o modelos para revertir.
