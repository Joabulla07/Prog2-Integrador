# Proposal

## Why

La aplicación tiene persistencia, servicio de negocio y controles de entrada, pero `Main` sigue mostrando una salida fija. Falta una interfaz de consola que conecte esas capas y permita operar la concesionaria sin usar código directamente.

US-13 agrega `ConsolaApp` como aplicación interactiva de consola, con todas las operaciones indicadas por el TP y mensajes claros ante errores de dominio o persistencia.

## What Changes

- Crear `ConsolaApp` con `main` y menú numerado en bucle.
- Registrar automóvil, camioneta y motocicleta sin solicitar código de inventario.
- Listar todos los vehículos y filtrar por estado o tipo.
- Buscar y eliminar vehículos por código de inventario.
- Mostrar el reporte directivo generado por el servicio.
- Capturar `DatoVehiculoInvalidoException`, `VehiculoNoEncontradoException` y `PersistenciaException` sin terminar el menú.
- Reutilizar `ValidadorEntrada` para todas las entradas de consola.
- Exponer desde `ConcesionariaService` las consultas necesarias para listar y filtrar vehículos.

## Capabilities

### New Capabilities
- `menu-interactivo-consola`: menú de consola que permite registrar, consultar, filtrar, eliminar y reportar vehículos contra el servicio de negocio.

### Modified Capabilities
- Ninguna.

## Impact

- Nuevo paquete `src/concesionaria/ui/` con `ConsolaApp` o archivo equivalente.
- Uso de `ConcesionariaService`, `ValidadorEntrada`, repositorio MSSQL y excepciones existentes.
- Posible ampliación de `ConcesionariaService` con métodos de lectura por estado y tipo si no existen.
- Reemplazo del comportamiento fijo de `concesionaria.Main` por el arranque de la aplicación interactiva.
