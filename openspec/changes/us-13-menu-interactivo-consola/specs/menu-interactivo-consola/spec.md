# Spec Delta

## Purpose

Permite operar la concesionaria desde consola mediante un menú estable conectado al servicio de negocio y MSSQL, sin exponer el código de inventario durante los altas.

## ADDED Requirements

### Requirement: Menú interactivo numerado
El sistema SHALL presentar un menú numerado con las opciones de registro, consulta, eliminación, reporte y salida, y SHALL volver a mostrarlo después de una operación válida.

#### Scenario: Selección válida
- **WHEN** el usuario elige una opción existente del menú
- **THEN** el sistema ejecuta la operación correspondiente y vuelve al menú

#### Scenario: Opción inválida
- **WHEN** el usuario elige una opción inexistente o texto no entero
- **THEN** el sistema muestra un mensaje claro y vuelve a solicitar una opción

#### Scenario: Salida
- **WHEN** el usuario elige la opción 0
- **THEN** el sistema termina el bucle y finaliza la aplicación

### Requirement: Registro interactivo de vehículos
El sistema SHALL registrar automóviles, camionetas y motocicletas usando los controles de entrada existentes sin solicitar codigoInventario.

#### Scenario: Registrar automóvil
- **WHEN** el usuario elige registrar automóvil y completa datos válidos
- **THEN** el sistema crea un Automovil y delega el registro a ConcesionariaService

#### Scenario: Registrar camioneta
- **WHEN** el usuario elige registrar camioneta y completa datos válidos
- **THEN** el sistema crea una Camioneta y delega el registro a ConcesionariaService

#### Scenario: Registrar motocicleta
- **WHEN** el usuario elige registrar motocicleta y completa datos válidos
- **THEN** el sistema crea una Motocicleta y delega el registro a ConcesionariaService

#### Scenario: Alta rechazada
- **WHEN** ConcesionariaService rechaza los datos con DatoVehiculoInvalidoException
- **THEN** el menú muestra el mensaje de error y permanece activo

### Requirement: Listados y filtros interactivos
El sistema SHALL listar todos los vehículos, unidades CERO_KM, unidades USADO y vehículos de un TipoVehiculo seleccionado.

#### Scenario: Listar todos
- **WHEN** el usuario elige listar todos los vehículos
- **THEN** el sistema muestra los vehículos persistidos o un mensaje cuando no existen resultados

#### Scenario: Filtrar por estado
- **WHEN** el usuario elige unidades 0 km o usadas
- **THEN** el sistema muestra únicamente vehículos cuyo EstadoUnidad coincide

#### Scenario: Filtrar por tipo
- **WHEN** el usuario elige filtrar por tipo y selecciona un TipoVehiculo válido
- **THEN** el sistema muestra únicamente vehículos cuyo tipo coincide

### Requirement: Consulta y eliminación por código
El sistema SHALL usar el código de inventario persistido para búsqueda y eliminación.

#### Scenario: Buscar existente
- **WHEN** el usuario busca un código que existe
- **THEN** el sistema muestra los datos del vehículo encontrado

#### Scenario: Buscar inexistente
- **WHEN** el usuario busca un código inexistente
- **THEN** el sistema captura VehiculoNoEncontradoException y muestra un error claro

#### Scenario: Eliminar existente
- **WHEN** el usuario elimina un código existente
- **THEN** el sistema confirma la eliminación y vuelve al menú

#### Scenario: Eliminar inexistente
- **WHEN** el usuario elimina un código inexistente
- **THEN** el sistema captura VehiculoNoEncontradoException y muestra un error claro

### Requirement: Reporte directivo desde consola
El sistema SHALL mostrar el reporte directivo generado por ConcesionariaService.

#### Scenario: Reporte disponible
- **WHEN** el usuario elige ver el reporte
- **THEN** el sistema muestra totales por tipo y estado junto con las fichas persistidas

### Requirement: Manejo estable de errores
El sistema SHALL capturar DatoVehiculoInvalidoException, VehiculoNoEncontradoException y PersistenciaException sin finalizar el menú ante esos errores.

#### Scenario: Error de persistencia
- **WHEN** una operación contra MSSQL falla con PersistenciaException
- **THEN** el sistema muestra un mensaje claro y vuelve al menú

#### Scenario: Operación exitosa
- **WHEN** una operación se completa sin excepción
- **THEN** el sistema muestra un mensaje claro de éxito
