# Proposal

## Why

El sistema actual no tiene la jerarquía de clases del dominio de vehículos. Necesitamos implementar la clase abstracta Vehiculo y sus subclases (Automovil, Camioneta, Motocicleta) para modelar correctamente el dominio de la concesionaria multimarca, cumpliendo con los requisitos del TFI de Programación II sobre clases abstractas, herencia, polimorfismo y patrones JavaBean/POJO.

## What Changes

- Crear clase abstracta `Vehiculo` en `src/concesionaria/model/` con atributos privados usando Wrappers (Integer, Double), constructores JavaBean (vacío y parametrizado), getters/setters públicos, e implementación de interfaces Identificable, EvaluatableComercial y Serializable
- Crear subclase `Automovil extends Vehiculo` con atributo `carroceria` (String libre validado), implementación de métodos polimórficos `obtenerTipoVehiculo()`, `obtenerTipoEstructura()`, `obtenerFichaDirectiva()`
- Crear subclase `Camioneta extends Vehiculo` con atributo `carroceria` (String libre validado), implementación de métodos polimórficos
- Crear subclase `Motocicleta extends Vehiculo` con atributo `tipoMotocicleta` (String libre validado), implementación de métodos polimórficos
- Agregar JavaDoc completo en todas las clases y métodos públicos

## Capabilities

### New Capabilities
- `jerarquia-vehiculo`: Implementación de la jerarquía de clases del dominio con clase abstracta Vehiculo y subclases específicas, siguiendo patrones JavaBean/POJO, usando Wrappers y polimorfismo

### Modified Capabilities
- Ninguna

## Impact

- Código afectado: nuevo paquete `src/concesionaria/model/` con 4 clases (Vehiculo, Automovil, Camioneta, Motocicleta)
- Interfaces: las clases implementarán interfaces existentes (Identificable, EvaluatableComercial) ya definidas en `src/concesionaria/interfaces/`
- Enums: usarán TipoVehiculo y EstadoUnidad ya implementados en `src/concesionaria/model/enums/`
- Sin cambios en base de datos, configuración o scripts de ejecución
