# Design

## Context

US-13 reutiliza `ValidadorEntrada`, `ConcesionariaService` y el repositorio MSSQL existentes. `Main` todavía es una clase fija sin menú. El servicio posee registro, búsqueda, eliminación y reporte, pero no expone todavía listados por estado o tipo.

## Goals / Non-Goals

**Goals:**
- Reemplazar la ejecución fija por un menú interactivo estable.
- Conectar todas las operaciones al repositorio mediante `ConcesionariaService`.
- Reutilizar entradas validadas sin solicitar código de inventario.
- Mantener la aplicación activa ante errores de dominio y persistencia.

**Non-Goals:**
- Crear una interfaz gráfica o API externa.
- Cambiar validaciones de dominio existentes.
- Cambiar el esquema MSSQL.
- Implementar funciones de exportación, autenticación o concurrencia.

## Decisions

- `ConsolaApp` recibirá `ConcesionariaService` y `ValidadorEntrada` por constructor para facilitar pruebas.
  - `main` creará MSSQL repository, service, input validator y app.
  - Alternativa descartada: instanciar dependencias dentro de cada opción, que repetiría conexiones y dificultaría tests.

- El menú usará un bucle `do-while` con opción 0 como salida y opciones de 1 a 10 según el TP.
  - Cada opción llamará a un método privado específico.
  - Entradas inválidas se resolverán mediante `ValidadorEntrada`; opciones numéricas no válidas volverán al bucle.

- `ConcesionariaService` agregará métodos `listarTodos()`, `listarPorEstado(EstadoUnidad)` y `listarPorTipo(TipoVehiculo)` delegando al repositorio.
  - Alternativa descartada: leer directamente `VehiculoRepository` desde `ConsolaApp`, porque rompería la separación de acceso usada por US-11.

- Las altas construirán subclases y delegarán a `registrarVehiculo`; ningún flujo pedirá código de inventario.
- Los errores se capturarán en puntos de operación: `DatoVehiculoInvalidoException`, `VehiculoNoEncontradoException` y `PersistenciaException`; el bucle continuará después de mostrar el mensaje.
- Las listas se imprimirán línea por línea usando `toString()` o las fichas directivas cuando corresponda.

## Risks / Trade-offs

- [Bucle infinito con scanner sin entrada] → Exponer constructor con `ValidadorEntrada` y tests con entrada finita.
- [Errores runtime no especificados] → Separar capturas conocidas; no envolver errores ajenos sin requisito.
- [Mostrar listas vacías ambiguamente] → Mostrar explícitamente "No hay vehículos registrados" o "No se encontraron vehículos".
- [Altas repetidas con mismo estado] → Cada registro crea una nueva instancia sin código previo y el servicio asigna el código persistido.

## Migration Plan

1. Agregar métodos de listado a `ConcesionariaService`.
2. Implementar `ConsolaApp` y reemplazar el comportamiento fijo de `Main`.
3. Agregar tests de menú con dependencias simuladas y entrada simulada.
4. Compilar y ejecutar los tests existentes sin cambiar MSSQL ni scripts.

Rollback: restaurar `Main` fijo y retirar `ConsolaApp` no requiere cambios de base de datos.
