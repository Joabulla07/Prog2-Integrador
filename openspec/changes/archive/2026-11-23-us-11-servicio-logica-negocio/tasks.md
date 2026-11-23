# Tasks

## 1. Servicio y dependencia

- [x] 1.1 Crear `src/concesionaria/servicio/ConcesionariaService.java` con `VehiculoRepository` inyectado por constructor y verificar que el proyecto compila.
- [x] 1.2 Implementar `registrarVehiculo(Vehiculo v)` con validación completa antes de delegar en `guardar(v)`, y verificar con un test que rechaza objetos inválidos sin llamar al repositorio.
- [x] 1.3 Implementar el rechazo de registros que ya tengan `codigoInventario` y verificar que el repositorio no recibe el alta.

## 2. Validación de dominio

- [x] 2.1 Validar marca, modelo, color, carrocería en automóvil/camioneta y tipoMotocicleta en motocicleta, verificando que cada dato vacío lance `DatoVehiculoInvalidoException`.
- [x] 2.2 Validar TipoVehiculo, EstadoUnidad, año, precioLista y kilometraje según US-11, verificando cada límite y coherencia con tests.

## 3. Consultas y eliminación

- [x] 3.1 Implementar `obtenerVehiculoPorCodigo` y verificar que devuelve el vehículo existente o lanza `VehiculoNoEncontradoException` con el código buscado.
- [x] 3.2 Implementar `eliminarVehiculo` y verificar la eliminación existente y la excepción para códigos inexistentes.

## 4. Reportes directivos

- [x] 4.1 Implementar `generarReporteDirectivos()` con totales por `TipoVehiculo` y `EstadoUnidad`, verificando los tres tipos y los dos estados.
- [x] 4.2 Incorporar una ficha por vehículo mediante `obtenerFichaDirectiva()` y verificar que los reportes con vehículos incluyen el `codigoInventario` generado.
- [x] 4.3 Verificar el reporte vacío con los totales en cero y sin fichas.

## 5. Integración y regresión

- [x] 5.1 Añadir tests de `ConcesionariaService` con repositorio en memoria o mock y verificar todos los escenarios de US-11.
- [x] 5.2 Ejecutar los tests existentes y confirmar que no se introducen reglas de validación en consola o `Vehiculo`.
