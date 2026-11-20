# Spec Delta

## Purpose

Define la jerarquía de clases del dominio de vehículos con clase abstracta Vehiculo y subclases específicas, siguiendo patrones JavaBean/POJO y usando Wrappers para tipos primitivos.

## ADDED Requirements

### Requirement: Clase abstracta Vehiculo como base de jerarquía
El sistema SHALL proporcionar una clase abstracta Vehiculo que implemente interfaces Identificable, EvaluatableComercial y Serializable, con atributos privados usando Wrappers y constructores JavaBean.

#### Scenario: Constructor vacío JavaBean
- **WHEN** se invoca el constructor público sin argumentos de Vehiculo
- **THEN** se crea una instancia con todos los atributos en null (no se puede instanciar directamente por ser abstracta, pero las subclases heredan este constructor)

#### Scenario: Constructor parametrizado completo
- **WHEN** se invoca el constructor parametrizado de Vehiculo con todos los campos
- **THEN** se inicializa codigoInventario, marca, modelo, anio, color, estado, kilometraje y precioLista con los valores proporcionados

#### Scenario: Atributos con Wrappers
- **WHEN** se inspeccionan los atributos de Vehiculo
- **THEN** anio y kilometraje son de tipo Integer, precioLista es Double, y el resto son String o enums

#### Scenario: Getters y setters públicos
- **WHEN** se invocan getters y setters en Vehiculo
- **THEN** todos los atributos privados son accesibles mediante métodos públicos getXxx() y setXxx()

### Requirement: Métodos polimórficos abstractos en Vehiculo
El sistema SHALL declarar métodos abstractos en Vehiculo que cada subclase debe implementar para proporcionar comportamiento específico de tipo de vehículo.

#### Scenario: Obtener tipo de vehículo polimórfico
- **WHEN** se invoca obtenerTipoVehiculo() en una instancia de Vehiculo
- **THEN** la subclase devuelve su TipoVehiculo específico (AUTOMOVIL, CAMIONETA o MOTOCICLETA)

#### Scenario: Obtener tipo de estructura
- **WHEN** se invoca obtenerTipoEstructura() en una instancia de Vehiculo
- **THEN** la subclase devuelve una descripción de su estructura (ej. "Automóvil con carrocería")

#### Scenario: Obtener ficha directiva
- **WHEN** se invoca obtenerFichaDirectiva() en una instancia de Vehiculo
- **THEN** la subclase devuelve un resumen ejecutivo con sus atributos específicos para directivos

### Requirement: Subclase Automovil extiende Vehiculo
El sistema SHALL proporcionar una subclase Automovil que extienda Vehiculo, agregue el atributo carroceria como String libre validado, e implemente los métodos polimórficos.

#### Scenario: Automovil devuelve tipo AUTOMOVIL
- **WHEN** se invoca obtenerTipoVehiculo() en una instancia de Automovil
- **THEN** se devuelve TipoVehiculo.AUTOMOVIL

#### Scenario: Automovil con carrocería
- **WHEN** se crea un Automovil con carrocería "Sedán"
- **THEN** el atributo carroceria se almacena como "Sedán" y es accesible via getter/setter

#### Scenario: Estructura de Automovil
- **WHEN** se invoca obtenerTipoEstructura() en Automovil
- **THEN** se devuelve "Automóvil con carrocería [valor]"

### Requirement: Subclase Camioneta extiende Vehiculo
El sistema SHALL proporcionar una subclase Camioneta que extienda Vehiculo, agregue el atributo carroceria como String libre validado, e implemente los métodos polimórficos.

#### Scenario: Camioneta devuelve tipo CAMIONETA
- **WHEN** se invoca obtenerTipoVehiculo() en una instancia de Camioneta
- **THEN** se devuelve TipoVehiculo.CAMIONETA

#### Scenario: Camioneta con carrocería
- **WHEN** se crea una Camioneta con carrocería "Pick-up Doble Cabina"
- **THEN** el atributo carroceria se almacena como "Pick-up Doble Cabina" y es accesible via getter/setter

#### Scenario: Estructura de Camioneta
- **WHEN** se invoca obtenerTipoEstructura() en Camioneta
- **THEN** se devuelve "Camioneta con carrocería [valor]"

### Requirement: Subclase Motocicleta extiende Vehiculo
El sistema SHALL proporcionar una subclase Motocicleta que extienda Vehiculo, agregue el atributo tipoMotocicleta como String libre validado, e implemente los métodos polimórficos.

#### Scenario: Motocicleta devuelve tipo MOTOCICLETA
- **WHEN** se invoca obtenerTipoVehiculo() en una instancia de Motocicleta
- **THEN** se devuelve TipoVehiculo.MOTOCICLETA

#### Scenario: Motocicleta con tipo
- **WHEN** se crea una Motocicleta con tipoMotocicleta "Naked"
- **THEN** el atributo tipoMotocicleta se almacena como "Naked" y es accesible via getter/setter

#### Scenario: Estructura de Motocicleta
- **WHEN** se invoca obtenerTipoEstructura() en Motocicleta
- **THEN** se devuelve "Motocicleta tipo [valor]"

### Requirement: No instanciación directa de Vehiculo
El sistema SHALL impedir la instanciación directa de Vehiculo por ser abstracta, permitiendo solo instancias de sus subclases.

#### Scenario: Error al instanciar Vehiculo directamente
- **WHEN** se intenta instanciar Vehiculo directamente con new Vehiculo()
- **THEN** el compilador lanza error indicando que Vehiculo es abstracta y no puede ser instanciada

#### Scenario: Instanciación vía subclases
- **WHEN** se instancian Automovil, Camioneta o Motocicleta
- **THEN** la instanciación es exitosa y las instancias son reconocidas como Vehiculo vía polimorfismo
