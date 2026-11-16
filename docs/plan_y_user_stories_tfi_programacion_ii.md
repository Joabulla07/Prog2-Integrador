# Plan de Modelado y Tablero de User Stories — TFI Programación II (UTN FRRe)

## 1. Contexto del Dominio y Requerimientos del TFI

* **Escenario**: Concesionaria Multimarcas de vehículos que gestiona **Automóviles, Camionetas y Motocicletas**, diferenciando unidades **0 km** y **usadas** (con kilometraje recorrido).
* **Requerimientos de directivos**:
  * Marca
  * Modelo
  * Año
  * Color
  * Carrocería (o tipo de Motocicleta)
* **Requisitos técnicos integrados**:
  * Dominio del problema, abstracción de la realidad, modelado y tipos de datos.
  * Organización de código mediante esquema **"Diseño vs. Negocio"** y separación por capas (Presentación/Consola, Lógica de Negocio, Datos).
  * Documentación interna vía **remarks** (`//`) y **JavaDoc** (`/** ... */`).
  * **Clases Abstractas, Herencia y Polimorfismo**.
  * Patrones de diseño **JavaBean** y **POJO**.
  * **Implementación de Interfaces** y transmisión de comportamiento.
  * **Tratamiento de Excepciones** (incluyendo **Excepciones Propias**).
  * **Tipos Enumerativos** (`enum`).
  * Énfasis en la **interacción con el usuario** (menús de consola con opciones, controles de *data-entry*, mensajes por consola).
  * **Aplicación de Wrappers** (`Integer`, `Double`, etc.).
  * **Organización de los datos utilizando una Colección** (`List`, `ArrayList`, etc.).
  * **Sobreescritura de métodos esenciales** (`equals`, `hashCode`, `toString`).
  * **Persistencia real**: Base de datos **MSSQL en Docker**, conectada mediante **Sql2o** y configurada con **java-dotenv** para variables de entorno.
  * **Ejecución en consola separada**: Compilación en la consola habitual/IDE y apertura de una consola aparte en negro y limpia al ejecutar el programa interactivo.

## 2. Decisiones Clave de Arquitectura y Modelado

### 2.1 Estructura de Paquetes (`com.utn.frre.concesionaria`)

* `config`: carga de entorno con `java-dotenv` y fábrica de conexiones `Sql2o`.
* `model`: clase abstracta `Vehiculo` (JavaBean/POJO) y subclases `Automovil`, `Camioneta`, `Motocicleta`.
* `model.enums`: `EstadoUnidad` y `TipoVehiculo` (los únicos enums del dominio; carrocerías y tipos de moto se manejan como texto libre validado para no limitar el catálogo multimarca).
* `interfaces`: `Identificable`, `EvaluatableComercial`.
* `repository`: `VehiculoRepository` y `MssqlVehiculoRepository` (con `Sql2o` + colecciones `List<Vehiculo>`).
* `service`: `ConcesionariaService` (reglas de negocio, validaciones de 0 km/usado y reportes directivos).
* `exception`: `DatoVehiculoInvalidoException`, `VehiculoNoEncontradoException`, `PersistenciaException`.
* `ui`: `ConsolaApp`, `ValidadorEntrada`, `LauncherConsola` (y script `ejecutar.bat` en raíz).

### 2.2 Jerarquía, Polimorfismo, JavaBean/POJO y Wrappers

* `Vehiculo` es `abstract`, implementa `Identificable`, `EvaluatableComercial` y `Serializable`.
* Cumple el estándar **JavaBean / POJO**: atributos `private`, constructor público sin argumentos, constructor parametrizado, getters y setters públicos.
* Usa **Wrappers** (`Integer` para `anio` y `kilometraje`; `Double` para `precioLista`) y los enums del dominio (`EstadoUnidad` y `TipoVehiculo`).
* Para no duplicar estado ni desincronizar la jerarquía, cada subclase define su `TipoVehiculo` mediante un método de consulta/polimórfico (`obtenerTipoVehiculo()`), mientras que `Vehiculo` declara métodos abstractos polimórficos: `obtenerTipoEstructura()` y `obtenerFichaDirectiva()`.
* Subclases:
  * `Automovil extends Vehiculo` -> `obtenerTipoVehiculo()` devuelve `TipoVehiculo.AUTOMOVIL`; atributo `String carroceria` (texto libre validado, ej. Sedán, Hatchback, Coupé, etc.).
  * `Camioneta extends Vehiculo` -> `obtenerTipoVehiculo()` devuelve `TipoVehiculo.CAMIONETA`; atributo `String carroceria` (texto libre validado, ej. Pick-up Doble Cabina, SUV, Furgón, etc.).
  * `Motocicleta extends Vehiculo` -> `obtenerTipoVehiculo()` devuelve `TipoVehiculo.MOTOCICLETA`; atributo `String tipoMotocicleta` (texto libre validado, ej. Naked, Enduro, Scooter, Custom, etc.).

### 2.3 Estrategia para Consola Separada en Negro

* Se compila en la consola normal o IDE con `javac` hacia `out/`.
* Se ejecuta mediante `ejecutar.bat` (o `LauncherConsola` con `ProcessBuilder`), que abre una ventana nueva de `cmd` en negro (`start "Concesionaria Multimarcas - TFI Programacion II" cmd /k "cls && java -cp ..."`) para correr `ConsolaApp` sin logs de compilación.

## 3. Tablero de User Stories (Listas para CodeAR)

### US-01 — Base de Datos MSSQL en Docker, Tablas y Datos Semilla

* **Contexto del TP**: Gestión multimarca (Automóviles, Camionetas, Motocicletas), 0 km vs. usados (con kilometraje), y atributos de directivos (Marca, Modelo, Año, Color, Carrocería o tipo de Motocicleta).
* **Objetivo**: Levantar MSSQL en Docker y crear el esquema relacional para persistir los vehículos multimarca.
* **Especificaciones técnicas**:
  * **Docker**: levantar contenedor MSSQL (ej. `mcr.microsoft.com/mssql/server:2022-latest`), exponiendo puerto `1433`, `ACCEPT_EULA=Y` y `MSSQL_SA_PASSWORD`.
  * **Base de datos**: `ConcesionariaTFI`.
  * **Tabla `Vehiculos`**:
    * `codigo_inventario` (`VARCHAR(20) PRIMARY KEY`) — ej. `AUTO-001`, `CAM-002`, `MOTO-003`.
    * `tipo_vehiculo` (`VARCHAR(20) NOT NULL`) — discriminador tipado con `TipoVehiculo`: `'AUTOMOVIL'`, `'CAMIONETA'`, `'MOTOCICLETA'`.
    * `marca` (`VARCHAR(60) NOT NULL`).
    * `modelo` (`VARCHAR(60) NOT NULL`).
    * `anio` (`INT NOT NULL`).
    * `color` (`VARCHAR(40) NOT NULL`).
    * `estado_unidad` (`VARCHAR(15) NOT NULL`) — `'CERO_KM'` o `'USADO'`.
    * `kilometraje` (`INT NOT NULL DEFAULT 0`).
    * `precio_lista` (`DECIMAL(14,2) NOT NULL`).
    * `carroceria` (`VARCHAR(60) NULL`) — texto libre requerido si `tipo_vehiculo` es `'AUTOMOVIL'` o `'CAMIONETA'`.
    * `tipo_motocicleta` (`VARCHAR(60) NULL`) — texto libre requerido si `tipo_vehiculo` es `'MOTOCICLETA'`.
    * `CONSTRAINT CK_Vehiculos_Tipo CHECK (tipo_vehiculo IN ('AUTOMOVIL', 'CAMIONETA', 'MOTOCICLETA'))`
    * `CONSTRAINT CK_Vehiculos_Estado CHECK (estado_unidad IN ('CERO_KM', 'USADO'))`
    * `CONSTRAINT CK_Vehiculos_Estado_Km CHECK ((estado_unidad = 'CERO_KM' AND kilometraje = 0) OR (estado_unidad = 'USADO' AND kilometraje > 0))`
    * `CONSTRAINT CK_Vehiculos_Estructura CHECK ((tipo_vehiculo IN ('AUTOMOVIL', 'CAMIONETA') AND carroceria IS NOT NULL AND LEN(LTRIM(RTRIM(carroceria))) > 0 AND tipo_motocicleta IS NULL) OR (tipo_vehiculo = 'MOTOCICLETA' AND tipo_motocicleta IS NOT NULL AND LEN(LTRIM(RTRIM(tipo_motocicleta))) > 0 AND carroceria IS NULL))`
  * **Datos semilla (`seed.sql`)**: insertar al menos 6 registros (1 auto 0 km, 1 auto usado, 1 camioneta 0 km, 1 camioneta usada, 1 moto 0 km, 1 moto usada).
* **Criterios de aceptación**:
  * El contenedor MSSQL arranca y acepta conexiones en `localhost:1433`.
  * Los `CHECK` rechazan inconsistencias (`tipo_vehiculo` inválido, `CERO_KM` con `kilometraje > 0`, `MOTOCICLETA` con `carroceria`, o textos vacíos).
  * `SELECT * FROM Vehiculos` devuelve los datos semilla.

### US-02 — Configuración de Entorno (`java-dotenv`) y Conexión `Sql2o`

* **Contexto del TP**: Separación en capas ("Diseño vs. Negocio" / Interfaz, Lógica, Datos) y tipos de datos.
* **Objetivo**: Centralizar la lectura de variables de entorno con `java-dotenv` y la fábrica de conexiones `Sql2o`.
* **Especificaciones técnicas**:
  * **Archivos en raíz**: `.env` (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_ENCRYPT`, `DB_TRUST_SERVER_CERTIFICATE`) y `.env.example`.
  * **Clase `com.utn.frre.concesionaria.config.DatabaseConfig`**:
    * Carga `Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();`.
    * Construye la URL JDBC de MSSQL: `jdbc:sqlserver://<host>:<port>;databaseName=<db>;encrypt=<encrypt>;trustServerCertificate=<trust>`.
    * Expone `public static Sql2o getSql2o()`.
    * Documentar con JavaDoc (`@return`, `@throws`) y comentarios `//`.
* **Criterios de aceptación**:
  * `DatabaseConfig.getSql2o().open()` abre y cierra una conexión válida contra MSSQL en Docker sin credenciales hardcodeadas.

### US-03 — Estructura de Paquetes y Estándar de Documentación

* **Contexto del TP**: Esquema "Diseño vs. Negocio", abstracción, modelado, tipos de datos y documentación interna vía remarks y JavaDoc.
* **Objetivo**: Dejar lista la arquitectura de paquetes y la convención de documentación.
* **Especificaciones técnicas**:
  * **Paquetes base**:
    * `com.utn.frre.concesionaria.config`
    * `com.utn.frre.concesionaria.model`
    * `com.utn.frre.concesionaria.model.enums`
    * `com.utn.frre.concesionaria.interfaces`
    * `com.utn.frre.concesionaria.repository`
    * `com.utn.frre.concesionaria.service`
    * `com.utn.frre.concesionaria.exception`
    * `com.utn.frre.concesionaria.ui`
  * **Documentación**: JavaDoc (`/** ... */`) en clases públicas, interfaces, enums y métodos públicos con `@param`, `@return`, `@throws`; comentarios `//` en lógica relevante.
* **Criterios de aceptación**:
  * Estructura creada y `javac` compila sin errores.

### US-04 — Tipos Enumerativos del Dominio (`model.enums`)

* **Contexto del TP**: Tipos enumerativos y modelado de familia de vehículo y estado 0 km vs. usado.
* **Objetivo**: Crear los enums tipados `TipoVehiculo` y `EstadoUnidad` para clasificar unidades sin restringir carrocerías ni tipos de moto a un catálogo fijo.
* **Especificaciones técnicas**:
  * `TipoVehiculo`: `AUTOMOVIL("Automóvil")`, `CAMIONETA("Camioneta")`, `MOTOCICLETA("Motocicleta")`.
    * Campo `private final String descripcion;` con getter `getDescripcion()`.
    * Método `fromString(String valor)` tolerante a mayúsculas/minúsculas o etiquetas, lanzando `IllegalArgumentException` si no coincide.
  * `EstadoUnidad`: `CERO_KM("0 km")`, `USADO("Usado")`.
    * Campo `private final String descripcion;` con getter `getDescripcion()`.
    * Método `fromString(String valor)` tolerante a mayúsculas/minúsculas o etiquetas como `"0 km"` / `"CERO_KM"`, lanzando `IllegalArgumentException` si no coincide.
  * No crear enums cerrados para carrocerías ni tipos de motocicleta: esos datos quedan como `String` libre validado en el dominio.
  * JavaDoc completo en ambos enums y sus miembros.
* **Criterios de aceptación**:
  * Tanto la familia de vehículo (`TipoVehiculo`) como el estado (`EstadoUnidad`) se manejan mediante enums en Java y en el mapeo con MSSQL.

### US-05 — Excepciones Propias (`exception`)

* **Contexto del TP**: Tratamiento de Excepciones (incluyendo Excepciones Propias).
* **Objetivo**: Crear excepciones propias del dominio y la persistencia.
* **Especificaciones técnicas**:
  * `DatoVehiculoInvalidoException extends Exception`: año fuera de rango, kilometraje negativo, `CERO_KM` con `kilometraje > 0`, `USADO` con `kilometraje <= 0`, `TipoVehiculo` o `EstadoUnidad` inválido, o campos obligatorios (incluyendo carrocería o tipo de motocicleta) vacíos.
  * `VehiculoNoEncontradoException extends Exception`: código de inventario inexistente al buscar o eliminar.
  * `PersistenciaException extends RuntimeException`: fallos de `Sql2o` / MSSQL preservando la causa (`Throwable`).
  * JavaDoc en cada clase y sus constructores.
* **Criterios de aceptación**:
  * Las excepciones compilan y pueden instanciarse con mensaje y causa.

### US-06 — Interfaces y Transmisión de Comportamiento (`interfaces`)

* **Contexto del TP**: Implementación de Interfaces y transmisión del comportamiento.
* **Objetivo**: Definir contratos de comportamiento transversal.
* **Especificaciones técnicas**:
  * `Identificable`: `String getCodigoInventario();`.
  * `EvaluatableComercial`: `String obtenerCategoriaComercial();` y `boolean esAptoEntregaInmediata();`.
  * JavaDoc explicando contratos y precondiciones.
* **Criterios de aceptación**:
  * Interfaces declaradas en `com.utn.frre.concesionaria.interfaces` con JavaDoc.

### US-07 — Clase Abstracta `Vehiculo` (POJO / JavaBean + Wrappers) y Subclases (`model`)

* **Contexto del TP**: Clases Abstractas, Herencia, Polimorfismo, patrones JavaBean y POJO, Wrappers y atributos de directivos.
* **Objetivo**: Implementar la jerarquía de clases del dominio.
* **Especificaciones técnicas**:
  * `Vehiculo` (abstracta, implementa `Identificable`, `EvaluatableComercial`, `Serializable`):
    * Atributos `private` con Wrappers: `codigoInventario` (`String`), `marca` (`String`), `modelo` (`String`), `anio` (`Integer`), `color` (`String`), `estado` (`EstadoUnidad`), `kilometraje` (`Integer`), `precioLista` (`Double`).
    * Constructor público vacío `public Vehiculo()`, constructor parametrizado, getters y setters públicos.
    * Métodos polimórficos:
      * `public abstract TipoVehiculo obtenerTipoVehiculo();`
      * `public abstract String obtenerTipoEstructura();`
      * `public abstract String obtenerFichaDirectiva();`
  * `Automovil extends Vehiculo`: `private String carroceria;`, `obtenerTipoVehiculo()` devuelve `TipoVehiculo.AUTOMOVIL`, constructor vacío + completo, getter/setter, implementación polimórfica.
  * `Camioneta extends Vehiculo`: `private String carroceria;`, `obtenerTipoVehiculo()` devuelve `TipoVehiculo.CAMIONETA`, constructor vacío + completo, getter/setter, implementación polimórfica.
  * `Motocicleta extends Vehiculo`: `private String tipoMotocicleta;`, `obtenerTipoVehiculo()` devuelve `TipoVehiculo.MOTOCICLETA`, constructor vacío + completo, getter/setter, implementación polimórfica.
* **Criterios de aceptación**:
  * No se puede instanciar `Vehiculo` directamente; las subclases son JavaBeans válidos y responden polimórficamente a `obtenerTipoVehiculo()`, `obtenerTipoEstructura()` y `obtenerFichaDirectiva()`.

### US-08 — Sobreescritura de Métodos Esenciales (`equals`, `hashCode`, `toString`)

* **Contexto del TP**: Sobreescritura de métodos esenciales.
* **Objetivo**: Implementar `equals`, `hashCode` y `toString` en la jerarquía `Vehiculo`.
* **Especificaciones técnicas**:
  * `Vehiculo`: `equals(Object o)` por `codigoInventario` (normalizado), `hashCode()` con `Objects.hash(...)`, `toString()` con resumen legible incluyendo `obtenerTipoVehiculo()`.
  * `Automovil`, `Camioneta`, `Motocicleta`: `toString()` encadenando `super.toString()` con `carroceria` o `tipoMotocicleta`.
* **Criterios de aceptación**:
  * Dos instancias con el mismo código dan `equals == true` y mismo `hashCode`; `toString()` muestra el detalle de cada subtipo.

### US-09 — Repositorio MSSQL con `Sql2o` y Colecciones (`repository`)

* **Contexto del TP**: Capa de datos y organización de datos utilizando una Colección.
* **Objetivo**: Implementar persistencia en MSSQL usando `Sql2o` y trabajar con colecciones en memoria (`List<Vehiculo>`).
* **Especificaciones técnicas**:
  * Interfaz `VehiculoRepository`: `guardar`, `buscarPorCodigo`, `listarTodos`, `listarPorEstado`, `listarPorTipo`, `eliminarPorCodigo`.
  * `MssqlVehiculoRepository implements VehiculoRepository`: usa `DatabaseConfig.getSql2o()`, consultas parametrizadas con `Sql2o`, persiste `tipo_vehiculo` desde `vehiculo.obtenerTipoVehiculo().name()`, lee `tipo_vehiculo` mediante `TipoVehiculo.fromString(...)` junto con `carroceria` y `tipo_motocicleta` como `String` para instanciar `Automovil`, `Camioneta` o `Motocicleta`, devuelve `ArrayList<Vehiculo>` y traduce errores `Sql2oException` a `PersistenciaException`.
* **Criterios de aceptación**:
  * Altas, consultas, filtros por `TipoVehiculo` / `EstadoUnidad` y bajas persisten en `Vehiculos` de Docker MSSQL y se devuelven en `List<Vehiculo>`.

### US-10 — Lógica de Negocio `ConcesionariaService` y Reportes Directivos (`service`)

* **Contexto del TP**: Separación "Diseño vs. Negocio", abstracción, polimorfismo y requerimientos de directivos.
* **Objetivo**: Centralizar validaciones de dominio, reglas 0 km/usado y reportes para directivos.
* **Especificaciones técnicas**:
  * `ConcesionariaService` con inyección de `VehiculoRepository` por constructor.
  * `registrarVehiculo(Vehiculo v)`: valida campos obligatorios no vacíos (incluyendo `carroceria` en `Automovil`/`Camioneta` y `tipoMotocicleta` en `Motocicleta`), `TipoVehiculo` y `EstadoUnidad` no nulos, rango de año (`1950` a `Year.now().getValue() + 1`), `precioLista > 0`, coherencia `CERO_KM` (`kilometraje = 0`) vs. `USADO` (`kilometraje > 0`), y unicidad de código.
  * `obtenerVehiculoPorCodigo` y `eliminarVehiculo`: lanzan `VehiculoNoEncontradoException` si no existe.
  * `generarReporteDirectivos()`: totales por `TipoVehiculo` (`AUTOMOVIL`, `CAMIONETA`, `MOTOCICLETA`) y por `EstadoUnidad` (`0 km` vs `Usados`), más fichas directivas (`obtenerFichaDirectiva()`).
* **Criterios de aceptación**:
  * Ninguna regla de validación queda en `ConsolaApp` ni en `Vehiculo`; reporte directivo completo.

### US-11 — Controles de *Data-Entry* y Wrappers (`ui`)

* **Contexto del TP**: Interacción con el usuario (menús de consola, controles de *data-entry*, mensajes) y aplicación de Wrappers.
* **Objetivo**: Crear utilidades de entrada segura con `Scanner` y wrappers (`Integer`, `Double`).
* **Especificaciones técnicas**:
  * `ValidadorEntrada` con `Scanner(System.in)` usando `nextLine()`:
    * `leerTextoObligatorio(String etiqueta)` (sirve para marca, modelo, color, carrocería y tipo de motocicleta con texto abierto).
    * `leerEnteroWrapper(String etiqueta, int min, int max)` con `Integer.valueOf(...)`.
    * `leerDecimalWrapper(String etiqueta, double min)` con `Double.valueOf(...)`.
    * `<E extends Enum<E>> E leerEnum(String etiqueta, Class<E> enumClass)` (usado para `TipoVehiculo` y `EstadoUnidad`).
* **Criterios de aceptación**:
  * Entradas inválidas o vacías muestran mensaje claro y reintentan sin romper el programa.

### US-12 — Menú Interactivo de Consola `ConsolaApp` (`ui`)

* **Contexto del TP**: Interacción con el usuario mediante menú de consola con opciones y mensajes.
* **Objetivo**: Implementar el menú interactivo conectado a `ConcesionariaService`.
* **Especificaciones técnicas**:
  * `ConsolaApp` con `main` y menú en bucle:
    1. Registrar Automóvil
    2. Registrar Camioneta
    3. Registrar Motocicleta
    4. Listar todos los vehículos
    5. Listar unidades 0 km
    6. Listar unidades usadas
    7. Filtrar por tipo de vehículo (`TipoVehiculo`)
    8. Buscar vehículo por código de inventario
    9. Eliminar vehículo por código de inventario
    10. Ver reporte para directivos
    0. Salir
  * Captura `DatoVehiculoInvalidoException`, `VehiculoNoEncontradoException` y `PersistenciaException`.
* **Criterios de aceptación**:
  * Todas las opciones operan contra MSSQL y muestran mensajes claros de éxito o error.

### US-13 — Consola Separada en Negro al Ejecutar (`ui` + script)

* **Contexto del TP**: Esquema de capas y ejecución en consola separada limpia en negro.
* **Objetivo**: Abrir una ventana de consola nueva en negro al ejecutar `ConsolaApp`, compilando en la consola/IDE habitual.
* **Especificaciones técnicas**:
  * `ejecutar.bat` en raíz con `chcp 65001 >nul` y `start "Concesionaria Multimarcas - TFI Programacion II" cmd /k "cls && java -cp "out;lib/*" com.utn.frre.concesionaria.ui.ConsolaApp"`.
  * Opcional `LauncherConsola.java` en `ui` usando `ProcessBuilder` con `cmd.exe /c start ...` y `System.exit(0)`.
* **Criterios de aceptación**:
  * Al correr `ejecutar.bat`, se abre una consola nueva en negro mostrando solo el encabezado y el menú de `ConsolaApp`.

### US-14 — Verificación Integral, Generación de JavaDoc y Cierre de Entrega

* **Contexto del TP**: Cierre del TFI de Programación II (UTN FRRe), JavaDoc y datos de portada.
* **Objetivo**: Auditar el cumplimiento completo y generar JavaDoc.
* **Especificaciones técnicas**:
  * Ejecutar `javadoc -d docs -sourcepath src -subpackages com.utn.frre.concesionaria`.
  * Probar persistencia real reiniciando la consola separada y verificando datos en MSSQL.
  * Completar datos del alumno (Alumno, Legajo, Email) en el documento del TFI.
* **Criterios de aceptación**:
  * Carpeta `docs/` generada con JavaDoc, persistencia verificada y entrega lista.