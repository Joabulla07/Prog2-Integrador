# Tasks

## 1. Implementar el orden natural

- [x] 1.1 Modificar `Vehiculo` para implementar `Comparable<Vehiculo>` y agregar JavaDoc a `compareTo(Vehiculo otro)`, verificando que compila
- [x] 1.2 Implementar comparación solo por código normalizado con `trim().toUpperCase(Locale.ROOT)`, verificando consistencia con `equals`
- [x] 1.3 Crear tests ejecutables para igualdad normalizada, códigos distintos, subclases distintas y valores nulos, verificando que todos pasan
- [x] 1.4 Reutilizar `Locale.ROOT` en `equals`, `hashCode` y `compareTo`, verificando normalización homogénea

## 2. Implementar comparadores alternativos

- [x] 2.1 Crear comparadores de `Vehiculo` para precio, año y marca/modelo, agregando JavaDoc y verificando que compilan
- [x] 2.2 Configurar los comparadores para colocar atributos nulos al final y verificar su comportamiento con tests
- [x] 2.3 Crear tests que ordenen listas con `Collections.sort(...)` y `List.sort(...)`, verificando cada criterio

## 3. Integrar y limpiar el modelo

- [x] 3.1 Verificar las referencias a `concesionaria.interfaces.Comparable` y eliminarla o reemplazarla sin referencias rotas
- [x] 3.2 Compilar todas las clases modificadas y ejecutar los tests de ordenamiento sin errores
- [x] 3.3 Actualizar la spec y el diseño para documentar el orden natural solo por código y dejar otros criterios en comparadores externos

## Workflow follow-up

- Revisar y validar el cambio con OpenSpec.
- Archivar el cambio después de implementar y revisar la US-09.
