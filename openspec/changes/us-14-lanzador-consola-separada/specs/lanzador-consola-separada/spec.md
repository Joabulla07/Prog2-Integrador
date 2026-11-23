# Spec Delta

## Purpose

Abre la aplicación en una consola separada y proporciona un flujo visual limpio para cada pantalla del menú.

## ADDED Requirements

### Requirement: Lanzador de consola separada
El sistema SHALL proporcionar mecanismos de ejecución que abran `ConsolaApp` en una ventana separada después de compilar los fuentes.

#### Scenario: Ejecutar desde Windows
- **WHEN** el usuario ejecuta `ejecutar.bat` en Windows
- **THEN** se configura UTF-8 y se abre una nueva ventana con el título Concesionaria Multimarcas - TFI Programacion II

#### Scenario: Ejecutar desde scripts
- **WHEN** el usuario ejecuta `run.ps1` o `run.sh`
- **THEN** los scripts compilan y/o inician el launcher después de levantar MSSQL

#### Scenario: Continuar desde IDE o consola normal
- **WHEN** el usuario ejecuta `Main` o `ConsolaApp` sin launcher
- **THEN** la aplicación sigue mostrando el menú en la consola actual

### Requirement: Limpieza de pantalla por acción
El sistema SHALL limpiar la consola al seleccionar una operación y al volver al menú principal.

#### Scenario: Entrada a una operación
- **WHEN** el usuario selecciona una opción del menú principal
- **THEN** se limpia la consola y queda visible únicamente el encabezado y controles de esa operación

#### Scenario: Resultado de operación
- **WHEN** una operación finaliza correctamente o con un error capturado
- **THEN** el resultado ocupa una pantalla limpia y muestra una opción explícita para volver

### Requirement: Retorno al menú principal
El sistema SHALL ofrecer una acción de regreso disponible desde cada pantalla de operación.

#### Scenario: Volver desde una operación
- **WHEN** el usuario elige volver desde una pantalla secundaria
- **THEN** se limpia la consola y se vuelve a mostrar el menú principal

#### Scenario: Finalizar operación repetida
- **WHEN** el usuario desea realizar otra acción después de completar una operación
- **THEN** puede volver al menú sin cerrar la aplicación
