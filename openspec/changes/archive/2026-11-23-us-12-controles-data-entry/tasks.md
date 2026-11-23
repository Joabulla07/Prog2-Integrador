# Tasks

## 1. Control de entrada

- [x] 1.1 Crear `src/concesionaria/ui/ValidadorEntrada.java` con constructores para `System.in` y `Scanner` recibido, y verificar que el proyecto compila.
- [x] 1.2 Implementar `leerTextoObligatorio(String etiqueta)`, leer con `nextLine()`, normalizar el texto y reintentar ante entradas vacías; verificar con entrada simulada.
- [x] 1.3 Implementar `leerEnteroWrapper(String etiqueta, int min, int max)` con `Integer.valueOf()`, rango inclusivo y reintentos; verificar formato inválido y límites.
- [x] 1.4 Implementar `leerDecimalWrapper(String etiqueta, double min)` con `Double.valueOf()`, mínimo y rechazo de números no finitos; verificar formato inválido y límite.

## 2. Enums genéricos

- [x] 2.1 Implementar `<E extends Enum<E>> E leerEnum(String etiqueta, Class<E> enumClass)` usando nombres y descripciones disponibles, y verificar `TipoVehiculo` y `EstadoUnidad`.
- [x] 2.2 Agregar mensajes de opciones disponibles y reintento ante enum vacío o desconocido; verificar que no termina con excepción ante entrada inválida.

## 3. Pruebas de data-entry

- [x] 3.1 Crear tests con `ByteArrayInputStream` y salida capturada para texto vacío y válido; verificar contenido y reintento.
- [x] 3.2 Crear tests para enteros fuera de rango y no enteros; verificar que solo devuelve valores válidos.
- [x] 3.3 Crear tests para decimales menores al mínimo y no decimales; verificar que solo devuelve valores válidos.
- [x] 3.4 Crear tests para enums válidos e inválidos; verificar coincidencia por nombre y descripción.

## 4. Integración y regresión

- [x] 4.1 Verificar que `ValidadorEntrada` no solicita ni contiene código de inventario y no duplica validaciones de `ConcesionariaService`.
- [x] 4.2 Ejecutar compilación completa y tests existentes; verificar que no hay regresiones fuera de `ui`.
