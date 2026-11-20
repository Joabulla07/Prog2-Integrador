# Tasks

## 1. Implementar equals y hashCode en Vehiculo

- [x] 1.1 Leer archivo src/concesionaria/model/Vehiculo.java para entender la estructura actual
- [x] 1.2 Implementar método equals(Object o) que compara por codigoInventario normalizado (trim().toUpperCase()), verificando que compila
- [x] 1.3 Implementar método hashCode() usando Objects.hash(codigoInventario), verificando que compila
- [x] 1.4 Agregar JavaDoc a equals y hashCode, verificando que el JavaDoc es válido

## 2. Implementar toString en Vehiculo

- [x] 2.1 Implementar método toString() en Vehiculo con formato "Vehiculo[tipo=..., marca=..., modelo=..., anio=..., codigo=...]", verificando que compila
- [x] 2.2 Agregar manejo de null con marcadores "[sin valor]" para atributos null en toString, verificando que compila
- [x] 2.3 Agregar JavaDoc a toString, verificando que el JavaDoc es válido

## 3. Implementar toString en Automovil

- [x] 3.1 Leer archivo src/concesionaria/model/Automovil.java para entender la estructura actual
- [x] 3.2 Sobreescribir toString() llamando super.toString() y concatenando carrocería, verificando que compila
- [x] 3.3 Agregar JavaDoc a toString, verificando que el JavaDoc es válido

## 4. Implementar toString en Camioneta

- [x] 4.1 Leer archivo src/concesionaria/model/Camioneta.java para entender la estructura actual
- [x] 4.2 Sobreescribir toString() llamando super.toString() y concatenando carrocería, verificando que compila
- [x] 4.3 Agregar JavaDoc a toString, verificando que el JavaDoc es válido

## 5. Implementar toString en Motocicleta

- [x] 5.1 Leer archivo src/concesionaria/model/Motocicleta.java para entender la estructura actual
- [x] 5.2 Sobreescribir toString() llamando super.toString() y concatenando tipoMotocicleta, verificando que compila
- [x] 5.3 Agregar JavaDoc a toString, verificando que el JavaDoc es válido

## 6. Verificación de compilación y comportamiento

- [x] 6.1 Compilar todas las clases con `javac -d out -cp "lib/*" src/concesionaria/model/*.java src/concesionaria/model/enums/*.java src/concesionaria/interfaces/*.java`, verificando que la compilación termina sin errores
- [x] 6.2 Crear test de verificación de equals/hashCode creando dos instancias con mismo código y verificando que equals devuelve true y hashCode es igual
- [x] 6.3 Crear test de verificación de toString invocando toString en instancias de cada subclase y verificando que la salida incluye los atributos esperados
