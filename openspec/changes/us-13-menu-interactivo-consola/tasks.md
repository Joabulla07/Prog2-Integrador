# Tasks

## 1. Fachada de servicio

- [x] 1.1 Agregar `listarTodos()` a `ConcesionariaService` delegando en `VehiculoRepository` y verificar con un test o ejecución que devuelve la lista persistida.
- [x] 1.2 Agregar `listarPorEstado(EstadoUnidad)` y `listarPorTipo(TipoVehiculo)` delegando en el repositorio y verificar ambos filtros.

## 2. Aplicación de consola

- [x] 2.1 Crear `src/concesionaria/ui/ConsolaApp.java` con dependencias por constructor y punto `main`, y verificar que el proyecto compila.
- [x] 2.2 Implementar el menú numerado 1-10 y 0 con bucle estable, opción inválida y salida; verificar con entrada simulada.
- [x] 2.3 Implementar registro de automóvil usando `ValidadorEntrada` sin solicitar código; verificar que construye el objeto y llama al servicio.
- [x] 2.4 Implementar registro de camioneta usando `ValidadorEntrada` sin solicitar código; verificar que construye el objeto y llama al servicio.
- [x] 2.5 Implementar registro de motocicleta usando `ValidadorEntrada` sin solicitar código; verificar que construye el objeto y llama al servicio.

## 3. Consultas y reporte

- [x] 3.1 Implementar listado completo y verificable de vehículos, incluyendo mensaje para lista vacía.
- [x] 3.2 Implementar filtros por 0 km, usado y TipoVehiculo, verificando que usan el servicio y muestran resultados claros.
- [x] 3.3 Implementar búsqueda por código y eliminación por código, verificando éxito y excepciones no encontradas.
- [x] 3.4 Implementar opción de reporte directivo y verificar que muestra totales y fichas.

## 4. Errores y integración

- [x] 4.1 Capturar `DatoVehiculoInvalidoException`, `VehiculoNoEncontradoException` y `PersistenciaException` por operación sin finalizar el menú.
- [x] 4.2 Reemplazar `concesionaria.Main` por el arranque de `ConsolaApp` y verificar que la aplicación inicia.
- [x] 4.3 Agregar tests de menú con entrada simulada y servicios/repositorios simulados, verificando opciones básicas y reintentos.
- [x] 4.4 Ejecutar compilación completa y tests existentes; confirmar que MSSQL, validaciones y registros siguen funcionando sin regresiones.
