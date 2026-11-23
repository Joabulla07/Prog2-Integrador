# controles-data-entry Specification

## Purpose

Proporciona controles reutilizables de entrada por consola que convierten texto a tipos seguros y repiten lecturas inválidas sin interrumpir el programa.

## Requirements

### Requirement: Lectura de texto obligatorio
El sistema SHALL solicitar hasta recibir texto no vacío cuando se lee un campo obligatorio.

#### Scenario: Texto válido
- **WHEN** el usuario ingresa texto con contenido al leer un campo obligatorio
- **THEN** el sistema devuelve el texto sin espacios innecesarios al inicio o al final

#### Scenario: Texto vacío
- **WHEN** el usuario ingresa una línea vacía o espacios
- **THEN** el sistema muestra un mensaje claro y solicita nuevamente el valor

### Requirement: Lectura de entero wrapper dentro de rango
El sistema SHALL convertir entradas enteras con `Integer.valueOf()` y validar que estén dentro del rango inclusive recibido.

#### Scenario: Entero válido
- **WHEN** el usuario ingresa un entero dentro de mínimo y máximo
- **THEN** el sistema devuelve un Integer dentro del rango

#### Scenario: Entero fuera de rango
- **WHEN** el usuario ingresa un entero menor al mínimo o mayor al máximo
- **THEN** el sistema muestra el rango permitido y solicita nuevamente el valor

#### Scenario: Texto no entero
- **WHEN** el usuario ingresa texto que no puede convertirse con `Integer.valueOf()`
- **THEN** el sistema muestra un mensaje claro y solicita nuevamente el valor

### Requirement: Lectura de decimal wrapper con mínimo
El sistema SHALL convertir entradas decimales con `Double.valueOf()` y rechazar valores menores al mínimo recibido.

#### Scenario: Decimal válido
- **WHEN** el usuario ingresa un decimal igual o mayor al mínimo
- **THEN** el sistema devuelve un Double válido

#### Scenario: Decimal menor al mínimo
- **WHEN** el usuario ingresa un decimal menor al mínimo
- **THEN** el sistema muestra el mínimo permitido y solicita nuevamente el valor

#### Scenario: Texto no decimal
- **WHEN** el usuario ingresa texto que no puede convertirse con `Double.valueOf()`
- **THEN** el sistema muestra un mensaje claro y solicita nuevamente el valor

### Requirement: Lectura genérica de enum
El sistema SHALL permitir leer cualquier enum por nombre o por las formas de texto aceptadas por ese tipo sin duplicar validaciones por cada enum.

#### Scenario: Enum válido
- **WHEN** el usuario ingresa el nombre o la descripción reconocida de un valor enum
- **THEN** el sistema devuelve el valor correspondiente

#### Scenario: Enum inválido
- **WHEN** el usuario ingresa un valor vacío o no reconocido
- **THEN** el sistema muestra las opciones disponibles y solicita nuevamente el valor

### Requirement: Entrada estable sin ruptura de flujo
El sistema SHALL mantener la lectura activa ante entradas inválidas y SHALL no solicitar ni almacenar un código de inventario.

#### Scenario: Secuencia con entradas inválidas
- **WHEN** el usuario comete uno o varios errores de formato, rango o texto vacío
- **THEN** cada error muestra un mensaje y el control vuelve a solicitar hasta obtener un valor válido

#### Scenario: Intento de ingresar código de inventario
- **WHEN** el flujo solicita datos de un nuevo vehículo
- **THEN** no existe una entrada de usuario para codigoInventario y ese código queda reservado a la persistencia
