# Proposal

## Why

El TFI de Programación II requiere implementar tipos enumerativos para modelar la familia de vehículos (automóviles, camionetas, motocicletas) y el estado de las unidades (0 km vs usados). Actualmente no existen estos enums en el proyecto, lo que impide avanzar con la jerarquía de clases Vehiculo y sus subclases.

## What Changes

- Crear enum `TipoVehiculo` en `concesionaria.model.enums` con valores AUTOMOVIL, CAMIONETA, MOTOCICLETA
- Crear enum `EstadoUnidad` en `concesionaria.model.enums` con valores CERO_KM, USADO
- Ambos enums incluirán campo `descripcion` con getter
- Ambos enums incluirán método estático `fromString(String)` tolerante a mayúsculas/minúsculas y etiquetas alternativas
- JavaDoc completo en clases y miembros

## Capabilities

### New Capabilities

- `domain-enums`: Definición de tipos enumerativos del dominio para clasificación de vehículos (TipoVehiculo) y estado de unidades (EstadoUnidad), con métodos de conversión desde texto y descripciones legibles.

### Modified Capabilities

- Ninguna (es una capability nueva)

## Impact

- Crea paquete `concesionaria.model.enums`
- Añade 2 archivos Java: `TipoVehiculo.java` y `EstadoUnidad.java`
- Prepara el terreno para US-07 (jerarquía Vehiculo) y US-09 (repositorio)
- Sin breaking changes - es código nuevo
