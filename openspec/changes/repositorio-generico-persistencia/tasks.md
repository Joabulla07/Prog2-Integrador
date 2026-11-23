# Tasks

## 1. Interfaz genérica RepositorioGenerico

- [x] 1.1 Crear interfaz RepositorioGenerico<T extends Identificable, ID> en src/concesionaria/repository con métodos guardar(T), buscarPorCodigo(ID), listarTodos() y eliminarPorCodigo(ID), y verificar que compila sin errores
- [x] 1.2 Agregar JavaDoc completo a la interfaz y sus métodos con @param y @return, y verificar que javadoc no genera warnings

## 2. Clase genérica IndiceGenerico

- [x] 2.1 Crear clase IndiceGenerico<K, V extends Identificable> con HashMap<K, V> interno y métodos agregar(K, V), buscar(K), contiene(K) y eliminar(K), y verificar que compila
- [x] 2.2 Agregar JavaDoc a IndiceGenerico explicando que es una utilidad de Map tipado sin sincronización con DB, y verificar que la documentación es clara

## 3. Interfaz VehiculoRepository

- [x] 3.1 Crear interfaz VehiculoRepository extends RepositorioGenerico<Vehiculo, String> con métodos listarPorEstado(EstadoUnidad) y listarPorTipo(TipoVehiculo), y verificar que hereda correctamente los métodos CRUD genéricos
- [x] 3.2 Agregar JavaDoc a VehiculoRepository describiendo los métodos específicos del dominio, y verificar que la documentación es consistente con el spec

## 4. Implementación MssqlVehiculoRepository - estructura básica

- [x] 4.1 Crear clase MssqlVehiculoRepository implements VehiculoRepository con inyección de Sql2o via constructor, y verificar que compila
- [x] 4.2 Implementar método listarTodos() con query SELECT * FROM Vehiculos, mapeo polimórfico usando factory privado, y verificar que devuelve ArrayList<Vehiculo> con todos los registros
- [x] 4.3 Implementar método buscarPorCodigo(String) con query WHERE codigo_inventario = :codigo, y verificar que devuelve el vehículo correcto o null si no existe

## 5. Implementación MssqlVehiculoRepository - alta con OUTPUT INSERTED

- [x] 5.1 Implementar método guardar(Vehiculo) con INSERT sin codigo_inventario usando OUTPUT INSERTED.codigo_inventario, y verificar que el INSERT ejecuta correctamente
- [x] 5.2 Implementar recuperación de codigo_inventario desde OUTPUT y llamada a vehiculo.asignarCodigoInventario(), y verificar que el código se asigna una única vez
- [x] 5.3 Implementar manejo de Sql2oException traduciendo a PersistenciaException con causa preservada, y verificar que los errores se propagan correctamente

## 6. Implementación MssqlVehiculoRepository - mapeo polimórfico

- [x] 6.1 Implementar método privado factory que lee tipo_vehiculo y crea Automovil/Camioneta/Motocicleta según TipoVehiculo.fromString(), y verificar que cada tipo se instancia correctamente
- [x] 6.2 Implementar mapeo de carroceria para Automovil/Camioneta y tipoMotocicleta para Motocicleta, y verificar que los atributos específicos se asignan correctamente
- [x] 6.3 Implementar conversión de estado_unidad desde string a EstadoUnidad usando EstadoUnidad.fromString(), y verificar que la conversión funciona para ambos valores

## 7. Implementación MssqlVehiculoRepository - filtros y eliminación

- [x] 7.1 Implementar método listarPorEstado(EstadoUnidad) con query WHERE estado_unidad = :estado, y verificar que filtra correctamente por CERO_KM y USADO
- [x] 7.2 Implementar método listarPorTipo(TipoVehiculo) con query WHERE tipo_vehiculo = :tipo, y verificar que filtra correctamente por AUTOMOVIL, CAMIONETA y MOTOCICLETA
- [x] 7.3 Implementar método eliminarPorCodigo(String) con DELETE WHERE codigo_inventario = :codigo, y verificar que el registro se elimina de la DB

## 8. Verificación de integración

- [x] 8.1 Compilar todo el proyecto con javac y verificar que no hay errores de compilación en el paquete repository
- [x] 8.2 Probar el flujo completo de alta: crear vehículo, guardar, verificar código asignado, buscar por código, y verificar que todas las operaciones funcionan contra MSSQL (REQUIERE DOCKER ACTIVO)
- [x] 8.3 Verificar que las consultas parametrizadas previenen inyección SQL probando con valores especiales en los parámetros (REQUIERE DOCKER ACTIVO)
