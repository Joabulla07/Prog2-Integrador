# Proposal

## Why

El sistema necesita ordenar vehículos en listas por reglas predecibles y reutilizables. Actualmente existe una interfaz personalizada sin implementar y no hay una estrategia basada en las interfaces estándar `Comparable` y `Comparator` de Java.

## What Changes

- `Vehiculo` implementará `java.lang.Comparable<Vehiculo>` mediante `compareTo(Vehiculo otro)`.
- El orden natural se basará exclusivamente en el código de inventario normalizado, consistente con `equals` y `hashCode`; precio y demás criterios quedarán en comparadores externos.
- Se eliminará o reemplazará la interfaz personalizada `concesionaria.interfaces.Comparable` para evitar ambigüedad con la interfaz estándar.
- Se incorporarán comparadores externos de `Vehiculo` para precio, año y marca/modelo.
- Las colecciones podrán ordenarse mediante `Collections.sort(...)` o `List.sort(...)`.
- Se agregarán pruebas del orden natural, los comparadores y el manejo de valores nulos.

## Capabilities

### New Capabilities
- `ordenamiento-vehiculos`: ordenamiento natural y mediante comparadores de vehículos en listas de la concesionaria.

### Modified Capabilities
- Ninguna.

## Impact

- Código afectado: `Vehiculo` e interfaces existentes del paquete `concesionaria.interfaces`.
- Código nuevo esperado: comparadores del dominio y pruebas de ordenamiento.
- Sin cambios en base de datos, configuración, persistencia ni menú interactivo.
