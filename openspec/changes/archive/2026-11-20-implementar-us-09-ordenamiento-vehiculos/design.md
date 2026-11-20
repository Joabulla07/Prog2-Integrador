# Design

## Context

Las clases `Vehiculo` y sus subclases ya tienen getters, setters, `equals`, `hashCode` y `toString`. El proyecto contiene una interfaz personalizada `concesionaria.interfaces.Comparable`, pero no está implementada ni integrada con las colecciones estándar de Java.

## Goals / Non-Goals

**Goals:**
- Incorporar el contrato estándar `Comparable<Vehiculo>` al dominio.
- Separar el orden natural de criterios alternativos mediante `Comparator<Vehiculo>`.
- Garantizar compatibilidad con `Collections.sort(...)` y `List.sort(...)`.
- Evitar ambigüedades con la interfaz personalizada existente.

**Non-Goals:**
- Modificar persistencia, consola, repositorio o validaciones de negocio.
- Cambiar el comportamiento actual de `equals` y `hashCode`.
- Incorporar ordenamiento en el menú hasta que exista una US específica posterior.

## Decisions

**Orden natural por código de inventario:**
- `Vehiculo` implementará `Comparable<Vehiculo>`.
- `compareTo` comparará exclusivamente el código normalizado con `trim().toUpperCase(Locale.ROOT)`.
- Los códigos nulos se ubicarán al final.
- `equals` y `hashCode` reutilizarán la misma normalización de código basada en `Locale.ROOT`.
- Precio, año y marca/modelo se resolverán solo mediante comparadores externos `Comparator<Vehiculo>`.

**Comparadores externos:**
- Se crearán comparadores independientes para precio, año y marca/modelo.
- Los comparadores manejarán valores nulos sin lanzar `NullPointerException` y ubicarán valores nulos al final.
- Cada criterio podrá usarse por separado con lambdas o clases `Comparator<Vehiculo>`.

**Remoción de la interfaz personalizada:**
- Se eliminará `concesionaria.interfaces.Comparable` si no tiene referencias.
- Esto evita que una clase llamada `Comparable` confunda el uso de `java.lang.Comparable` y `java.util.Comparator`.

**Manejo de tipos cruzados:**
- `compareTo` podrá validar `null`, pero no necesita aceptar tipos de clase distintos por estar tipado con genéricos.

## Risks / Trade-offs

- **Riesgo:** Un criterio natural basado solo en código puede resultar poco útil para reportes comerciales. → Mitigación: ofrecer comparadores externos para precio, año y marca/modelo.
- **Riesgo:** Valores nulos en atributos requieren reglas explícitas. → Mitigación: documentar y centralizar los comparadores con valores nulos al final.
- **Riesgo:** Eliminación de una interfaz pública no utilizada. → Mitigación: verificar referencias antes de eliminar el archivo y conservar compatibilidad si existe código externo.

## Migration Plan

1. Actualizar `Vehiculo` para implementar `Comparable<Vehiculo>`.
2. Eliminar o reemplazar la interfaz personalizada después de verificar referencias.
3. Agregar comparadores y pruebas unitarias o tests ejecutables.
4. Compilar y ejecutar las verificaciones antes de integrar otras historias.

## Open Questions

Ninguna. El orden natural y los criterios alternativos quedaron definidos durante la exploración.
