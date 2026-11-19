
# Tasks

## 1. Implementar TipoVehiculo

- [x] 1.1 Crear archivo TipoVehiculo.java en src/concesionaria/model/enums con package correcto y verificar que el archivo existe
- [x] 1.2 Implementar valores enum AUTOMOVIL, CAMIONETA, MOTOCICLETA con constructor que recibe descripcion y verificar que compila
- [x] 1.3 Implementar campo private final String descripcion y getter public String getDescripcion() y verificar que getDescripcion() devuelve "Automóvil" para AUTOMOVIL
- [x] 1.4 Implementar método estático fromString(String) que compara con name(), descripcion y variantes normalizadas y verificar que fromString("AUTOMOVIL") devuelve AUTOMOVIL
- [x] 1.5 Agregar JavaDoc completo a la clase y a cada valor enum y verificar que javadoc no genera errores
- [x] 1.6 Verificar que fromString lanza IllegalArgumentException con mensaje descriptivo para texto inválido

## 2. Implementar EstadoUnidad

- [x] 2.1 Crear archivo EstadoUnidad.java en src/concesionaria/model/enums con package correcto y verificar que el archivo existe
- [x] 2.2 Implementar valores enum CERO_KM, USADO con constructor que recibe descripcion y verificar que compila
- [x] 2.3 Implementar campo private final String descripcion y getter public String getDescripcion() y verificar que getDescripcion() devuelve "0 km" para CERO_KM
- [x] 2.4 Implementar método estático fromString(String) que compara con name(), descripcion y variantes normalizadas y verificar que fromString("CERO_KM") devuelve CERO_KM
- [x] 2.5 Agregar JavaDoc completo a la clase y a cada valor enum y verificar que javadoc no genera errores
- [x] 2.6 Verificar que fromString lanza IllegalArgumentException con mensaje descriptivo para texto inválido

## 3. Verificación de integración

- [x] 3.1 Compilar ambos enums con javac y verificar que no hay errores de compilación
- [x] 3.2 Verificar que ambos enums están en el paquete correcto con la estructura de directorios esperada
