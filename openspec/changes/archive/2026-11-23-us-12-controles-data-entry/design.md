# Design

## Context

US-12 agrega una utilidad de entrada segura antes de construir el menú interactivo de US-13. Actualmente `Main` no usa consola y no existe ninguna clase `ui`. Los métodos pueden reutilizar `TipoVehiculo.fromString()` y `EstadoUnidad.fromString()` para enums existentes.

## Goals / Non-Goals

**Goals:**
- Centralizar lecturas por consola con reintentos controlados.
- Convertir texto a wrappers mediante `Integer.valueOf()` y `Double.valueOf()`.
- Soportar enums arbitrarios dentro de límites de tipos Java.
- Permitir pruebas con entrada simulada sin acoplarlas a `System.in`.

**Non-Goals:**
- Implementar el menú de US-13.
- Añadir validaciones de dominio a los controles de entrada.
- Solicitar o asignar código de inventario desde consola.
- Cambiar persistencia, repositorio o reglas de `ConcesionariaService`.

## Decisions

- `ValidadorEntrada` tendrá una instancia de `Scanner` y expondrá el constructor por defecto `ValidadorEntrada()` con `Scanner(System.in)`.
  - Se agregará también un constructor `ValidadorEntrada(Scanner scanner)` para pruebas y reutilización con otros flujos.
  - Alternativa descartada: leer directamente `System.in` dentro de cada método, que impediría probar entradas múltiples de forma aislada.

- Todos los métodos leerán una sola línea mediante `nextLine()` y normalizarán texto con `trim()` antes de validar.
  - Alternativa descartada: usar `nextInt()` o `nextDouble()`, porque dejan caracteres pendientes y rompen lecturas posteriores.

- `leerEnteroWrapper` validará conversión y luego rango inclusivo `[min, max]`.
- `leerDecimalWrapper` validará conversión y luego `valor >= min`.
- Ambos devolverán wrappers `Integer` y `Double`, no tipos primitivos.

- `leerEnum` recorrerá `enumClass.getEnumConstants()` y aceptará coincidencia case-insensitive con el nombre del enum.
  - Cuando el enum exponga `getDescripcion()`, la entrada también podrá coincidir con esa descripción.
  - Alternativa descartada: resolver solo por `name()`, porque excluiría las formas legibles ya aceptadas por los enums del dominio.

- Los errores se imprimirán por consola con etiqueta y motivo; el método seguirá en bucle hasta obtener una entrada válida.
- Un cierre o fin de entrada no válido se tratará sin excepciones inesperadas: se reutilizará el bucle mientras el scanner tenga entrada disponible.

## Risks / Trade-offs

- [Scanner compartido puede consumir entradas] → Mantener una sola instancia y evitar mezclar `nextLine()` con métodos `next*()`.
- [Mensaje enum genérico sin descripción] → Mostrar los nombres disponibles y, si existe `getDescripcion()`, mostrar ambas formas.
- [Métodos infinitos ante una entrada siempre inválida] → Mantener reintento como requerimiento; las pruebas deben proporcionar secuencias finitas.
- [NaN o infinito como decimal] → Rechazar valores no finitos además del mínimo, para evitar números inválidos de entrada.

## Migration Plan

1. Crear `src/concesionaria/ui/ValidadorEntrada.java` sin cambiar `Main` ni otros módulos.
2. Agregar tests con `Scanner` sobre `ByteArrayInputStream` y salida capturada.
3. Integrar esta utilidad con US-13 cuando se implemente el menú.

Rollback: eliminar `ValidadorEntrada` y sus tests no afecta clases existentes ni el flujo actual.

## Open Questions

- No hay decisiones pendientes que cambien el alcance US-12. El formato exacto de cada mensaje puede mantenerse claro y uniforme sin afectar contratos de entrada.
