# Proposal

## Why

US-13 ofrece el menú en la consola donde se ejecuta el proyecto, pero el TP requiere una experiencia separada y limpia. La aplicación necesita una entrada launcher que abra una ventana nueva y permita navegar sin menús o resultados acumulados.

US-14 agrega lanzamiento en una consola separada, limpieza de pantalla y regreso explícito al menú principal.

## What Changes

- Crear `ejecutar.bat` en la raíz para Windows con UTF-8 y una consola separada.
- Agregar `LauncherConsola` como opción Java opcional para abrir la consola con `ProcessBuilder`.
- Limpiar la consola antes de cada acción y antes de volver al menú principal.
- Agregar una opción visible para volver al menú desde cada pantalla de operación.
- Integrar el launcher en `run.ps1` y `run.sh` después de levantar MSSQL.
- Mantener `Main` como punto de entrada normal y evitar que `ConsolaApp` tenga otro `main`.

## Capabilities

### New Capabilities
- `lanzador-consola-separada`: abre una ventana separada y limpia las pantallas de operaciones y menú.

### Modified Capabilities
- `menu-interactivo-consola`: cada operación debe limpiar la pantalla y ofrecer regreso explícito al menú principal.

## Impact

- Nuevos archivos raíz: `ejecutar.bat` y posible `LauncherConsola.java` en `ui`.
- Cambio en `ConsolaApp` para limpiar pantalla y controlar retorno.
- Cambio en `run.ps1` y `run.sh` para compilación y lanzamiento de la aplicación.
- Sin cambios en persistencia, modelo, servicio o validaciones de dominio.
