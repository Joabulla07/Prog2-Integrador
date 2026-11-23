# Spec Delta

## MODIFIED Requirements

### Requirement: Menú interactivo numerado
El sistema SHALL presentar un menú numerado con las opciones de registro, consulta, eliminación, reporte y salida. Antes de mostrar una operación SHALL limpiar la consola. Después de completar o fallar una operación SHALL mantener el resultado en una pantalla limpia con una opción explícita de volver. La selección de volver SHALL limpiar la consola y volver a mostrar el menú principal.

#### Scenario: Selección válida
- **WHEN** el usuario elige una opción existente del menú
- **THEN** el sistema limpia la consola, ejecuta la operación correspondiente y ofrece volver

#### Scenario: Opción inválida
- **WHEN** el usuario elige una opción inexistente o texto no entero
- **THEN** el sistema limpia o mantiene una pantalla clara, muestra un mensaje claro y vuelve a solicitar una opción

#### Scenario: Salida
- **WHEN** el usuario elige la opción 0
- **THEN** el sistema termina el bucle y finaliza la aplicación

#### Scenario: Volver al menú principal
- **WHEN** el usuario completa una operación y elige volver
- **THEN** el sistema limpia la consola y muestra nuevamente solo el menú principal
