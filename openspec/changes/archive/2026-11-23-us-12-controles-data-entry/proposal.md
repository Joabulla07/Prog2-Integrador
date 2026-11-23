# Proposal

## Why

La aplicación aún no tiene una utilidad reutilizable para leer datos desde consola. Sin controles de entrada seguros, cada operación podría intentar convertir texto directamente y fallar ante valores vacíos, formatos inválidos o fuera de rango.

US-12 agrega controles de data-entry con reintentos claros para centralizar la interacción de entrada sin mover las reglas de dominio fuera de la capa de servicio.

## What Changes

- Crear `ValidadorEntrada` en la capa `ui`, con `Scanner(System.in)` y lectura mediante `nextLine()`.
- Agregar lectura obligatoria de texto para campos abiertos.
- Agregar lectura de enteros wrapper con rango mínimo y máximo.
- Agregar lectura de decimales wrapper con límite mínimo.
- Agregar lectura genérica de enums mediante nombres o descripciones aceptadas por el enum.
- Mostrar mensajes claros y repetir la lectura ante entradas vacías, no convertibles o fuera de rango.

## Capabilities

### New Capabilities
- `controles-data-entry`: entrada segura por consola para texto, enteros, decimales y enums con reintento.

### Modified Capabilities
- Ninguna.

## Impact

- Nuevo paquete `src/concesionaria/ui/` con `ValidadorEntrada`.
- Uso de `Scanner`, `nextLine()`, `Integer.valueOf()` y `Double.valueOf()`.
- Integración futura con `ConsolaApp` y `ConcesionariaService`.
- Sin cambios en persistencia, modelo, validación de dominio ni repositorio.
