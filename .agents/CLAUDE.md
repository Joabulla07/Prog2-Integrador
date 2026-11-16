# CLAUDE.md

Memoria persistente para agentes basados en Claude (Cline, Windsurf, Cascade) que trabajan en este proyecto.

## Información del Proyecto

**Nombre**: Prog2-Integrador (Concesionaria)
**Materia**: Programación II
**Lenguaje**: Java (sin frameworks, JDBC directo)
**Base de datos**: SQL Server en Docker/Podman

## Estructura del Proyecto

```
Prog2-Integrador/
├── src/concesionaria/
│   ├── modelo/          # Clases de modelo (Cliente, Vehiculo, etc.)
│   ├── repositorio/     # Acceso a datos (JDBC)
│   ├── servicio/        # Lógica de negocio
│   └── config/          # Configuración (DB, properties)
├── lib/                 # Dependencias JAR
├── sql/                 # Scripts SQL
├── openspec/            # Configuración de OpenSpec
└── .agents/             # Skills y contexto para agentes
```

## Ejecución

**Windows (PowerShell)**:
```powershell
.\run.ps1
javac -cp ".;lib/*" -d out src/concesionaria/*.java
java -cp "out;lib/*" concesionaria.Main
```

**Linux/Mac**:
```bash
./run.sh
javac -cp ".:lib/*" -d out src/concesionaria/*.java
java -cp "out:lib/*" concesionaria.Main
```

## Herramientas Configuradas

### OpenSpec
- **Versión**: 2.0.0
- **Propósito**: Desarrollo dirigido por especificaciones
- **Comando inicial**: `/openspec-propose "tu idea"`
- **Config**: `openspec/config.yaml`
- **Skills**: `.agents/skills/openspec-*`

### Caveman
- **Propósito**: Observabilidad y optimización de tokens
- **Skills**: `.agents/skills.md` contiene la lista completa
- **Modos**: `caveman`, `ultracave`, `megacave`

## Reglas Importantes

1. **Idioma**: Todo el código, documentación y comentarios deben estar en español.
2. **Compatibilidad**: El código debe funcionar en Windows y Linux.
3. **Base de datos**: SQL Server debe estar corriendo antes de ejecutar la aplicación.
4. **OpenSpec**: Para cambios estructurales, usa el flujo de OpenSpec (propose → review → apply → archive).
5. **Brevedad**: Prefiere respuestas cortas y técnicas.

## Modelos de Datos Principales

- **Cliente**: Información de clientes de la concesionaria
- **Vehiculo**: Inventario de vehículos
- **Empleado**: Personal de la concesionaria
- **Sucursal**: Ubicaciones de la concesionaria
- **BoletaCompraVenta**: Transacciones de compra/venta
- **PlanFinanciamiento**: Planes de pago
- **Proveedor**: Proveedores de vehículos
- **Marca**: Marcas de vehículos
- **ModeloVehiculo**: Modelos específicos

## Dependencias

Las dependencias JAR deben estar en `lib/`. Incluyen drivers JDBC y otras librerías necesarias.

## Configuración de Base de Datos

- **Usuario/Password**: Ver archivo `.env` o `credentials.java`
- **Contenedor**: Levantado con `docker-compose.yml` o `Dockerfile`
- **Scripts**: `sql/` contiene scripts de inicialización

## Notas para Agentes

- Antes de hacer cambios, revisa `openspec/config.yaml` para contexto específico.
- Usa los skills de OpenSpec para cambios estructurales.
- Mantén la documentación en `.agents/` actualizada.
- Para optimización de tokens, considera usar el modo `caveman`.
