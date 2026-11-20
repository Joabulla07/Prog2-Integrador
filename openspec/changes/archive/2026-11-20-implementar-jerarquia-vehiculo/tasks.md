# Tasks

## 1. Estructura de paquetes

- [x] 1.1 Verificar que existe el directorio `src/concesionaria/model/` y crearlo si es necesario, verificando que el directorio se crea exitosamente

## 2. Clase abstracta Vehiculo

- [x] 2.1 Crear archivo `src/concesionaria/model/Vehiculo.java` con clase abstracta que implementa Identificable, EvaluatableComercial y Serializable, verificando que el archivo se crea en la ruta correcta
- [x] 2.2 Declarar atributos privados con Wrappers (codigoInventario: String, marca: String, modelo: String, anio: Integer, color: String, estado: EstadoUnidad, kilometraje: Integer, precioLista: Double), verificando que la declaración compila sin errores
- [x] 2.3 Implementar constructor público sin argumentos (JavaBean), verificando que compila
- [x] 2.4 Implementar constructor parametrizado completo con todos los atributos, verificando que compila
- [x] 2.5 Implementar getters y setters públicos para todos los atributos siguiendo convención getXxx()/setXxx(), verificando que compilan
- [x] 2.6 Declarar métodos abstractos: obtenerTipoVehiculo(), obtenerTipoEstructura(), obtenerFichaDirectiva(), verificando que la declaración compila
- [x] 2.7 Agregar JavaDoc completo a la clase y todos los métodos públicos con @param, @return, @throws según corresponda, verificando que el JavaDoc es válido

## 3. Subclase Automovil

- [x] 3.1 Crear archivo `src/concesionaria/model/Automovil.java` que extiende Vehiculo, verificando que el archivo se crea y la herencia compila
- [x] 3.2 Declarar atributo privado `carroceria` de tipo String, verificando que compila
- [x] 3.3 Implementar constructor público sin argumentos que llama a super(), verificando que compila
- [x] 3.4 Implementar constructor parametrizado completo que incluye carrocería y llama a super() con los atributos heredados, verificando que compila
- [x] 3.5 Implementar getter y setter para carrocería, verificando que compilan
- [x] 3.6 Implementar obtenerTipoVehiculo() devolviendo TipoVehiculo.AUTOMOVIL, verificando que compila
- [x] 3.7 Implementar obtenerTipoEstructura() devolviendo "Automóvil con carrocería [valor]", verificando que compila
- [x] 3.8 Implementar obtenerFichaDirectiva() devolviendo resumen ejecutivo con atributos específicos de Automovil, verificando que compila
- [x] 3.9 Agregar JavaDoc completo a la clase y todos los métodos públicos, verificando que el JavaDoc es válido

## 4. Subclase Camioneta

- [x] 4.1 Crear archivo `src/concesionaria/model/Camioneta.java` que extiende Vehiculo, verificando que el archivo se crea y la herencia compila
- [x] 4.2 Declarar atributo privado `carroceria` de tipo String, verificando que compila
- [x] 4.3 Implementar constructor público sin argumentos que llama a super(), verificando que compila
- [x] 4.4 Implementar constructor parametrizado completo que incluye carrocería y llama a super() con los atributos heredados, verificando que compila
- [x] 4.5 Implementar getter y setter para carrocería, verificando que compilan
- [x] 4.6 Implementar obtenerTipoVehiculo() devolviendo TipoVehiculo.CAMIONETA, verificando que compila
- [x] 4.7 Implementar obtenerTipoEstructura() devolviendo "Camioneta con carrocería [valor]", verificando que compila
- [x] 4.8 Implementar obtenerFichaDirectiva() devolviendo resumen ejecutivo con atributos específicos de Camioneta, verificando que compila
- [x] 4.9 Agregar JavaDoc completo a la clase y todos los métodos públicos, verificando que el JavaDoc es válido

## 5. Subclase Motocicleta

- [x] 5.1 Crear archivo `src/concesionaria/model/Motocicleta.java` que extiende Vehiculo, verificando que el archivo se crea y la herencia compila
- [x] 5.2 Declarar atributo privado `tipoMotocicleta` de tipo String, verificando que compila
- [x] 5.3 Implementar constructor público sin argumentos que llama a super(), verificando que compila
- [x] 5.4 Implementar constructor parametrizado completo que incluye tipoMotocicleta y llama a super() con los atributos heredados, verificando que compila
- [x] 5.5 Implementar getter y setter para tipoMotocicleta, verificando que compilan
- [x] 5.6 Implementar obtenerTipoVehiculo() devolviendo TipoVehiculo.MOTOCICLETA, verificando que compila
- [x] 5.7 Implementar obtenerTipoEstructura() devolviendo "Motocicleta tipo [valor]", verificando que compila
- [x] 5.8 Implementar obtenerFichaDirectiva() devolviendo resumen ejecutivo con atributos específicos de Motocicleta, verificando que compila
- [x] 5.9 Agregar JavaDoc completo a la clase y todos los métodos públicos, verificando que el JavaDoc es válido

## 6. Verificación de compilación

- [x] 6.1 Compilar todas las clases con `javac -d out -cp "lib/*" src/concesionaria/model/*.java src/concesionaria/model/enums/*.java src/concesionaria/interfaces/*.java`, verificando que la compilación termina sin errores
- [x] 6.2 Verificar que no se puede instanciar Vehiculo directamente creando un test de compilación que falle, verificando que el compilador rechaza `new Vehiculo()`
- [x] 6.3 Verificar que se pueden instanciar las subclases creando instancias de prueba de Automovil, Camioneta y Motocicleta, verificando que la compilación y creación de instancias es exitosa
