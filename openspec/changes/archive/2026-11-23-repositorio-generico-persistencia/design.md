# Design

## Context

Estado actual:
- Modelo de vehículos (Vehiculo, Automovil, Camioneta, Motocicleta) implementado con JavaBean/POJO
- Interfaz Identificable con getCodigoInventario()
- Base de datos MSSQL en Docker con tabla Vehiculos y columna codigo_inventario calculada PERSISTED
- Configuración Sql2o en ConexionDB.sql2o
- Paquete repositorio/ vacío
- Excepción PersistenciaException ya definida

Ver proposal.md para motivación del cambio.

## Goals / Non-Goals

**Goals:**
- Implementar arquitectura de repositorio genérico reutilizable con Generics
- Persistir vehículos en MSSQL usando Sql2o con consultas parametrizadas
- Generar codigo_inventario en MSSQL y asignarlo una única vez al objeto
- Mapear polimórficamente desde tipo_vehiculo a subclases concretas
- Proporcionar utilidad de indexación en memoria con tipado seguro

**Non-Goals:**
- Caché en memoria sincronizada con DB (IndiceGenerico es utilidad, no caché)
- Transacciones complejas (OUTPUT INSERTED es atómico por diseño)
- Soporte para otros tipos de entidades además de Vehiculo

## Decisions

### 1. IndiceGenerico como utilidad Map tipado

**Decisión:** IndiceGenerico<K, V extends Identificable> es un wrapper sobre HashMap<K, V> con métodos de ayuda (agregar, buscar, contiene, eliminar), sin sincronización con DB.

**Racional:**
- Simple de implementar, cumple requisito de genéricos y tipado seguro
- Reutilizable en otros contextos sin preocupación de sincronización
- Si se necesita caché, se puede agregar en capa superior (service)

**Alternativa descartada:** Índice caché sincronizado con DB - agrega complejidad innecesaria para el TFI.

### 2. Filtrado directo en MSSQL

**Decisión:** listarPorEstado() y listarPorTipo() ejecutan queries WHERE en MSSQL, no cargan todos los vehículos en memoria.

**Racional:**
- Patrón estándar de repository, escala mejor
- DB hace el filtrado con índices existentes
- No carga datos innecesarios en memoria

**Alternativa descartada:** Cargar todo y filtrar en memoria - no escala con datasets grandes.

### 3. OUTPUT INSERTED para generación de código

**Decisión:** Usar OUTPUT INSERTED.codigo_inventario en el INSERT para recuperar el código generado en la misma operación.

**Racional:**
- Atómico por diseño de MSSQL
- Una sola query, más eficiente que INSERT + SELECT SCOPE_IDENTITY()
- Sql2o soporta esto con executeAndFetch()

**Alternativas descartadas:**
- INSERT + SELECT SCOPE_IDENTITY() - dos operaciones, riesgo de inconsistencia
- No transaccional simple - estado inconsistente si falla recuperación

### 4. Mapeo polimórfico con factory pattern

**Decisión:** Método privado factory en MssqlVehiculoRepository que lee tipo_vehiculo y crea Automovil/Camioneta/Motocicleta según corresponda.

**Racional:**
- Centraliza la lógica de instanciación
- Usa TipoVehiculo.fromString() para conversión
- Maneja carroceria y tipoMotocicleta según el tipo

### 5. Estructura de paquetes

**Decisión:**
```
concesionaria/repository/
  RepositorioGenerico.java (interfaz)
  IndiceGenerico.java (clase)
  VehiculoRepository.java (interfaz)
  MssqlVehiculoRepository.java (clase)
```

**Racional:** Sigue estructura existente del proyecto, mantiene separación de capas.

## Risks / Trade-offs

**Riesgo:** OUTPUT INSERTED puede fallar si Sql2o no maneja correctamente el resultado.
**Mitigación:** Probar con query simple primero, verificar que executeAndFetch() devuelva el código.

**Riesgo:** Mapeo polimórfico puede fallar si se agrega nuevo tipo de vehículo sin actualizar factory.
**Mitigación:** Lanzar PersistenciaException con mensaje claro si tipo_vehiculo no coincide con ninguno conocido.

**Trade-off:** IndiceGenerico no sincroniza con DB - si se usa como caché, puede quedar desactualizado.
**Mitigación:** Documentar claramente que es utilidad, no caché. Si se necesita caché, implementar en service layer.

## Open Questions

Ninguna - las decisiones cubren todos los aspectos del diseño sin ambigüedades que afecten la implementación.
