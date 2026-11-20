# Proposal

## Why

Las clases Vehiculo y sus subclases (Automovil, Camioneta, Motocicleta) actualmente no sobreescriben los métodos esenciales equals, hashCode y toString. Esto causa problemas cuando se usan en colecciones (HashSet, HashMap) que dependen de equals/hashCode, y dificulta el debugging al no tener representaciones legibles de las instancias. La implementación de estos métodos es un requisito del TFI de Programación II sobre sobreescritura de métodos esenciales.

## What Changes

- Sobreescribir `equals(Object o)` en Vehiculo para comparar por codigoInventario (normalizado)
- Sobreescribir `hashCode()` en Vehiculo usando `Objects.hash(...)` con codigoInventario
- Sobreescribir `toString()` en Vehiculo con resumen legible incluyendo tipo de vehículo
- Sobreescribir `toString()` en Automovil encadenando `super.toString()` con carrocería
- Sobreescribir `toString()` en Camioneta encadenando `super.toString()` con carrocería
- Sobreescribir `toString()` en Motocicleta encadenando `super.toString()` con tipoMotocicleta
- Agregar JavaDoc a los métodos sobrescritos

## Capabilities

### New Capabilities
- `metodos-esenciales-vehiculo`: Implementación de equals, hashCode y toString en la jerarquía de clases Vehiculo para comportamiento correcto en colecciones y debugging

### Modified Capabilities
- Ninguna

## Impact

- Código afectado: clases `Vehiculo`, `Automovil`, `Camioneta`, `Motocicleta` en `src/concesionaria/model/`
- Sin cambios en base de datos, configuración o scripts de ejecución
- Mejora el comportamiento al usar instancias de Vehiculo en colecciones (HashSet, HashMap, ArrayList.contains, etc.)
